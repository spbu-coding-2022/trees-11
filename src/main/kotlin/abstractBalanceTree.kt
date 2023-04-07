abstract class BalanceTree<Key : Comparable<Key>, Value> : BinTree<Key, Value>() {
    enum class RotationType { Left, Right }

    protected fun rotation(parentKey: Key, type: RotationType): BinNode<Key, Value>? {
        //giving the parentNode key
        val parent: BinNode<Key, Value>? = getNode(parentKey)
        parent?.let {
            val node = if (type.name == "Left")
                it.right ?: error("rotation is not possible")
            else it.left ?: error("rotation is not possible")

            when (type.name) {
                "Left" -> {
                    it.right = node.left
                    node.left?.parent = it
                }

                "Right" -> {
                    it.left = node.right
                    node.right?.parent = it
                }
            }
            replaceNodeParent(it, node)
            it.parent = node
            node.left = it
            return node
        }
        return null
    }
}

