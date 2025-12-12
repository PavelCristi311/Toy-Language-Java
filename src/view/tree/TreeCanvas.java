package view.tree;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeCanvas extends Canvas {
    private TreeNode root;
    private ScrollPane ownerScrollPane;
    private Group zoomGroup;

    private static final int MIN_NODE_WIDTH = 100;
    private static final int MAX_NODE_WIDTH = 400;
    private static final int BASE_NODE_HEIGHT = 60;
    private static final int LINE_HEIGHT = 18;
    private static final int HORIZONTAL_PADDING = 10;
    private static final int VERTICAL_PADDING = 10;
    private static final int LEVEL_VERTICAL_GAP = 80;
    private static final int SIBLING_HORIZONTAL_GAP = 30;
    private static final int ROOT_MARGIN_X = 40;
    private static final int ROOT_MARGIN_Y = 40;
    private static final int RIGHT_MARGIN = 50;

    private final Font nodeFont = Font.font("Monospaced", 12);

    private static final Color EXPORT_BACKGROUND = Color.web("#212121");
    private static final double EXPORT_SCALE = 3.0;

    public TreeCanvas(double width, double height) {
        super(width, height);
    }

    private static class Bounds {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
    }

    private Bounds computeBounds(TreeNode node) {
        Bounds b = new Bounds();
        collectBounds(node, b);
        if (b.minX == Double.MAX_VALUE) {
            b.minX = b.minY = 0;
            b.maxX = b.maxY = 0;
        }
        return b;
    }

    private void collectBounds(TreeNode node, Bounds b) {
        if (node == null) return;

        int x = node.getX();
        int y = node.getY();
        int w = node.getWidth();
        int h = node.getHeight();

        double left = x - w / 2.0;
        double right = x + w / 2.0;
        double top = y - h / 2.0;
        double bottom = y + h / 2.0;

        b.minX = Math.min(b.minX, left);
        b.maxX = Math.max(b.maxX, right);
        b.minY = Math.min(b.minY, top);
        b.maxY = Math.max(b.maxY, bottom);

        collectBounds(node.getLeft(), b);
        collectBounds(node.getRight(), b);
    }

    public void setRoot(TreeNode root) {
        this.root = root;
        redraw();
    }

    public void setOwnerScrollPane(ScrollPane scrollPane) {
        this.ownerScrollPane = scrollPane;
    }

    public void setZoomGroup(Group zoomGroup) {
        this.zoomGroup = zoomGroup;
    }

    private void drawTree(GraphicsContext gc, TreeNode node) {
        if (node == null) return;

        int nodeWidth = node.getWidth();
        int nodeHeight = node.getHeight();

        if (node.getLeft() != null) {
            gc.setStroke(Color.web("#1072a3"));
            gc.setLineWidth(2);
            gc.strokeLine(
                    node.getX(),
                    node.getY() + nodeHeight / 2.0,
                    node.getLeft().getX(),
                    node.getLeft().getY() - node.getLeft().getHeight() / 2.0
            );
            drawTree(gc, node.getLeft());
        }

        if (node.getRight() != null) {
            gc.setStroke(Color.web("#1072a3"));
            gc.setLineWidth(2);
            gc.strokeLine(
                    node.getX(),
                    node.getY() + nodeHeight / 2.0,
                    node.getRight().getX(),
                    node.getRight().getY() - node.getRight().getHeight() / 2.0
            );
            drawTree(gc, node.getRight());
        }

        gc.setFill(Color.web("#4d4d4f"));
        gc.fillRoundRect(
                node.getX() - nodeWidth / 2.0,
                node.getY() - nodeHeight / 2.0,
                nodeWidth,
                nodeHeight,
                10,
                10
        );

        gc.setStroke(Color.web("#1072a3"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(
                node.getX() - nodeWidth / 2.0,
                node.getY() - nodeHeight / 2.0,
                nodeWidth,
                nodeHeight,
                10,
                10
        );

        gc.setFill(Color.web("#F8F8F2"));
        gc.setFont(nodeFont);
        gc.setTextAlign(TextAlignment.CENTER);

        List<String> lines = wrapText(node.getData(), nodeWidth - 2 * HORIZONTAL_PADDING);
        int totalTextHeight = lines.size() * LINE_HEIGHT;
        double startY = node.getY() - totalTextHeight / 2.0 + LINE_HEIGHT * 0.75;

        for (int i = 0; i < lines.size(); i++) {
            gc.fillText(lines.get(i), node.getX(), startY + i * LINE_HEIGHT);
        }
    }

    public void redraw() {
        if (root == null) {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            return;
        }

        computeNodeSize(root);

        computeSubtreeWidth(root);

        layoutTree(root, ROOT_MARGIN_X, ROOT_MARGIN_Y);


        Bounds b = computeBounds(root);
        double rightEdge = b.maxX;
        double requiredWidth = rightEdge + RIGHT_MARGIN;

        if (requiredWidth > getWidth()) {
            setWidth(requiredWidth);
        }

        double requiredHeight = b.maxY + ROOT_MARGIN_Y;
        if (requiredHeight > getHeight()) {
            setHeight(requiredHeight);
        }

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawTree(gc, root);
    }

    public void scrollToNode(TreeNode node) {
        if (node == null || ownerScrollPane == null) {
            return;
        }

        double scale = (zoomGroup != null ? zoomGroup.getScaleX() : 1.0);

        double targetX = node.getX() * scale;
        double targetY = node.getY() * scale;

        double viewportWidth = ownerScrollPane.getViewportBounds().getWidth();
        double viewportHeight = ownerScrollPane.getViewportBounds().getHeight();

        if (viewportWidth <= 0 || viewportHeight <= 0) {
            return;
        }

        double contentWidth = getWidth() * scale;
        double contentHeight = getHeight() * scale;

        double scrollX = targetX - viewportWidth / 2.0;
        double scrollY = targetY - viewportHeight / 2.0;

        scrollX = Math.max(0, Math.min(scrollX, contentWidth - viewportWidth));
        scrollY = Math.max(0, Math.min(scrollY, contentHeight - viewportHeight));

        double targetH = (contentWidth <= viewportWidth) ? 0.0 : scrollX / (contentWidth - viewportWidth);
        double targetV = (contentHeight <= viewportHeight) ? 0.0 : scrollY / (contentHeight - viewportHeight);

        animateScrollTo(targetH, targetV, Duration.millis(500));
    }

    private void animateScrollTo(double targetH, double targetV, Duration duration) {
        double startH = ownerScrollPane.getHvalue();
        double startV = ownerScrollPane.getVvalue();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(ownerScrollPane.hvalueProperty(), startH),
                        new KeyValue(ownerScrollPane.vvalueProperty(), startV)
                ),
                new KeyFrame(duration,
                        new KeyValue(ownerScrollPane.hvalueProperty(), targetH, Interpolator.EASE_BOTH),
                        new KeyValue(ownerScrollPane.vvalueProperty(), targetV, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
    }

    private void computeNodeSize(TreeNode node) {
        if (node == null) return;

        Text textNode = new Text(node.getData());
        textNode.setFont(nodeFont);
        double textWidth = textNode.getLayoutBounds().getWidth();
        int nodeWidth = (int) Math.max(
                MIN_NODE_WIDTH,
                Math.min(MAX_NODE_WIDTH, textWidth + 2 * HORIZONTAL_PADDING)
        );
        node.setWidth(nodeWidth);

        List<String> lines = wrapText(node.getData(), nodeWidth - 2 * HORIZONTAL_PADDING);
        int textHeight = lines.size() * LINE_HEIGHT + 2 * VERTICAL_PADDING;
        int nodeHeight = Math.max(BASE_NODE_HEIGHT, textHeight);
        node.setHeight(nodeHeight);

        computeNodeSize(node.getLeft());
        computeNodeSize(node.getRight());
    }


    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }

        Text helper = new Text();
        helper.setFont(nodeFont);

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {

            helper.setText(word);
            if (helper.getLayoutBounds().getWidth() > maxWidth) {

                if (!line.isEmpty()) {
                    lines.add(line.toString());
                    line.setLength(0);
                }

                StringBuilder part = new StringBuilder();
                for (int i = 0; i < word.length(); i++) {
                    part.append(word.charAt(i));
                    helper.setText(part.toString());
                    if (helper.getLayoutBounds().getWidth() > maxWidth && part.length() > 1) {
                        char last = part.charAt(part.length() - 1);
                        part.deleteCharAt(part.length() - 1);
                        lines.add(part.toString());
                        part.setLength(0);
                        part.append(last);
                    }
                }
                if (!part.isEmpty()) {
                    line = new StringBuilder(part.toString());
                }
            } else {
                String testLine = line.isEmpty() ? word : line + " " + word;
                helper.setText(testLine);
                if (helper.getLayoutBounds().getWidth() > maxWidth && !line.isEmpty()) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(testLine);
                }
            }
        }

        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        if (lines.isEmpty()) {
            lines.add("");
        }

        return lines;
    }

    private int computeSubtreeWidth(TreeNode node) {
        if (node == null) return 0;

        int nodeWidth = node.getWidth();
        int leftWidth = computeSubtreeWidth(node.getLeft());
        int rightWidth = computeSubtreeWidth(node.getRight());

        if (node.getLeft() == null && node.getRight() == null) {
            int w = nodeWidth + SIBLING_HORIZONTAL_GAP;
            node.setSubtreeWidth(w);
            return w;
        }

        if (node.getLeft() == null) leftWidth = nodeWidth;
        if (node.getRight() == null) rightWidth = nodeWidth;

        int subtreeWidth = leftWidth + SIBLING_HORIZONTAL_GAP + rightWidth;
        subtreeWidth = Math.max(subtreeWidth, nodeWidth + SIBLING_HORIZONTAL_GAP);

        node.setSubtreeWidth(subtreeWidth);
        return subtreeWidth;
    }

    private void layoutTree(TreeNode node, int x, int y) {
        if (node == null) return;

        int subtreeWidth = node.getSubtreeWidth();
        int nodeHeight = node.getHeight();

        int nodeX = x + subtreeWidth / 2;
        node.setPosition(nodeX, y + nodeHeight / 2);

        TreeNode left = node.getLeft();
        TreeNode right = node.getRight();

        if (left != null) {
            int leftWidth = left.getSubtreeWidth();
            layoutTree(left, x, y + nodeHeight + LEVEL_VERTICAL_GAP);
            x += leftWidth + SIBLING_HORIZONTAL_GAP;
        }

        if (right != null) {
            layoutTree(right, x, y + nodeHeight + LEVEL_VERTICAL_GAP);
        }
    }

    public void exportToPng(File file) throws IOException {

        double scale = EXPORT_SCALE;
        int exportWidth = (int) (getWidth() * scale);
        int exportHeight = (int) (getHeight() * scale);

        Canvas tmpCanvas = new Canvas(exportWidth, exportHeight);
        GraphicsContext gc = tmpCanvas.getGraphicsContext2D();

        gc.setFill(EXPORT_BACKGROUND);
        gc.fillRect(0, 0, exportWidth, exportHeight);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        params.setTransform(new Scale(scale, scale));

        WritableImage treeImage = snapshot(params, null);
        gc.drawImage(treeImage, 0, 0);

        java.net.URL logoUrl = getClass().getResource("../images/Larva.png");
        if (logoUrl != null) {
            javafx.scene.image.Image logo = new javafx.scene.image.Image(logoUrl.toExternalForm());

            double targetLogoWidth = exportWidth * 0.1;
            double ratio = logo.getWidth() / logo.getHeight();
            double targetLogoHeight = targetLogoWidth / ratio;

            double margin = exportWidth * 0.015;
            double y = exportHeight - targetLogoHeight - margin;

            gc.drawImage(logo, margin, y, targetLogoWidth, targetLogoHeight);
        } else {
            System.err.println("Logo resource `/images/Larva.png` not found on classpath.");
        }

        WritableImage combined = tmpCanvas.snapshot(null, null);
        java.awt.image.BufferedImage bufferedImage =
                SwingFXUtils.fromFXImage(combined, null);
        ImageIO.write(bufferedImage, "png", file);
    }
}
