import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

public class gui extends JPanel
                implements MouseListener, MouseMotionListener, KeyListener {

    private GridLayout gridPanel;
    private JPanel optionsPanel;
    private JPanel controlsPanel;
    private JLabel setStartLabel;
    private JLabel setEndLabel;
    private JLabel setObstacleLabel;
    private JLabel eraseLabel;
    private JButton startButton;
    private JButton resetButton;
    private JPanel startButtonContainer;
    private JPanel algorithmPanel;
    private JRadioButton aStarRadioButton;
    private JRadioButton dijkstraRadioButton;
    private JPanel resultPanel;
    private JLabel resultLabel;

    private char keyPressed = '_';

    public final int windowWidth = 1280;
    public final int windowHeight = 720;
    public int squareSize;
    public int numRows;
    public int numCols;
    public AStar aStar;

    public gui() {
        numRows = 45;
        numCols = 80;
        squareSize = Math.min(windowWidth / numCols, windowHeight / numRows);
        initializeComponents();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        aStar = new AStar(gridPanel);
    }

    public class GridLayout extends JPanel
    {
        public int squareSize;
        public int windowWidth;
        public int windowHeight;

        public GridLayout() {
            super();
            this.squareSize = gui.this.squareSize;
            this.windowWidth = gui.this.windowWidth;
            this.windowHeight = gui.this.windowHeight;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw grid
            g.setColor(Color.darkGray);
            for( int i = 0; i < windowWidth; i += squareSize ) {
                for( int j = 0; j < windowHeight; j += squareSize ) {
                    g.drawRect(i, j, squareSize, squareSize);
                }
            }

            // fill obstacles
            g.setColor(Color.black);
            for( Node obstacle : aStar.getObstacleList() ) {
                g.fillRect(obstacle.getLocation().x + 1, obstacle.getLocation().y + 1, squareSize - 1, squareSize - 1);
            }

            // fill openList
            g.setColor(Color.orange);
            for( Node open : aStar.getOpenList() ) {
                g.fillRect(open.getLocation().x + 1, open.getLocation().y + 1, squareSize - 1, squareSize - 1);
            }

            // fill closedList
            g.setColor(Color.lightGray);
            for( Node closed : aStar.getClosedList() ) {
                g.fillRect(closed.getLocation().x + 1, closed.getLocation().y + 1, squareSize - 1, squareSize - 1);
            }

            // fill path if found
            if( aStar.getPath() != null ) {
                g.setColor(Color.green);
                for( Node path : aStar.getPath() ) {
                    g.fillRect(path.getLocation().x + 1, path.getLocation().y + 1, squareSize - 1, squareSize - 1);
                }
            }

            // fill start node
            if( aStar.getStartNode() != null ) {
                g.setColor(Color.blue);
                g.fillRect(aStar.getStartNode().getLocation().x + 1, aStar.getStartNode().getLocation().y + 1, squareSize - 1, squareSize - 1);
            }

            // fill end node
            if( aStar.getEndNode() != null ) {
                g.setColor(Color.magenta);
                g.fillRect(aStar.getEndNode().getLocation().x + 1, aStar.getEndNode().getLocation().y + 1, squareSize - 1, squareSize - 1);
            }

            this.revalidate();
            this.repaint();
        }
    }

    public void mouseDragged(MouseEvent e) { handleGridInputs(e); }
    public void mouseClicked(MouseEvent e) { handleGridInputs(e); }
    public void mouseMoved(MouseEvent e) { handleGridInputs(e); }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void keyPressed(KeyEvent e) { keyPressed = e.getKeyChar(); }
    public void keyReleased(KeyEvent e) { keyPressed = '_'; }
    public void keyTyped(KeyEvent e) {}

    public void handleGridInputs(MouseEvent e) {
        int x = e.getX() - (e.getX() % squareSize);
        int y = e.getY() - (e.getY() % squareSize);
        Node n = new Node(new Point(x, y));

        if( SwingUtilities.isLeftMouseButton(e) ) {
            if( keyPressed == 's' ) {
                aStar.setStartNode(n);
            }
            else if( keyPressed == 'e' ) {
                aStar.setEndNode(n);
            }
        }
        else if( SwingUtilities.isRightMouseButton(e) ) {
            if( aStar.getStartNode() != null && aStar.getStartNode().equals(n) ) {
                aStar.setStartNode(null);
            }
            if( aStar.getEndNode() != null && aStar.getEndNode().equals(n) ) {
                aStar.setEndNode(null);
            }
            aStar.removeObstacle(n);
        }
        else if( SwingUtilities.isMiddleMouseButton(e) ) {
            aStar.addObstacle(n);
        }
    }

    public void initializeComponents() {
        this.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.setPreferredSize(new Dimension(1280, 850));
        this.setFocusable(true);
        gridPanel = new GridLayout();
        gridPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new LineBorder(Color.BLACK));
        gridPanel.setOpaque(true);
        gridPanel.setFocusable(true);
        gridPanel.setPreferredSize(new Dimension(1280, 720));
        this.add(gridPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(1280, 720), null, 0, true));
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        optionsPanel.setBackground(new Color(-1578778));
        this.add(optionsPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        // region controls panel
        //controlsContainer = new JPanel();
        //controlsContainer.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        //controlsContainer.setOpaque(false);
        //optionsPanel.add(controlsContainer, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(350, -1), null, 0, false));
        controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 20), -1, -1));
        controlsPanel.setAlignmentX(0.0f);
        controlsPanel.setFocusable(false);
        controlsPanel.setOpaque(false);
        optionsPanel.add(controlsPanel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        controlsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        setStartLabel = new JLabel();
        setStartLabel.setAlignmentX(1.0f);
        setStartLabel.setEnabled(true);
        setStartLabel.setFocusable(false);
        setStartLabel.setForeground(new Color(-16777216));
        setStartLabel.setOpaque(false);
        setStartLabel.setText("Set start node: Hold \"s\" + left click");
        controlsPanel.add(setStartLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        setEndLabel = new JLabel();
        setEndLabel.setForeground(new Color(-16777216));
        setEndLabel.setText("Set end node: Hold \"e\" + left click");
        controlsPanel.add(setEndLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        setObstacleLabel = new JLabel();
        setObstacleLabel.setForeground(new Color(-16777216));
        setObstacleLabel.setText("Set obstacle: Hold middle click");
        controlsPanel.add(setObstacleLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        eraseLabel = new JLabel();
        eraseLabel.setForeground(new Color(-16777216));
        eraseLabel.setText("Erase: Right click");
        controlsPanel.add(eraseLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final Spacer spacer1 = new Spacer();
        optionsPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, new Dimension(20, -1), 0, false));
        // endregion
        // region algorithm panel
        algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 20), -1, -1));
        algorithmPanel.setBackground(new Color(-1578778));
        algorithmPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Algorithm", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        aStarRadioButton = new JRadioButton("A*", true);
        aStarRadioButton.setEnabled(true);
        aStarRadioButton.setBackground(new Color(-1578778));
        algorithmPanel.add(aStarRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        dijkstraRadioButton = new JRadioButton("Dijkstra");
        dijkstraRadioButton.setEnabled(false);
        dijkstraRadioButton.setBackground(new Color(-1578778));
        algorithmPanel.add(dijkstraRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        optionsPanel.add(algorithmPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        // endregion
        // region result label
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 20), -1, -1));
        resultPanel.setOpaque(false);
        resultLabel = new JLabel();
        resultPanel.add(resultLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        optionsPanel.add(resultPanel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        // endregion
        // region start button container
        startButtonContainer = new JPanel();
        startButtonContainer.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 20), -1, -1));
        startButtonContainer.setOpaque(false);
        optionsPanel.add(startButtonContainer, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        startButton = new JButton();
        startButton.setAlignmentY(0.5f);
        startButton.setPreferredSize(new Dimension(75, 45));
        startButton.setOpaque(true);
        startButton.setText("Start");
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pathFound = aStar.run();
                startButton.setEnabled(false);
                if( pathFound == 1 ) {
                    resultLabel.setText("Path found!");
                }
                else if ( pathFound == 0 ) {
                    resultLabel.setText("Path not found!");
                }
                else {
                    startButton.setEnabled(true);
                }
            }
        });
        resetButton = new JButton();
        resetButton.setAlignmentY(0.5f);
        resetButton.setPreferredSize(new Dimension(75, 45));
        resetButton.setOpaque(true);
        resetButton.setText("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aStar.reset();
                startButton.setEnabled(true);
                resultLabel.setText("");
            }
        });
        startButtonContainer.add(startButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startButtonContainer.add(resetButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        //final Spacer spacer2 = new Spacer();
        //startButtonContainer.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        //final Spacer spacer3 = new Spacer();
        //startButtonContainer.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(40, -1), null, 0, false));
        // endregion
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pathfinding Visualizer");
        frame.setContentPane(new gui());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}

