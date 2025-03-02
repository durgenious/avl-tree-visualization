/**
 * This class implements an AVL Tree, a self-balancing binary search tree.
 * It ensures that the tree remains balanced after insertion operations
 * by performing necessary rotations.
 */
public class AVLTree implements AVLTreeInterface {
    private AVLNode root;

    /**
     * Gets the height of a given node.
     *
     * @param node The node whose height is to be determined.
     * @return The height of the node, or 0 if the node is null.
     */
    private int getHeight(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    /**
     * Updates the height of a given node based on its children's heights.
     *
     * @param node The node whose height needs updating.
     */
    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    /**
     * Returns the root of the AVL tree.
     *
     * @return The root node.
     */
    @Override
    public AVLNode getRoot() {
        return root;
    }

    /**
     * Calculates the balance factor of a given node.
     *
     * @param node The node for which the balance factor is calculated.
     * @return The difference between the heights of the left and right subtrees.
     */
    @Override
    public int getBalanceFactor(AVLNode node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Resets the AVL tree, removing all nodes.
     */
    @Override
    public void reset() {
        root = null;
    }

    /**
     * Performs a right rotation on the given node.
     *
     * @param y The node to be rotated.
     * @return The new root of the rotated subtree.
     */
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Performs a left rotation on the given node.
     *
     * @param x The node to be rotated.
     * @return The new root of the rotated subtree.
     */
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Inserts a key into the AVL tree and balances the tree if necessary.
     *
     * @param key The key to insert.
     * @return A string describing the insertion and rotations performed.
     */
    @Override
    public String insert(int key) {
        StringBuilder steps = new StringBuilder(" Insert " + key + ": ");
        root = insert(root, key, steps);
        if (steps.toString().endsWith(" Insert " + key + ": ")) {
            steps.append("No rotation.");
        }
        return steps.toString();
    }

    /**
     * Recursive function to insert a key into the AVL tree and update balance.
     *
     * @param node  the current node being checked
     * @param key   the key to be inserted
     * @param steps a StringBuilder to track insertion steps
     * @return the balanced node after insertion
     */
    private AVLNode insert(AVLNode node, int key, StringBuilder steps) {
        if (node == null) {
            return new AVLNode(key);
        }

        if (key < node.key) node.left = insert(node.left, key, steps);
        else if (key > node.key) node.right = insert(node.right, key, steps);
        else return node; // Duplicate keys not allowed

        updateHeight(node);

        int balance = getBalanceFactor(node);

        // Left-Left Case (LL)
        if (balance > 1 && key < node.left.key) {
            steps.append("Right Rotation on Node: ").append(node.key);
            return rightRotate(node);
        }

        // Right-Right Case (RR)
        if (balance < -1 && key > node.right.key) {
            steps.append("Left Rotation on Node: ").append(node.key);
            return leftRotate(node);
        }

        // Left-Right Case (LR)
        if (balance > 1 && key > node.left.key) {
            steps.append("Left Rotation on Node: ").append(node.left.key)
                    .append(", Right Rotation on Node: ").append(node.key);
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left Case (RL)
        if (balance < -1 && key < node.right.key) {
            steps.append("Right Rotation on Node: ").append(node.right.key)
                    .append(", Left Rotation on Node: ").append(node.key);
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Performs a preorder traversal of the tree.
     *
     * @return A string representation of the preorder traversal.
     */
    @Override
    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorderHelper(root, sb);
        return sb.toString();
    }

    /**
     * Helper method for preorder traversal.
     *
     * @param node the current node being visited
     * @param sb   a StringBuilder to accumulate traversal results
     */
    private void preorderHelper(AVLNode node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.key).append(" ");
        preorderHelper(node.left, sb);
        preorderHelper(node.right, sb);
    }

    /**
     * Performs an inorder traversal of the AVL tree.
     *
     * @return a string representation of the inorder traversal
     */
    @Override
    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorderHelper(root, sb);
        return sb.toString();
    }

    /**
     * Helper method for inorder traversal.
     *
     * @param node the current node being visited
     * @param sb   a StringBuilder to accumulate traversal results
     */
    private void inorderHelper(AVLNode node, StringBuilder sb) {
        if (node == null) return;
        inorderHelper(node.left, sb);
        sb.append(node.key).append(" ");
        inorderHelper(node.right, sb);
    }

    /**
     * Performs a postorder traversal of the AVL tree.
     *
     * @return a string representation of the postorder traversal
     */
    @Override
    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorderHelper(root, sb);
        return sb.toString();
    }

    /**
     * Helper method for postorder traversal.
     *
     * @param node the current node being visited
     * @param sb   a StringBuilder to accumulate traversal results
     */
    private void postorderHelper(AVLNode node, StringBuilder sb) {
        if (node == null) return;
        postorderHelper(node.left, sb);
        postorderHelper(node.right, sb);
        sb.append(node.key).append(" ");
    }
}
