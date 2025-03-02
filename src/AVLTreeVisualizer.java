import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Map;
import java.util.HashMap;

/**
 * AVLTreeVisualizer is a graphical user interface for visualizing AVL tree operations.
 * It provides an animated step-by-step insertion process with rotation effects.
 * The GUI includes a tree visualization panel, step tracking, and result display.
 */
public class AVLTreeVisualizer extends JPanel {
    private final AVLTree tree; // The AVL tree to visualize
    private final Map<AVLNode, Point> nodePositions = new HashMap<>(); // Stores positions of nodes
    private Timer animationTimer; // Timer for animations
    private float animationProgress = 0f; // Progress of the animation (0 to 1)
    private final Map<AVLNode, Point> startPositions = new HashMap<>(); // Start positions for animations
    private final Map<AVLNode, Point> endPositions = new HashMap<>(); // End positions for animations
    private JTextArea stepsArea; // Displays insertion steps
    private JTextArea resultArea; // Displays traversal results
    private JComboBox<String> speedControl; // Controls animation speed
    private Timer insertTimer; // Timer for inserting keys
    private final int[] keys; // Keys to insert into the tree
    private int currentIndex = 0; // Current index in the keys array
    private boolean finalTree = false; // Flag to indicate if the final tree is displayed

    /**
     * Constructs an AVLTreeVisualizer instance.
     *
     * @param tree The AVL tree to visualize.
     * @param keys The array of keys to insert into the tree.
     */
    public AVLTreeVisualizer(AVLTree tree, int[] keys) {
        this.tree = tree;
        this.keys = keys;

        // Set up the GUI
        setBackground(new Color(30, 30, 30)); // Dark background
        setLayout(new BorderLayout());

        // Create the bottom panel for steps, results, and controls
        JPanel bottomPanel = createBottomPanel();

        // Add a dividing line between the canvas and the bottom panel
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(255, 100, 100)); // Light blue line
        separator.setBackground(new Color(255, 100, 100)); // Light blue line

        // Wrap the bottom panel and separator in a new panel
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setBackground(new Color(45, 45, 45)); // Darker background
        bottomContainer.add(separator, BorderLayout.NORTH); // Add separator at the top
        bottomContainer.add(bottomPanel, BorderLayout.CENTER); // Add bottom panel below the separator

        add(bottomContainer, BorderLayout.SOUTH); // Add the container to the main panel

        // Set up the animation timer and start the insertion process
        setupAnimationTimer();
        startInsertionProcess();
    }

    /**
     * Creates the bottom panel containing step tracking, results, and controls.
     *
     * @return A JPanel containing GUI components.
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(45, 45, 45)); // Darker background

        // Steps and result areas
        JPanel stepsResultPanel = new JPanel(new GridLayout(1, 2));
        stepsResultPanel.setBackground(new Color(45, 45, 45));

        // Steps area with label
        JPanel stepsPanel = new JPanel(new BorderLayout());
        stepsPanel.setBackground(new Color(45, 45, 45));

        JLabel stepsLabel = new JLabel("Steps");
        stepsLabel.setFont(new Font("Monospaced", Font.BOLD, 16)); // Larger font
        stepsLabel.setForeground(new Color(100, 150, 255)); // White text
        stepsLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align
        stepsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding (top, left, bottom, right)
        stepsPanel.add(stepsLabel, BorderLayout.NORTH); // Add label at the top

        stepsArea = createTextArea();
        stepsPanel.add(createScrollPane(stepsArea), BorderLayout.CENTER); // Add text area below label

        stepsResultPanel.add(stepsPanel);

        // Result area with label
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(45, 45, 45));

        JLabel resultLabel = new JLabel("Result");
        resultLabel.setFont(new Font("Monospaced", Font.BOLD, 16)); // Larger font
        resultLabel.setForeground(new Color(100, 150, 255)); // White text
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add padding (top, left, bottom, right)
        resultPanel.add(resultLabel, BorderLayout.NORTH); // Add label at the top

        resultArea = createTextArea();
        resultPanel.add(createScrollPane(resultArea), BorderLayout.CENTER); // Add text area below label

        stepsResultPanel.add(resultPanel);

        bottomPanel.add(stepsResultPanel, BorderLayout.CENTER);

        // Control panel with restart button and speed control
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(45, 45, 45));

        // Restarts the insertion process
        JButton restartButton = createButton(_ -> restartTree());
        controlPanel.add(restartButton);

        speedControl = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        speedControl.setSelectedIndex(1); // Default to "Normal"
        speedControl.addActionListener(_ -> updateAnimationSpeed());
        controlPanel.add(speedControl);

        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    /**
     * Creates a non-editable text area.
     *
     * @return A styled JTextArea.
     */
    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(45, 45, 45));
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        return textArea;
    }

    /**
     * Wraps a text area inside a scroll pane.
     *
     * @param textArea The text area to wrap.
     * @return A JScrollPane containing the text area.
     */
    private JScrollPane createScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(getWidth(), 150));
        return scrollPane;
    }

    /**
     * Creates a button with an action listener.
     *
     * @param listener The ActionListener to handle button events.
     * @return A JButton.
     */
    private JButton createButton(ActionListener listener) {
        JButton button = new JButton("Restart");
        button.setFont(new Font("Monospaced", Font.PLAIN, 14));
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.BLACK);
        button.addActionListener(listener);
        return button;
    }

    /**
     * Initializes the animation timer.
     */
    private void setupAnimationTimer() {
        animationTimer = new Timer(16, _ -> {
            animationProgress += 0.02f; // Adjust animation speed
            if (animationProgress >= 1f) {
                animationProgress = 1f;
                animationTimer.stop();
            }
            repaint();
        });
    }

    /**
     * Updates animation speed based on user selection.
     */
    private void updateAnimationSpeed() {
        String selectedSpeed = (String) speedControl.getSelectedItem();
        if (selectedSpeed != null) {
            switch (selectedSpeed) {
                case "Slow": animationTimer.setDelay(32); break;
                case "Normal": animationTimer.setDelay(16); break;
                case "Fast": animationTimer.setDelay(8); break;
            }
        }
    }

    /**
     * Restarts the AVL tree visualization.
     */
    private void restartTree() {
        if (insertTimer != null) insertTimer.stop();
        tree.reset();
        stepsArea.setText("");
        resultArea.setText("");
        nodePositions.clear();
        startPositions.clear();
        endPositions.clear();
        animationProgress = 0f;
        currentIndex = 0;
        repaint();
        startInsertionProcess();
    }

    /**
     * Starts the insertion process, adding keys one by one with animation.
     */
    private void startInsertionProcess() {
        insertTimer = new Timer(1000, e -> {
            if (currentIndex < keys.length) {
                String step = tree.insert(keys[currentIndex]);
                animateRotation(step);
                currentIndex++;
            } else {
                ((Timer) e.getSource()).stop();
                resultArea.setText("\n ---> Insertion process completed <---\n");
                resultArea.append("\n Pre-order  : " + tree.preorder() + "\n");
                resultArea.append(" In-order   : " + tree.inorder() + "\n");
                resultArea.append(" Post-order : " + tree.postorder() + "\n");
                resultArea.append("\n Final tree structure:\n" + getTreeStructure(tree.getRoot()));

                if (!finalTree) {
                    finalTree = true;
                    System.out.println("---> Insertion process completed <---");
                    System.out.println(" Preorder: " + tree.preorder());
                    System.out.println(" Inorder: " + tree.inorder());
                    System.out.println(" Postorder: " + tree.postorder());
                    System.out.println("\nFinal tree structure:\n" + getTreeStructure(tree.getRoot()));
                }
            }
        });
        insertTimer.start();
    }

    /**
     * Returns the tree structure as a formatted string.
     * @param node The root node of the AVL tree.
     * @return A string representation of the tree structure.
     */
    private String getTreeStructure(AVLNode node) {
        if (node == null) return "Tree is empty.";
        StringBuilder sb = new StringBuilder();
        getTreeStructureHelper(node, sb, 0);
        return sb.toString();
    }

    /**
     * Helper method to recursively build the tree structure string.
     * @param node The current node being processed.
     * @param sb The StringBuilder to store the tree structure.
     * @param depth The depth level of the current node.
     */
    private void getTreeStructureHelper(AVLNode node, StringBuilder sb, int depth) {
        if (node == null) return;
        sb.append("|").append(depth).append("| ");
        sb.append("  ".repeat(Math.max(0, depth)));
        sb.append("Node: ").append(node.key).append(" (BF: ").append(tree.getBalanceFactor(node)).append(")\n");
        getTreeStructureHelper(node.left, sb, depth + 1);
        getTreeStructureHelper(node.right, sb, depth + 1);
    }

    /**
     * Draws the tree structure on the panel.
     * @param g The Graphics object used for rendering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tree.getRoot() != null) {
            calculateNodePositions(tree.getRoot(), getWidth() / 2, 50, getWidth() / 4);
            drawTree(g, tree.getRoot());
        }
    }

    /**
     * Recursively draws the AVL tree.
     * @param g The Graphics object used for rendering.
     * @param node The current node being drawn.
     */
    private void drawTree(Graphics g, AVLNode node) {
        if (node == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        Point position = getAnimatedPosition(node);

        // Draw edges
        if (node.left != null) {
            Point leftPosition = getAnimatedPosition(node.left);
            g.setColor(new Color(100, 150, 255));
            g.drawLine(position.x, position.y, leftPosition.x, leftPosition.y);
        }
        if (node.right != null) {
            Point rightPosition = getAnimatedPosition(node.right);
            g.setColor(new Color(100, 150, 255));
            g.drawLine(position.x, position.y, rightPosition.x, rightPosition.y);
        }

        // Draw node
        g2d.setColor(new Color(255, 100, 100)); // Node background color
        g2d.fillOval(position.x - 20, position.y - 20, 40, 40);
        g2d.setColor(Color.WHITE); // Border color
        g2d.drawOval(position.x - 20, position.y - 20, 40, 40);

        // Get font metrics for alignment
        FontMetrics fm = g2d.getFontMetrics();

        // Draw key
        String keyStr = Integer.toString(node.key);
        g2d.setColor(Color.WHITE); // Set text color for key
        g2d.drawString(keyStr, position.x - fm.stringWidth(keyStr) / 2, position.y + fm.getAscent() / 2 - 2);

        // Create balance factor string
        String balanceStr = "BF: " + tree.getBalanceFactor(node);

        // Set text color
        g2d.setColor(new Color(201, 100, 255)); // Set the same color for both label and value

        // Draw the balance factor text centered
        g2d.drawString(balanceStr, position.x - fm.stringWidth(balanceStr) / 2, position.y + fm.getAscent() + 30);

        // Recursively draw children
        drawTree(g, node.left);
        drawTree(g, node.right);
    }

    /**
     * Retrieves the animated position of a node during tree rotations.
     * @param node The AVL node.
     * @return The position of the node as a Point object.
     */
    private Point getAnimatedPosition(AVLNode node) {
        if (animationProgress < 1f && startPositions.containsKey(node) && endPositions.containsKey(node)) {
            Point start = startPositions.get(node);
            Point end = endPositions.get(node);
            int x = (int) (start.x + (end.x - start.x) * animationProgress);
            int y = (int) (start.y + (end.y - start.y) * animationProgress);
            return new Point(x, y);
        }
        return nodePositions.get(node);
    }

    /**
     * Calculates and updates the positions of nodes for visualization.
     * @param node The current node.
     * @param x The x-coordinate for positioning.
     * @param y The y-coordinate for positioning.
     * @param xOffset The horizontal offset for child nodes.
     */
    private void calculateNodePositions(AVLNode node, int x, int y, int xOffset) {
        if (node == null) return;
        nodePositions.put(node, new Point(x, y));
        if (node.left != null) calculateNodePositions(node.left, x - xOffset, y + 70, xOffset / 2);
        if (node.right != null) calculateNodePositions(node.right, x + xOffset, y + 70, xOffset / 2);
    }

    /**
     * Animates rotations in the AVL tree.
     // * @param node The node around which the rotation occurs.
     * @param step The step description for logging.
     */
    private void animateRotation(String step) {
        startPositions.clear();
        endPositions.clear();

        // Save current positions as start positions
        startPositions.putAll(nodePositions);

        // Calculate new positions after rotation
        calculateNodePositions(tree.getRoot(), getWidth() / 2, 50, getWidth() / 4);

        // Save new positions as end positions
        endPositions.putAll(nodePositions);

        // Add step to the text area and start animation
        stepsArea.append(step + "\n");
        animationProgress = 0f;
        animationTimer.start();
    }
}
