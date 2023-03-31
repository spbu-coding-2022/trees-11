class RBTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class RBNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: RBNode<Key, Value>? = null,
        left: RBNode<Key, Value>? = null,
        right: RBNode<Key, Value>? = null,
        var Black: Boolean = false
    ) : Node<Key, Value>(key, value, parent, left, right) {
        fun swapColor() {
            Black = !Black
        }
    }


    override fun insert(key: Key, value: Value) {
        if (rootNode == null)
            rootNode = RBNode(key, value)
    }

    protected fun getGrandparent(key: Key): RBNode<Key, Value>? {
        val parent = getParent(key) as RBNode<Key, Value>?
        parent?.let { it -> it.parent?.let { return it as RBNode<Key, Value>} }
        return null
    }

    protected fun getUncle(key: Key): RBNode<Key, Value>? {
        val grandparent = getGrandparent(key)
        val parent = getParent(key) as RBNode<Key, Value>
        grandparent?.let{
            if (it.left == parent) return it.right as RBNode<Key, Value>
            else if (it.right == parent) return it.left as RBNode<Key, Value>
        }
        return null
    }

    override fun rebalancing() {
        TODO("Not yet implemented")
    }

    override fun remove(key: Key) {
        TODO("Not yet implemented")
    }
}
