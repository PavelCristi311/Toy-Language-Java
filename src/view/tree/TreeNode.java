package view.tree;

public class TreeNode {
    private final String data;
    private TreeNode left;
    private TreeNode right;
    private int x, y;
    private int width;
    private int height;
    private int subtreeWidth;

    public TreeNode(String data) {
        this.data = data;
        this.width = 150;
        this.height = 60;
        this.subtreeWidth = 150;
    }

    public String getData() { return data; }
    public TreeNode getLeft() { return left; }
    public TreeNode getRight() { return right; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setLeft(TreeNode left) { this.left = left; }
    public void setRight(TreeNode right) { this.right = right; }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getSubtreeWidth() { return subtreeWidth; }
    public void setSubtreeWidth(int subtreeWidth) { this.subtreeWidth = subtreeWidth; }
}
