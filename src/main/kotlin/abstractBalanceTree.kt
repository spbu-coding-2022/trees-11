abstract class BalanceTree<Key : Comparable<Key>, Value> : BinTree<Key, Value>() {
    enum class RotationType { Left, Right }

    protected fun rotation(parent: BinNode<Key, Value>?, type: RotationType): BinNode<Key, Value>? {
        //giving the parentNode
        parent?.let {
            val node = if (type == RotationType.Left)
                it.right ?: error("rotation is not possible")
            else it.left ?: error("rotation is not possible")

            when (type) {
                RotationType.Left -> {
                    it.right = node.left
                    node.left?.parent = it
                    node.left = it
                }

                RotationType.Right -> {
                    it.left = node.right
                    node.right?.parent = it
                    node.right = it
                }
            }
            replaceNodeParent(it, node)
            it.parent = node
            return node
        }
        return null
    }
}

