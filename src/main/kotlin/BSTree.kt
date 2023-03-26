open class BSTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    open override fun insert(key: Key, value: Value) {
        if (rootNode == null)
            rootNode = Node(key, value)
        else {
            val parent = getParent(key)
            if (parent != null) {
                if (parent < key)
                    if (parent.right == null)
                        parent.right = Node(key, value)
                    else parent.right?.value = value ?: error("unexpected null")
                else
                    if (parent.left == null)
                        parent.left = Node(key, value)
                    else (parent.left)?.value = value ?: error("unexpected null")
            }
        }
    }

    open override fun remove(key: Key) {
        val node = getNode(key)
        if (node != null)
            removeNode(node)
    }
    protected open fun removeNode(node: Node<Key, Value>) {
        if ((node.left == null) && (node.right == null)) {
            if (node.parent == null)
                rootNode = null
            else if (node == node.parent!!.left)
                node.parent!!.left = null
            else
                node.parent!!.right = null
        }
        else if (node.left == null)
            transplant(node, node.right!!)
        else if (node.right == null)
            transplant(node, node.left!!)
        else{
            val nextNode = nextElement(node)
            if (nextNode != null) {
                if (nextNode.right != null)
                    transplant(nextNode, nextNode.right!!)
                nextNode.right = node.right
                nextNode.left = node.left
                nextNode.right!!.parent = nextNode
                nextNode.left!!.parent = nextNode
                transplant(node, nextNode)
            }
        }
    }

    protected open fun transplant(node1: Node<Key, Value>, node2: Node<Key, Value>) {
        if (node1.parent == null)
            rootNode = node2
        else if (node1 == (node1.parent)!!.left) {
            (node1.parent)!!.left = node2
        } else {
            (node1.parent)!!.right = node2
        }
        node2.parent = node1.parent
    }

    protected open fun nextElement(node: Node<Key, Value>): Node<Key, Value>?{
        if (node.right == null)
            return null
        return minElement(node.right!!)
    }

    protected fun minElement(node: Node<Key, Value>): Node<Key, Value> {
        var node1: Node<Key, Value> = node
        while (node1.left != null) {
            node1 = node1.left!!
        }
        return node1
    }

    protected fun maxElement(node: Node<Key, Value>): Node<Key, Value> {
        var node1: Node<Key, Value> = node
        while (node1.right != null) {
            node1 = node1.right!!
        }
        return node1
    }
}
