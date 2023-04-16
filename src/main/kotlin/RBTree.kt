const val RED = false
const val BLACK = true

class RBTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class RBNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: RBNode<Key, Value>? = null,
        left: RBNode<Key, Value>? = null,
        right: RBNode<Key, Value>? = null,
        var color: Boolean = RED
    ) : BinNode<Key, Value>(key, value, parent, left, right) {
        fun swapColor() {
            color = if (color == BLACK) RED else BLACK
        }
    }


    override fun insert(key: Key, value: Value) {
        val node = insertService(BinNode(key, value)) as RBNode?
        rebalancingInsert(node)
    }

    override fun remove(key: Key) {
        val node = getNode(key) as RBNode? ?: return
        val removeNode: RBNode<Key, Value>
        //find removeNode
        if ((node.left != null) && (node.right != null)) {
            val nextNode = nextElement(node) as RBNode? ?: error("remove is not possible: unexpected null")
            node.key = nextNode.key
            node.value = nextNode.value
            removeNode = nextNode
        } else removeNode = node

        //delete node without child
        if ((removeNode.left == null) && (removeNode.right == null)) {
            val parent: BinNode<Key, Value>? = removeNode.parent
            if (parent == null)
                rootNode = null
            else if (removeNode.color == RED)
                replaceNodeParent(removeNode, null)

            //special case for black node without child
            else {
                rebalancingRemove(removeNode)
                replaceNodeParent(removeNode, null)
            }

        }
        //delete black node with one red child
        else if (node.left == null) {
            replaceNodeParent(node, node.right ?: error("remove error: unexpected null"))
            (node.right as RBNode).swapColor()
        } else {
            replaceNodeParent(node, node.left ?: error("remove error: unexpected null"))
            (node.left as RBNode).swapColor()
        }
    }

    protected fun getGrandparent(node: RBNode<Key, Value>?): RBNode<Key, Value>? {
        val parent = node?.parent
        parent?.let { it -> it.parent?.let { return it as RBNode<Key, Value> } }
        return null
    }

    protected fun getSibling(node: RBNode<Key, Value>?): RBNode<Key, Value>? {
        val parent = node?.parent ?: return null
        return if (parent.left == node) parent.right as RBNode<Key, Value>?
        else parent.left as RBNode<Key, Value>?
    }

    protected fun getUncle(node: RBNode<Key, Value>?): RBNode<Key, Value>? {
        val parent = node?.parent ?: return null
        return getSibling(parent as RBNode?)
    }

    private fun rebalancingInsert(node: RBNode<Key, Value>?) {
        if (node == null) error("can't insert node")
        val parent = getParent(node.key) as RBNode?
        if (parent == null) (rootNode as RBNode?)?.color = BLACK
        else if (parent.color == BLACK) return
        else {
            val uncle = getUncle(node) ?: error("balancing error")
            val grandparent = getGrandparent(node) ?: error("balancing error")
            if (uncle.color == RED) {
                parent.swapColor()
                uncle.swapColor()
                grandparent.swapColor()
                rebalancingInsert(grandparent)
            } else {
                if (grandparent.left == parent) {
                    if (parent.right == node) rotation(parent, RotationType.Left)
                    val newNode = rotation(grandparent, RotationType.Right) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                } else {
                    if (parent.left == node) rotation(parent, RotationType.Right)
                    val newNode = rotation(grandparent, RotationType.Left) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                }
                grandparent.swapColor()
            }
        }
    }


    protected fun rebalancingRemove(removeNode: RBNode<Key, Value>?) {
        var node = removeNode

        while ((node != rootNode) && (node?.color == BLACK)) {
            var brother = getSibling(node)
            //balancing when a node is the left child of its parent
            if (node == node.parent?.left) {
                if (brother?.color == RED) {
                    (node.parent as RBNode<Key, Value>?)?.swapColor()
                    brother.swapColor()
                    rotation(node.parent, RotationType.Left)
                    brother = (node.parent as RBNode<Key, Value>?)?.right as RBNode<Key, Value>?
                }
                if ((((brother?.left as RBNode<Key, Value>?) == null) || (brother?.left as RBNode<Key, Value>?)?.color == BLACK) &&
                    (((brother?.right as RBNode<Key, Value>?) == null) || (brother?.right as RBNode<Key, Value>?)?.color == BLACK)
                ) {
                    brother?.color = RED
                    node = node.parent as RBNode<Key, Value>
                } else {
                    if ((brother?.right as RBNode<Key, Value>?)?.color == BLACK) {
                        (brother?.left as RBNode<Key, Value>?)?.color = BLACK
                        brother?.color = RED
                        rotation(brother, RotationType.Right)
                        brother = node.parent?.right as RBNode<Key, Value>?
                    }
                    brother?.color = (node.parent as RBNode<Key, Value>).color
                    (node.parent as RBNode<Key, Value>).color = BLACK
                    (brother?.right as RBNode<Key, Value>).color = BLACK
                    rotation(node.parent as RBNode<Key, Value>?, RotationType.Left)
                    node = rootNode as RBNode<Key, Value>
                }
            }
            //balancing when a node is the right child of its parent
            else {
                if (brother?.color == RED) {
                    (node.parent as RBNode<Key, Value>?)?.swapColor()
                    brother.swapColor()
                    rotation(node.parent, RotationType.Right)
                    brother = (node.parent as RBNode<Key, Value>?)?.left as RBNode<Key, Value>?
                }
                if ((((brother?.left as RBNode<Key, Value>?) == null) || (brother?.left as RBNode<Key, Value>?)?.color == BLACK) &&
                    (((brother?.right as RBNode<Key, Value>?) == null) || (brother?.right as RBNode<Key, Value>?)?.color == BLACK)
                ) {
                    brother?.color = RED
                    node = node.parent as RBNode<Key, Value>
                } else {
                    if ((brother?.left as RBNode<Key, Value>?)?.color == BLACK) {
                        (brother?.right as RBNode<Key, Value>?)?.color = BLACK
                        brother?.color = RED
                        rotation(brother, RotationType.Left)
                        brother = node.parent?.left as RBNode<Key, Value>?
                    }
                    brother?.color = (node.parent as RBNode<Key, Value>).color
                    (node.parent as RBNode<Key, Value>).color = BLACK
                    (brother?.left as RBNode<Key, Value>).color = BLACK
                    rotation(node.parent as RBNode<Key, Value>?, RotationType.Right)
                    node = rootNode as RBNode<Key, Value>
                }
            }
            node.color = BLACK
        }
    }
}