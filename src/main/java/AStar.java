import java.awt.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

public class AStar {
    private Node startNode, endNode, current;
    private ArrayList<Node> openList, closedList, obstacleList, path;
    private gui.GridLayout board;
    private boolean running = false;
    private boolean foundPath = false;

    public AStar(gui.GridLayout board) {
        this.board = board;
        this.openList = new ArrayList<Node>();
        this.closedList = new ArrayList<Node>();
        this.obstacleList = new ArrayList<Node>();
        this.path = new ArrayList<Node>();
    }

    public int run() {
        if( checkValidSetup() ) {
            openList.add(startNode);
            running = true;

            while( running && !openList.isEmpty() ) {
                // get node in openList with smallest fCost
                MergeSort.sort(openList, 0, openList.size()-1);
                current = openList.remove(0);

                // add current node to closed list
                closedList.add(current);

                // found goal
                if( current.equals(endNode) ) {
                    foundPath = true;
                    running = false;
                    break;
                }

                // create list of neighboring nodes
                ArrayList<Node> neighbors = findNeighboringNodes();

                for( Node n : neighbors ) {
                    if( closedList.contains(n) ) {
                        continue;
                    }

                    if( !openList.contains(n) ) {
                        initNode(n);
                        openList.add(n);
                    }
                    else {
                        int tentativeG = Node.CalculateDistance(n.getLocation(), current.getLocation()) + current.getGCost();
                        int comparison = openList.indexOf(n);
                        Node openListNode = openList.get(comparison);
                        if( tentativeG < openList.get(comparison).getGCost() ) {
                            openList.remove(openListNode);
                            initNode(n);
                            openList.add(n);
                        }
                    }
                }

                board.paintImmediately(new Rectangle(board.windowWidth, board.windowHeight));
            }

            if( foundPath ) {
                path.add(current);
                Node pointer = current;
                while( pointer.getParent() != startNode ) {
                    pointer = pointer.getParent();
                    path.add(pointer);
                }
                return 1;
            }
            else {
                System.out.println("path not found");
                return 0;
            }
        }
        return -1;
    }

    public void reset() {
        startNode = null;
        endNode = null;
        current = null;
        openList.clear();
        closedList.clear();
        obstacleList.clear();
        path.clear();
        running = false;
        foundPath = false;
        board.paintImmediately(new Rectangle(board.windowWidth, board.windowHeight));
    }

    private void initNode(Node n) {
        n.setParent(current);
        n.CalculateGCost();
        n.CalculateHCost(endNode);
        n.CalculateFCost();
    }

    private ArrayList<Node> findNeighboringNodes() {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for( int i = -1; i < 2; i++ ) {
            for( int j = -1; j < 2; j++ ) {
                int x = current.getLocation().x + (board.squareSize * i);
                int y = current.getLocation().y + (board.squareSize * j);
                // check if neighbor is out of bounds
                if( (x >= 0 && y >= 0) && (x < board.windowWidth && y < board.windowHeight) ) {
                    Node n = new Node(new Point(x, y));
                    if( !n.equals(current) && !obstacleList.contains(n) ) {
                        neighbors.add(n);
                    }
                }
            }
        }
        return neighbors;
    }

    // TODO: create pause & resume
    public void pause() {
        running = false;
    }

    public void resume() {
        running = true;
    }

    private boolean checkValidSetup() {
        String message = "";
        if( startNode == null ) {
            message += "Please set a start node.\n";
        }
        if( endNode == null ) {
            message += "Please set a end node.\n";
        }

        if( message.length() > 0 ) {
            showMessageDialog(null, message);
            return false;
        }
        else {
            return true;
        }
    }

    // region getters and setters
    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
        if( this.startNode != null && endNode != null && this.startNode.getLocation() == endNode.getLocation() ) {
            endNode = null;
        }
        if( obstacleList.size() > 0 ) {
            removeObstacle(this.startNode);
        }
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
        if( this.endNode != null && startNode != null && this.endNode.getLocation() == startNode.getLocation() ) {
            startNode = null;
        }
        if( obstacleList.size() > 0 ) {
            removeObstacle(this.endNode);
        }
    }

    public ArrayList<Node> getObstacleList() {
        return obstacleList;
    }

    public void addObstacle(Node obstacle) {
        obstacleList.add(obstacle);
    }

    public void removeObstacle(Node obstacle) {
        if( obstacle != null )
            obstacleList.removeIf(x -> (x.getLocation().equals(obstacle.getLocation())));
    }

    public ArrayList<Node> getOpenList() {
        return openList;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    public ArrayList<Node> getPath() { return path; }

    // endregion
}
