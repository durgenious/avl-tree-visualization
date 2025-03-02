public interface AVLTreeInterface {

    /**
     * Returns the root of the AVL tree.
     *
     * @return The root node.
     */
    AVLNode getRoot();

    /**
     * Returns the balance factor of a node.
     *
     * @param node The node to check.
     * @return The balance factor of the node.
     */
    int getBalanceFactor(AVLNode node);

    /**
     * Resets the AVL tree, removing all nodes.
     */
    void reset();

    /**
     * Inserts a key into the AVL tree and balances the tree if necessary.
     *
     * @param key The key to insert.
     * @return A string describing the insertion and rotations performed.
     */
    String insert(int key);

    /**
     * Performs a preorder traversal of the tree.
     *
     * @return A string representation of the preorder traversal.
     */
    String preorder();

    /**
     * Performs an inorder traversal of the AVL tree.
     *
     * @return A string representation of the inorder traversal.
     */
    String inorder();

    /**
     * Performs a postorder traversal of the AVL tree.
     *
     * @return A string representation of the postorder traversal.
     */
    String postorder();
}
