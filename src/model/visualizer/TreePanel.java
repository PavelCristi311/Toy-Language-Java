package model.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;

public class TreePanel extends JPanel {
    private Node root;

    private final int PADDING_X = 12;
    private final int PADDING_Y = 6;
    private final int ARC = 12;

    private final int LEVEL_DY = 60;

    private final int H_GAP = 20;

    private final Stroke EDGE_STROKE = new BasicStroke(2f);

    private final Map<Node, Dimension> sizeCache = new HashMap<>();

    public TreePanel(Node root) {
        this.root = root;
        setBackground(Color.white);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
    }

    public void setRoot(Node root) {
        this.root = root;
        sizeCache.clear();
        revalidate();
        repaint();
    }

    public void relayout() {
        sizeCache.clear();
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (root == null) return;

        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        boolean shifted = fixHorizontalOverlaps(g);

        ensureMarginsAndMaybeCenter(g);

        if (shifted) fitToContent();

        g.setStroke(EDGE_STROKE);
        drawEdges(g, root);
        drawNodes(g, root);

        g.dispose();
    }


    private boolean fixHorizontalOverlaps(Graphics2D g) {
        sizeCache.clear();
        FontMetrics fm = g.getFontMetrics();

        Map<Integer, List<Node>> levels = new HashMap<>();
        collectByLevel(root, 0, levels);

        boolean shifted = false;

        for (int depth : new TreeSet<>(levels.keySet())) {
            List<Node> list = levels.get(depth);
            if (list == null || list.isEmpty()) continue;

            list.sort(Comparator.comparingInt(a -> a.x));

            int rightEdge = Integer.MIN_VALUE;
            for (Node n : list) {
                Rectangle r = nodeBox(n, fm);
                int minLeft = (rightEdge == Integer.MIN_VALUE) ? r.x : rightEdge + H_GAP;

                if (r.x < minLeft) {
                    int dx = minLeft - r.x;
                    shiftSubtree(n, dx);
                    shifted = true;

                    Rectangle r2 = nodeBox(n, fm);
                    rightEdge = r2.x + r2.width;
                } else {
                    rightEdge = Math.max(rightEdge, r.x + r.width);
                }
            }
        }

        return shifted;
    }

    private void collectByLevel(Node n, int depth, Map<Integer, List<Node>> levels) {
        if (n == null) return;
        levels.computeIfAbsent(depth, k -> new ArrayList<>()).add(n);
        collectByLevel(n.left, depth + 1, levels);
        collectByLevel(n.right, depth + 1, levels);
    }

    private void shiftSubtree(Node n, int dx) {
        if (n == null) return;
        n.x += dx;
        shiftSubtree(n.left, dx);
        shiftSubtree(n.right, dx);
    }


    private void ensureMarginsAndMaybeCenter(Graphics2D g) {
        final int MARGIN = 40;
        FontMetrics fm = g.getFontMetrics();
        Rectangle b = boundsAll(fm);

        int dx = 0, dy = 0;

        if (b.x < MARGIN) dx = MARGIN - b.x;
        if (b.y < MARGIN) dy = MARGIN - b.y;

        if (dx != 0 || dy != 0) {
            shiftWholeTree(dx, dy);
            b = boundsAll(fm);
        }

        int availableW = getWidth();
        if (availableW > 0 && b.width + 2 * MARGIN < availableW) {
            int wantLeft = (availableW - b.width) / 2;
            int delta = wantLeft - b.x;
            if (delta != 0) shiftWholeTree(delta, 0);
        }
    }

    private Rectangle boundsAll(FontMetrics fm) {
        Rectangle acc = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        boundsDfs(root, fm, acc);
        return acc;
    }

    private void boundsDfs(Node n, FontMetrics fm, Rectangle acc) {
        if (n == null) return;
        boundsDfs(n.left, fm, acc);
        Rectangle r = nodeBox(n, fm);
        if (r.x < acc.x) acc.x = r.x;
        if (r.y < acc.y) acc.y = r.y;
        int rx = r.x + r.width;
        int ry = r.y + r.height;
        int ax = acc.x + acc.width;
        int ay = acc.y + acc.height;
        if (rx > ax) acc.width = rx - acc.x;
        if (ry > ay) acc.height = ry - acc.y;
        boundsDfs(n.right, fm, acc);
    }

    private void shiftWholeTree(int dx, int dy) {
        shiftSubtree(root, dx);
        shiftSubtreeY(root, dy);
    }

    private void shiftSubtreeY(Node n, int dy) {
        if (n == null) return;
        n.y += dy;
        shiftSubtreeY(n.left, dy);
        shiftSubtreeY(n.right, dy);
    }


    private void drawEdges(Graphics2D g, Node n) {
        if (n == null) return;
        if (n.left != null) {
            drawEdgeOrthogonal(g, n, n.left);
            drawEdges(g, n.left);
        }
        if (n.right != null) {
            drawEdgeOrthogonal(g, n, n.right);
            drawEdges(g, n.right);
        }
    }

    private void drawEdgeOrthogonal(Graphics2D g, Node parent, Node child) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle parentBox = nodeBox(parent, fm);
        Rectangle childBox = nodeBox(child, fm);

        int x1 = parentBox.x + parentBox.width / 2;
        int y1 = parentBox.y + parentBox.height;
        int x2 = childBox.x + childBox.width / 2;
        int y2 = childBox.y;

        int midY = Math.min(y1 + LEVEL_DY / 2, (y1 + y2) / 2);

        g.setColor(new Color(130, 130, 130));
        Path2D p = new Path2D.Double();
        p.moveTo(x1, y1);
        p.lineTo(x1, midY);
        p.lineTo(x2, midY);
        p.lineTo(x2, y2);
        g.draw(p);

        drawArrowHead(g, x2, y2, 0, -1);
    }

    private void drawArrowHead(Graphics2D g, int x, int y, double ux, double uy) {
        if (ux == 0 && uy == 0) uy = -1;
        double len = Math.hypot(ux, uy);
        ux /= len;
        uy /= len;
        int t = 8;
        Polygon tri = new Polygon();
        tri.addPoint(x, y);
        tri.addPoint((int) (x - ux * t - uy * 4), (int) (y - uy * t + ux * 4));
        tri.addPoint((int) (x - ux * t + uy * 4), (int) (y - uy * 4 - ux * 4));
        tri.xpoints[2] = (int) (x - ux * t + uy * 4);
        tri.ypoints[2] = (int) (y - uy * t - ux * 4);
        g.fill(tri);
    }

    private void drawNodes(Graphics2D g, Node n) {
        if (n == null) return;
        drawNodes(g, n.left);
        drawNodeBox(g, n, new Color(240, 247, 255), new Color(25, 71, 138));
        drawNodes(g, n.right);
    }

    private void drawNodeBox(Graphics2D g, Node n, Color fill, Color stroke) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle r = nodeBox(n, fm);

        Shape box = new RoundRectangle2D.Double(r.x, r.y, r.width, r.height, ARC, ARC);
        g.setColor(fill);
        g.fill(box);
        g.setColor(stroke);
        g.setStroke(new BasicStroke(2f));
        g.draw(box);

        g.setColor(stroke.darker());
        String text = Objects.toString(n.label, "");
        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(text, tx, ty);
    }

    private Dimension measureNode(Node n, FontMetrics fm) {
        return sizeCache.computeIfAbsent(n, k -> {
            String text = Objects.toString(n.label, "");
            int w = Math.max(40, fm.stringWidth(text) + 2 * PADDING_X);
            int h = Math.max(28, fm.getHeight() + 2 * PADDING_Y);
            return new Dimension(w, h);
        });
    }

    private Rectangle nodeBox(Node n, FontMetrics fm) {
        Dimension d = measureNode(n, fm);
        int x = n.x - d.width / 2;
        int y = n.y - d.height / 2;
        return new Rectangle(x, y, d.width, d.height);
    }

    public void fitToContent() {
        if (root == null) return;
        FontMetrics fm = getFontMetrics(getFont());
        Rectangle bounds = measureBounds(root, fm);

        int MARGIN = 40;
        int w = Math.max(800, bounds.x + bounds.width + MARGIN);
        int h = Math.max(600, bounds.y + bounds.height + MARGIN);
        setPreferredSize(new Dimension(w, h));
        revalidate();
    }

    private Rectangle measureBounds(Node n, FontMetrics fm) {
        Rectangle acc = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        measureDfs(n, fm, acc);
        return acc;
    }

    private void measureDfs(Node n, FontMetrics fm, Rectangle acc) {
        if (n == null) return;
        measureDfs(n.left, fm, acc);
        Rectangle r = nodeBox(n, fm);
        if (r.x < acc.x) acc.x = r.x;
        if (r.y < acc.y) acc.y = r.y;
        int rx = r.x + r.width;
        int ry = r.y + r.height;
        int ax = acc.x + acc.width;
        int ay = acc.y + acc.height;
        if (rx > ax) acc.width = rx - acc.x;
        if (ry > ay) acc.height = ry - acc.y;
        measureDfs(n.right, fm, acc);
    }
}