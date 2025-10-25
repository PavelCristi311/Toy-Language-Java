package model.visualizer;

public class Node {
    public final String label;
    public Node left, right;
    public int x, y;
    public Node(String label) { this.label = label; }
}