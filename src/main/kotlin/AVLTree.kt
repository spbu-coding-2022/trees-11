class AVLTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class AVLNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: AVLNode<Key, Value>? = null,
        left: AVLNode<Key, Value>? = null,
        right: AVLNode<Key, Value>? = null,
        var height: UByte = 0U
    ) : BinNode<Key, Value>(key, value, parent, left, right)

    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
        if (rootNode != null) {
            if ((rootNode as AVLNode).height != 255.toUByte()) {
                insertService(AVLNode(key, value))
            }
        } else
            rootNode = AVLNode(key, value)
        balance(rootNode as AVLNode)
    }


    private fun searchNode(key: Key, node: AVLNode<Key, Value>?): AVLNode<Key, Value>? {
        if (node == null || key == node.key) {
            return node
        }
        return if (key < node.key) {
            searchNode(key, node.left as AVLNode<Key, Value>?)
        } else {
            searchNode(key, node.right as AVLNode<Key, Value>?)
        }
    }
    private fun transplant(node: AVLNode<Key, Value>, newNode: AVLNode<Key, Value>) {
        if (node.parent == null) {
            rootNode = newNode
        } else if (node == node.parent?.left) {
            node.parent?.left = newNode
        } else {
            node.parent?.right = newNode
        }
        newNode.parent = node.parent
    }

    private fun minNode(node: AVLNode<Key, Value>): AVLNode<Key, Value> {
        var currentNode = node
        while (currentNode.left != null) {
            currentNode = currentNode.left as AVLNode<Key, Value>
        }
        return currentNode
    }

    override fun remove(key: Key) {
        val node = searchNode(key, rootNode as AVLNode<Key, Value>) ?: return
        when {
            node.left == null -> transplant(node, node.right as AVLNode<Key, Value>)
            node.right == null -> transplant(node, node.left as AVLNode<Key, Value>)
            else -> {
                val successor = minNode(node.right!! as AVLNode<Key, Value>)
                if (successor.parent != node) {
                    transplant(successor, successor.right as AVLNode<Key, Value>)
                    successor.right = node.right
                    successor.right?.parent = successor
                }
                transplant(node, successor)
                successor.left = node.left
                successor.left?.parent = successor
            }
        }
        balance(node.parent as AVLNode<Key, Value>)
    }
    private fun balance(node: AVLNode<Key, Value>) {
        if (node.left != null && node.right != null) {
            if ((node.left as AVLNode).height > (node.right as AVLNode).height + 1.toUByte()) {
                if ((node.left!!.left as AVLNode).height >= (node.left!!.right as AVLNode).height) {
                    rotateRight(node)
                } else {
                    rotateLeft(node.left!! as AVLNode<Key, Value>)
                    rotateRight(node)
                }
            } else if ((node.right as AVLNode).height > (node.left as AVLNode).height + 1.toUByte()) {
                if ((node.right!!.right as AVLNode).height >= (node.right!!.left as AVLNode).height) {
                    rotateLeft(node)
                } else {
                    rotateRight(node.right!! as AVLNode<Key, Value>)
                    rotateLeft(node)
                }
            }
            updateHeight(node)
            if (node.parent != null) {
                balance(node.parent!! as AVLNode<Key, Value>)
            }
        }
    }
    private fun updateHeight(node: AVLNode<Key, Value>) {
        val left = node.left?.let { (it as AVLNode).height } ?: 0U
        val right = node.right?.let { (it as AVLNode).height } ?: 0U
        node.height = (maxOf(left, right) + 1.toUByte()).toUByte()
    }
    private fun rotateLeft(node: AVLNode<Key, Value>) {
        val child = node.right!!
        child.parent = node.parent
        if (node.parent == null) {
            rootNode = child
        } else if (node.parent!!.left == node) {
            node.parent!!.left = child
        } else {
            node.parent!!.right = child
        }
        node.right = child.left
        child.left?.parent = node
        node.parent = child
        child.left = node
        updateHeight(node)
        updateHeight(child as AVLNode<Key, Value>)
    }
    private fun rotateRight(node: AVLNode<Key, Value>) {
        val child = node.left!!
        child.parent = node.parent
        if (node.parent == null) {
            rootNode = child
        } else if (node.parent!!.left == node) {
            node.parent!!.left = child
        } else {
            node.parent!!.right = child
        }
        node.left = child.right
        child.right?.parent = node
        node.parent = child
        child.right = node
        updateHeight(node)
        updateHeight(child as AVLNode<Key, Value>)
    }
}