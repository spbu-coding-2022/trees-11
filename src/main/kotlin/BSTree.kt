open class BSTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
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

    override fun remove(key: Key) {
        val node: Node<Key, Value>? = getNode(key)
        if (node == null)
            return
        else if ((node.left == null) && (node.right == null)) {
            val parent: Node<Key, Value>? = node.parent
            if (parent == null)
                rootNode = null
            else if (node == parent.left)
                parent.left = null
            else
                parent.right = null
        } else if (node.left == null)
            transplant(node, node.right ?: error("unexpected null"))
        else if (node.right == null)
            transplant(node, node.left ?: error("unexpected null"))
        else {
            val nextNode = nextElement(node)
            if (nextNode != null) {
                nextNode.right?.let {transplant(nextNode, it)}
                nextNode.right = node.right
                nextNode.left = node.left
                nextNode.right?.parent = nextNode
                nextNode.left?.parent = nextNode
                transplant(node, nextNode)
            }
        }
    }

    protected open fun transplant(oldNode: Node<Key, Value>, newNode: Node<Key, Value>) {
        val parent: Node<Key, Value>? = oldNode.parent
        if (parent == null)
            rootNode = newNode
        else if (oldNode == parent.left) {
            parent.left = newNode
        } else {
            parent.right = newNode
        }
        newNode.parent = parent
    }
}
