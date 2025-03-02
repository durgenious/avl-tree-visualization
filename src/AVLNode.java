/**
 * Represents a node in an AVL tree.
 */
public class AVLNode {
    /** The key value stored in the node. */
    int key;

    /** The height of the node in the AVL tree. */
    int height;

    /** Reference to the left child of the node. */
    AVLNode left;

    /** Reference to the right child of the node. */
    AVLNode right;

    /**
     * Constructs an AVLNode with the given key.
     * The height is initialized to 1.
     *
     * @param key The key value to be stored in the node.
     */
    AVLNode(int key) {
        this.key = key;
        this.height = 1;
    }
}
