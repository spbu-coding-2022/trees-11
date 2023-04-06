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
        val node = insertService(RBNode(key, value)) as RBNode<Key, Value>
        rebalancing(node)
        TODO("add balancing")
    }

    override fun remove(key: Key) {
        val parent = removeNode(key) as RBNode<Key, Value>?
        TODO("will do soon")
    }

    private tailrec fun rebalancing(node: RBNode<Key, Value>) {
        val parent = getParent(node.key) as RBNode?
        if (parent == null) (rootNode as RBNode?)?.Black = true
        else if (parent.Black) return
        else {
            val uncle = getUncle(node.key) ?: error("balancing error")
            val grandparent = getGrandparent(node.key) ?: error("balancing error")
            if (!uncle.Black) {
                parent.swapColor()
                uncle.swapColor()
                grandparent.swapColor()
                rebalancing(grandparent)
            } else {
                if (grandparent.left == parent) {
                    if (parent.right == node) rotation(parent.key, RotationType.Left)
                    val newNode = rotation(grandparent.key, RotationType.Right) as RBNode?
                    grandparent.swapColor()
                    newNode?.swapColor() ?: error("balancing error")
                } else {
                    if (parent.left == node) rotation(parent.key, RotationType.Right)
                    val newNode = rotation(grandparent.key, RotationType.Left) as RBNode?
                    grandparent.swapColor()
                    newNode?.swapColor() ?: error("balancing error")
                }
            }
        }
    }

    protected fun getGrandparent(key: Key): RBNode<Key, Value>? {
        val parent = getParent(key)
        parent?.let { it -> it.parent?.let { return it as RBNode<Key, Value> } }
        return null
    }

    protected fun getBrother(key: Key): RBNode<Key, Value>? {
        val node = getNode(key)
        val parent = getParent(key) ?: return null
        return if (parent.left == node) parent.right as RBNode<Key, Value>?
        else parent.left as RBNode<Key, Value>?
    }

    protected fun getUncle(key: Key): RBNode<Key, Value>? {
        val parent = getParent(key) ?: return null
        return getBrother(parent.key)
    }
}
