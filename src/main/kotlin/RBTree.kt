import abstractTree.BalanceTree

const val RED = false
const val BLACK = true

open class RBTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value> {
    protected class RBNode<Key : Comparable<Key>, Value>(
        key: Key, value: Value, var color: Boolean = RED
    ) : BinNode<Key, Value>(key, value) {
        fun swapColor() {
            color = !color
        }
    }

    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
        val node = insertService(RBNode(key, value))
        if (node != null) balancingInsert(node as RBNode)
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

            if (parent == null) rootNode = null

            //when the color of the node is red, just delete it
            else if (removeNode.color == RED) replaceNodeParent(removeNode, null)

            //when the color of the node without children is black,
            //the tree needs to be balanced
            else {
                balancingRemove(removeNode)
                replaceNodeParent(removeNode, null)
            }

        }
        //delete black node with one red child
        else if (removeNode.left == null) {
            replaceNodeParent(removeNode, removeNode.right ?: error("remove error: unexpected null"))
            (removeNode.right as RBNode).swapColor()
        } else {
            replaceNodeParent(removeNode, removeNode.left ?: error("remove error: unexpected null"))
            (removeNode.left as RBNode).swapColor()
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

    private fun balancingInsert(node: RBNode<Key, Value>) {
        val parent = getParent(node.key) as RBNode?

        //root color should always be black
        if (parent == null) (rootNode as RBNode?)?.color = BLACK
        else if (parent.color == BLACK) return
        else {
            val uncle = getUncle(node)
            val grandparent = getGrandparent(node) ?: error("balancing error")

            if (uncle?.color == RED) {
                parent.swapColor()
                uncle.swapColor()
                grandparent.swapColor()
                balancingInsert(grandparent)
            } else {
                if (grandparent.left == parent) {
                    if (parent.right == node) rotation(parent, RotationType.LEFT)
                    val newNode = rotation(grandparent, RotationType.RIGHT) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                } else {
                    if (parent.left == node) rotation(parent, RotationType.RIGHT)
                    val newNode = rotation(grandparent, RotationType.LEFT) as RBNode?
                    newNode?.swapColor() ?: error("balancing error")
                }
                grandparent.swapColor()
            }
        }
    }


    protected fun balancingRemove(removeNode: RBNode<Key, Value>?) {
        var node = removeNode

        while ((node != rootNode) && (node?.color == BLACK)) {
            var brother = getSibling(node)
            //balancing when a node is the left child of its parent
            if (node == node.parent?.left) {
                if (brother?.color == RED) {
                    (node.parent as RBNode<Key, Value>?)?.swapColor()
                    brother.swapColor()
                    rotation(node.parent, RotationType.LEFT)
                    brother = (node.parent as RBNode<Key, Value>?)?.right as RBNode<Key, Value>?
                }
                if (((brother?.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) && ((brother?.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK)) {
                    brother?.color = RED
                    node = node.parent as RBNode<Key, Value>
                } else {
                    if ((brother.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK) {
                        (brother.left as RBNode<Key, Value>?)?.color = BLACK
                        brother.color = RED
                        rotation(brother, RotationType.RIGHT)
                        brother = node.parent?.right as RBNode<Key, Value>?
                    }
                    brother?.color = (node.parent as RBNode<Key, Value>).color
                    (node.parent as RBNode<Key, Value>).color = BLACK
                    (brother?.right as RBNode<Key, Value>).color = BLACK
                    rotation(node.parent as RBNode<Key, Value>?, RotationType.LEFT)
                    node = rootNode as RBNode<Key, Value>
                }
            }
            //balancing when a node is the right child of its parent
            else {
                if (brother?.color == RED) {
                    (node.parent as RBNode<Key, Value>?)?.swapColor()
                    brother.swapColor()
                    rotation(node.parent, RotationType.RIGHT)
                    brother = (node.parent as RBNode<Key, Value>?)?.left as RBNode<Key, Value>?
                }
                if (((brother?.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) && ((brother?.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK)) {
                    brother?.color = RED
                    node = node.parent as RBNode<Key, Value>
                } else {
                    if ((brother.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) {
                        (brother.right as RBNode<Key, Value>?)?.color = BLACK
                        brother.color = RED
                        rotation(brother, RotationType.LEFT)
                        brother = node.parent?.left as RBNode<Key, Value>?
                    }
                    brother?.color = (node.parent as RBNode<Key, Value>).color
                    (node.parent as RBNode<Key, Value>).color = BLACK
                    (brother?.left as RBNode<Key, Value>).color = BLACK
                    rotation(node.parent as RBNode<Key, Value>?, RotationType.RIGHT)
                    node = rootNode as RBNode<Key, Value>
                }
            }
            node.color = BLACK
        }
    }
}