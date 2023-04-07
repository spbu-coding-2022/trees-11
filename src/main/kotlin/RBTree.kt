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
        val node = insertService(BinNode(key, value)) as RBNode?
        rebalancingInsert(node)
    }

    override fun remove(key: Key) {
        val node = getNode(key) as RBNode? ?: error("remove is not possible: there is no node with this key")
        val nextNode = nextElement(node) as RBNode?
        if ((node.left != null) && (node.right != null)) {
            if ((nextNode?.Black ?: error("remove is not possible: unexpected null")) != node.Black) {
                node.swapColor()
                nextNode.swapColor()
            }
        }
        val parent = removeNode(key) as RBNode<Key, Value>?
        rebalancingRemove(parent, node.Black)
    }

    private tailrec fun rebalancingInsert(node: RBNode<Key, Value>?) {
        if (node == null) error("can't insert node")
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
                rebalancingInsert(grandparent)
            } else {
                if (grandparent.left == parent) {
                    if (parent.right == node) rotation(parent.key, RotationType.Left)
                    val newNode = rotation(grandparent.key, RotationType.Right) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                } else {
                    if (parent.left == node) rotation(parent.key, RotationType.Right)
                    val newNode = rotation(grandparent.key, RotationType.Left) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                }
                grandparent.swapColor()
            }
        }
    }

    protected fun rebalancingRemove(parent: RBNode<Key, Value>?, black: Boolean) {
        if (parent == null) return
        else if (!black) return
        else {
            if (parent.Black) {
                return
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
