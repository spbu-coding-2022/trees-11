import abstractTree.BalanceTree

const val RED = false
const val BLACK = true

open class RBTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value> {
    protected class RBNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        var color: Boolean = RED
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
            val parent = node.parent as RBNode<Key, Value>?
            val brother = getSibling(node) ?: error("remove error: brother must exist")

            //balancing when a node is the left child of its parent
            if (node == parent?.left) {

                //if the parent color is red, the brother's color must be black
                if (parent.color == RED) {

                    //case when brother has a red child
                    if (((brother.left as RBNode<Key, Value>?)?.color == RED) ||
                        ((brother.right as RBNode<Key, Value>?)?.color == RED)) {
                        parent.color = BLACK
                        if ((brother.left as RBNode<Key, Value>?)?.color == RED) {
                            rotation(brother, RotationType.RIGHT)
                        } else {
                            brother.color = RED
                            (brother.right as RBNode<Key, Value>?)?.color = BLACK
                        }
                        node = rotation(parent, RotationType.LEFT) as RBNode<Key, Value>?
                    } else {
                        brother.swapColor()
                        parent.swapColor()
                    }
                    break
                }

                else if (brother.color == RED) {
                    //brother's left child must exist and his color must be black
                    var brotherLeftChild =
                        brother.left as RBNode<Key, Value>? ?: error("remove error: brother's left child must exist")

                    if (((brotherLeftChild.left as RBNode<Key, Value>?)?.color == RED) ||
                        ((brotherLeftChild.right as RBNode<Key, Value>?)?.color == RED)) {
                        if ((brotherLeftChild.left as RBNode<Key, Value>?)?.color == RED) {
                            brotherLeftChild.swapColor()
                            (brotherLeftChild.left as RBNode<Key, Value>?)?.swapColor()
                            brotherLeftChild = rotation(brotherLeftChild, RotationType.RIGHT) as RBNode<Key, Value>
                        }
                        (brotherLeftChild.right as RBNode<Key, Value>?)?.swapColor()
                        rotation(brother, RotationType.RIGHT)
                    }

                    else {
                        brother.swapColor()
                        brotherLeftChild.swapColor()
                    }
                    rotation(parent, RotationType.LEFT) as RBNode<Key, Value>?
                    break
                }

                //if brother's color is black
                else {
                    if (((brother.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) &&
                        ((brother.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK)) {
                        brother.color = RED
                        node = parent
                    }

                    else {
                        if ((brother.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK) {
                            (brother.left as RBNode<Key, Value>?)?.color = BLACK
                            rotation(brother, RotationType.RIGHT)
                        }
                        else {
                            (brother.right as RBNode<Key, Value>?)?.color = BLACK
                        }
                        rotation(parent, RotationType.LEFT) as RBNode<Key, Value>?
                        break
                    }
                }
            }

            //balancing when a node is the right child of its parent
            else {

                //if the parent color is red, the brother's color must be black
                if (parent?.color == RED) {

                    //case when brother has a red child
                    if (((brother.left as RBNode<Key, Value>?)?.color == RED) ||
                        ((brother.right as RBNode<Key, Value>?)?.color == RED)) {
                        parent.color = BLACK
                        if ((brother.right as RBNode<Key, Value>?)?.color == RED) {
                            rotation(brother, RotationType.LEFT)
                        } else {
                            brother.color = RED
                            (brother.left as RBNode<Key, Value>?)?.color = BLACK
                        }
                        node = rotation(parent, RotationType.RIGHT) as RBNode<Key, Value>?
                    }

                    else {
                        brother.swapColor()
                        parent.swapColor()
                    }
                    break
                }

                else if (brother.color == RED) {
                    //brother's right child must exist and his color must be black
                    var brotherRightChild =
                        brother.right as RBNode<Key, Value>? ?: error("remove error: brother's right child must exist")

                    if (((brotherRightChild.left as RBNode<Key, Value>?)?.color == RED) ||
                        ((brotherRightChild.right as RBNode<Key, Value>?)?.color == RED)
                    ) {
                        if ((brotherRightChild.right as RBNode<Key, Value>?)?.color == RED) {
                            brotherRightChild.swapColor()
                            (brotherRightChild.right as RBNode<Key, Value>?)?.swapColor()
                            brotherRightChild = rotation(brotherRightChild, RotationType.LEFT) as RBNode<Key, Value>
                        }
                        (brotherRightChild.left as RBNode<Key, Value>?)?.swapColor()
                        rotation(brother, RotationType.LEFT)
                    }

                    else {
                        brother.swapColor()
                        brotherRightChild.swapColor()
                    }
                    rotation(parent, RotationType.RIGHT) as RBNode<Key, Value>?
                    break
                }

                //if brother's color is black
                else {
                    if (((brother.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) &&
                        ((brother.right == null) || (brother.right as RBNode<Key, Value>?)?.color == BLACK)) {
                        brother.color = RED
                        node = parent
                    } else {
                        if ((brother.left == null) || (brother.left as RBNode<Key, Value>?)?.color == BLACK) {
                            (brother.right as RBNode<Key, Value>?)?.color = BLACK
                            rotation(brother, RotationType.LEFT)
                        }
                        else {
                            (brother.left as RBNode<Key, Value>?)?.color = BLACK
                        }
                        rotation(parent, RotationType.RIGHT) as RBNode<Key, Value>?
                        break
                    }
                }
            }
        }
    }
}