class RBTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class RBNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: RBNode<Key, Value>? = null,
        left: RBNode<Key, Value>? = null,
        right: RBNode<Key, Value>? = null,
        var Black: Boolean = false
    ) : BinNode<Key, Value>(key, value, parent, left, right) {
        fun swapColor() {
            Black = !Black
        }
    }

    override fun insert(key: Key, value: Value) {
        if (rootNode == null)
            rootNode = RBNode(key, value)
        TODO("add balancing")
    }

    override fun rebalancing() {
        TODO("Not yet implemented")
    }

    override fun leftRotation() {
        TODO("Not yet implemented")
    }

    override fun rightRotation() {
        TODO("Not yet implemented")
    }

    override fun remove(key: Key) {
        TODO("Not yet implemented")
    }
}
