class AVLTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value> {
    protected class AVLNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        var height: UByte = 0U
    ) : BinNode<Key, Value>(key, value)

    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
        //if the tree is too big, we can't insert something
        rootNode?.let { if ((rootNode as AVLNode<Key, Value>).height == 255.toUByte()) return }

        val node = insertService(AVLNode(key, value)) as AVLNode<Key, Value>?
        balancing(node ?: return)
    }

    override fun remove(key: Key) {
        val removeNode = getNode(key) ?: return

        //special case when the node has two children
        if ((removeNode.right != null) && (removeNode.left != null)) {
            val node = nextElement(removeNode) as AVLNode<Key, Value>

            if (node.parent == removeNode) {
                removeService(removeNode)
                balancing(node)
            } else {
                val nextNodeParent = node.parent as AVLNode<Key, Value>
                removeService(removeNode)
                balancing(nextNodeParent)
            }
        }

        //when the node has zero or one child, just remove it and balance the tree
        else {
            removeService(removeNode)
            if (rootNode != null) balancing(removeNode.parent as AVLNode<Key, Value>)
        }

    }

    //use tail recursion to balance after removing and inserting a node
    private tailrec fun balancing(node: AVLNode<Key, Value>): AVLNode<Key, Value> {
        var currentNode = node
        updateHeight(currentNode)

        //if the balancing factor by module is greater than one,
        //it is necessary to do the balancing
        if (balanceFactor(currentNode) >= 2) {
            if (balanceFactor(currentNode.right as AVLNode<Key, Value>) >= 0) {
                currentNode = rotation(currentNode, RotationType.LEFT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.left as AVLNode<Key, Value>?)
            } else {
                currentNode = rotation(currentNode.right, RotationType.RIGHT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.right as AVLNode<Key, Value>?)
                currentNode = rotation(currentNode.parent, RotationType.LEFT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.left as AVLNode<Key, Value>?)
            }
        } else if (balanceFactor(currentNode) <= -2) {
            if (balanceFactor(currentNode.left as AVLNode<Key, Value>) <= 0) {
                currentNode = rotation(currentNode, RotationType.RIGHT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.right as AVLNode<Key, Value>?)
            } else {
                currentNode = rotation(currentNode.left, RotationType.LEFT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.left as AVLNode<Key, Value>?)
                currentNode = rotation(currentNode.parent, RotationType.RIGHT) as AVLNode<Key, Value>
                updateHeightAfterRotation(currentNode.right as AVLNode<Key, Value>?)
            }
        }
        (currentNode.parent as AVLNode<Key, Value>?)?.let { return balancing(it) }
        return currentNode
    }

    private fun updateHeight(node: AVLNode<Key, Value>) {
        val left = node.left?.let { (it as AVLNode).height } ?: 0U
        val right = node.right?.let { (it as AVLNode).height } ?: 0U
        node.height = (maxOf(left, right) + 1U).toUByte()
    }

    private fun updateHeightAfterRotation(node: AVLNode<Key, Value>?) {
        node?.let { updateHeight(it) } ?: 0U
        node?.parent?.let { updateHeight(it as AVLNode<Key, Value>) } ?: 0U
    }

    private fun balanceFactor(node: AVLNode<Key, Value>): Int {
        val left = node.left?.let { (it as AVLNode).height.toInt() } ?: 0
        val right = node.right?.let { (it as AVLNode).height.toInt() } ?: 0
        return (right - left)
    }
}