class AVLTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class AVLNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: AVLNode<Key, Value>? = null,
        left: AVLNode<Key, Value>? = null,
        right: AVLNode<Key, Value>? = null,
        var height: UByte = 0U
    ) : Node<Key, Value>(key, value, parent, left, right)

    override fun insert(key: Key, value: Value) {
        if (rootNode != null) {
            if ((rootNode as AVLNode).height != 255.toUByte()) {
                val parent = getParent(key)
                if (parent != null) {
                    if (parent < key)
                        parent.right = AVLNode(key, value)
                    else
                        parent.left = AVLNode(key, value)
                }
            }
        } else
            rootNode = AVLNode(key, value)
    }

    override fun remove(key: Key) {
        TODO("jfaj;l")
    }

    override fun leftRotation() {
        TODO("Not yet implemented")
    }

    override fun rightRotation() {
        TODO("Not yet implemented")
    }

    override fun rebalancing() {
        TODO("Not yet implemented")
    }
}