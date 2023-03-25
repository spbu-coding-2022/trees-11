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
        removeNode(getNode(key))
    }

    protected open fun removeNode(node: Node<Key, Value>?) {
        if ((node!!.left == null) && (node.right == null)) {
            if (node.parent == null)
                rootNode == null
            else if (node == (node.parent)!!.left)
                (node.parent)!!.left == null
            else
                (node.parent)!!.right == null
        } else if (node.left == null)
            transplant(node, (node.right)!!)
        else if (node.right == null)
            transplant(node, (node.left)!!)
        else {
            TODO("Юля когда-нибудь доделает")
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
}
