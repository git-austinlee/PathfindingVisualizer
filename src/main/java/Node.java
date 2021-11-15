import java.awt.Point;

public class Node {
    private Point location;
    private Node parent;
    private int GCost = 0;
    private int HCost = 0;
    private int FCost = 0;

    public Node(Point location) {
        this.location = location;
    }

    // Distance from starting node when traversing through parent node
    public void CalculateGCost() {
        GCost = CalculateDistance(this.location, parent.getLocation()) + parent.getGCost();
    }

    // Distance from ending node
    public void CalculateHCost(Node endNode) { HCost = CalculateDistance(this.location, endNode.getLocation()); }

    // G cost + H cost
    public void CalculateFCost() { FCost = GCost + HCost; }

    public static int CalculateDistance(Point current, Point previous) {
        // calculate distance using euclidean heuristic
        return (int) (Math.sqrt(Math.pow((current.x - previous.x), 2) + Math.pow((current.y - previous.y), 2)) * 10);
    }

    // region getters and setters
    public Point getLocation() { return location; }

    public void setLocation(Point location) { this.location = location; }

    public int getGCost() { return GCost; }

    public void setGCost(int GCost) { this.GCost = GCost; }

    public int getHCost() { return HCost; }

    public void setHCost(int HCost) { this.HCost = HCost; }

    public int getFCost() { return FCost; }

    public void setFCost(int FCost) { this.FCost = FCost; }

    public Node getParent() { return parent; }

    public void setParent(Node parent) { this.parent = parent; }
    //endregion

    // region override
    @Override
    public boolean equals(Object o) {
        if( o == this ) { return true; }
        if( !(o instanceof Node n) ) { return false; }
        return location.x == n.getLocation().x && location.y == n.getLocation().y;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Node@ <%d, %d>", location.x, location.y);
    }
    // endregion
}
