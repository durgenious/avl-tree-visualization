import javax.swing.JFrame;

/**
 * A demo class to visualize an AVL tree using a graphical user interface
 */
public class AVLTreeDemo {
    /**
     * The entry point of the program.
     * It creates an AVL tree, inserts keys, and displays the tree in a GUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Create an empty AVL Tree
        AVLTree tree = new AVLTree();

        /**
         * Array of keys to be inserted into the AVL Tree.
         * The tree will automatically balance itself as these keys are inserted.
         * Uncomment an alternative array for a different input set.
         */

//        int[] keys = {10, 20, 30, 40, 50, 25};
//        int[] keys = {21, 26, 30, 9, 4, 14, 28, 18, 15, 10, 2, 3, 7};

        int[] keys = {12, 22, 91, 13, 16, 14, 15, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30};

        // Create a JFrame window for visualization
        JFrame frame = new JFrame("AVL Tree Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close program when window is closed
        frame.setSize(1400, 800); // Set the window size

        // Create and add the AVLTreeVisualizer component to visualize the tree
        // This component will be responsible for displaying the AVL tree structure
        AVLTreeVisualizer gui = new AVLTreeVisualizer(tree, keys);
        frame.add(gui);

        // Make the frame visible
        frame.setVisible(true);
    }
}
