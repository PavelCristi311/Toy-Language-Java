package model.visualizer;

public class TreeLayout {

    private static final int X_GAP = 140;
    private static final int Y_GAP = 80;

    public static void apply(Node root) {
        if (root == null) return;

        place(root, 0, 0);
    }

    private static void place(Node n, int x, int y) {
        if (n == null) return;
        n.x = x;
        n.y = y;

        place(n.left,  x - X_GAP, y + Y_GAP);

        place(n.right, x + X_GAP, y + Y_GAP);
    }
}
