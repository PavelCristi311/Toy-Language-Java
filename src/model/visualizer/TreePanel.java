package model.visualizer;

import model.visualizer.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Objects;


public class TreePanel extends JPanel {
    private Node root;

    private final int PADDING_X = 12;
    private final int PADDING_Y = 6;
    private final int ARC = 12;
    private final int LEVEL_DY = 60;
    private final Stroke EDGE_STROKE = new BasicStroke(2f);

    public TreePanel(Node root) {
        this.root = root;
        setBackground(Color.white);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
    }

    public void setRoot(Node root) {
        this.root = root;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (root == null) return;
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(EDGE_STROKE);
        drawEdges(g, root);
        drawNodes(g, root);

        g.dispose();
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
        Rectangle parentBox = nodeBox(parent, g.getFontMetrics());
        Rectangle childBox = nodeBox(child, g.getFontMetrics());

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

        if (ux == 0 && uy == 0) {
            uy = -1;
        }
        double len = Math.hypot(ux, uy);
        ux /= len;
        uy /= len;
        int t = 8;
        Polygon tri = new Polygon();
        tri.addPoint(x, y);
        tri.addPoint((int) (x - ux * t - uy * 4), (int) (y - uy * t + ux * 4));
        tri.addPoint((int) (x - ux * t + uy * 4), (int) (y - uy * t - ux * 4));
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
        int tx = r.x + (r.width - fm.stringWidth(n.label)) / 2;
        int ty = r.y + (r.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(Objects.toString(n.label, ""), tx, ty);
    }

    private Rectangle nodeBox(Node n, FontMetrics fm) {
        String text = Objects.toString(n.label, "");
        int w = Math.max(40, fm.stringWidth(text) + 2 * PADDING_X);
        int h = Math.max(28, fm.getHeight() + 2 * PADDING_Y);
        int x = n.x - w / 2;
        int y = n.y - h / 2;
        return new Rectangle(x, y, w, h);
    }


    public void fitToContent() {
        if (root == null) return;
        Node start = (root.left != null ? root.left : root);

        FontMetrics fm = getFontMetrics(getFont());
        Rectangle bounds = measureBounds(start);

        if (root.left != null) {
            Rectangle r0 = nodeBox(root, fm);
            bounds = bounds.union(r0);
        }

        int MARGIN = 40;
        int dx = Math.max(0, MARGIN - bounds.x);
        int dy = Math.max(0, MARGIN - bounds.y);

        if (dx != 0 || dy != 0) {

            shiftAll(root, dx, dy);

        }

        Rectangle b2 = measureBounds(start);
        if (root.left != null) b2 = b2.union(nodeBox(root, fm));

        int w = Math.max(800, b2.x + b2.width + MARGIN);
        int h = Math.max(600, b2.y + b2.height + MARGIN);
        setPreferredSize(new Dimension(w, h));
        revalidate();
    }

    private Rectangle measureBounds(Node n) {
        FontMetrics fm = getFontMetrics(getFont());
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


    private void shiftAll(Node n, int dx, int dy) {
        if (n == null) return;
        shiftAll(n.left, dx, dy);
        n.x += dx;
        n.y += dy;
        shiftAll(n.right, dx, dy);
    }
}