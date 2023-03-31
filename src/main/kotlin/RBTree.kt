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
        val parent = insertNode(key, value) as RBNode<Key, Value>?
        rebalancing(parent)
    }

    override fun remove(key: Key) {
        val parent = removeNode(key) as RBNode<Key, Value>?
        rebalancing(parent)
    }

    private fun rebalancing(parent: RBNode<Key, Value>?) {
        TODO("Not yet implemented")
    }

    protected fun getGrandparent(key: Key): RBNode<Key, Value>? {
        val parent = getParent(key)
        parent?.let { it -> it.parent?.let { return it as RBNode<Key, Value>} }
        return null
    }

    protected fun getUncle(key: Key): RBNode<Key, Value>? {
        val grandparent = getGrandparent(key)
        val parent = getParent(key)
        grandparent?.let{
            if (it.left == parent) return it.right as RBNode<Key, Value>?
            else if (it.right == parent) return it.left as RBNode<Key, Value>?
        }
        return null
    }

    protected fun getBrother(key: Key): RBNode<Key, Value>? {
        val node = getNode(key)
        val parent = getParent(key)
        parent?.let {
            if (parent.left == node) return parent.right as RBNode<Key, Value>?
            else if(parent.right == node) return parent.left as RBNode<Key, Value>?
        }
        return null
    }

}
