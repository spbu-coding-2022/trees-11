class AVLTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class AVLNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: AVLNode<Key, Value>? = null,
        left: AVLNode<Key, Value>? = null,
        right: AVLNode<Key, Value>? = null,
        var height: UByte = 0U
    ) : BinNode<Key, Value>(key, value, parent, left, right)

    private fun createNode(key: Key, value: Value) {
        val node = AVLNode<key, value>
        return node
    }

    private fun rotationAVL(parentKey: Key, type: RotationType): BinNode<Key, Value>? {
        val node = rotation(parentKey, type)

        node.height = max(node.left.height, node.right.height) + 1
        node.parent.height = max(node.parent.left.height, node.parent.right.height) + 1

        return node
    }

    override fun insert(key: Key, value: Value) {
        val node = createNode(key, value)

        if (key < node.key) node.left = insert(node.left, key)
        else if (key > node.key) node.right = insert(node.right, key)
        else return node

        node.height = 1 + (max(node.left.height, node.left.right))

        val heightDifference = node.left.height - node.right.height

        if (heightDifference > 1 and key < node.left.key) {
            return rotationAVL(parent.key, RotationType.right)
        }
        if (heightDifference < -1 and key > node.right.key) {
            return rotationAVL(parent.key, RotationType.left)
        }
        if (heightDifference > 1 and key > node.left.key) {
            node.left = rotationAVL(parent.key, RotationType.left)
            return rotationAVL(parent.key, RotationType.right)
        }
        if (heightDifference < -1 and key < node.right.key) {
            node.right = rotationAVL(parent.key, RotationType.right)
            return rotationAVL(parent.key, RotationType.left)
        }

        return node
    }

    override fun remove(key: Key) {
        TODO("Not yet implemented")
    }
}
