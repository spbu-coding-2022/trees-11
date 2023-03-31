abstract class BalanceTree<Key : Comparable<Key>, Value> : BinTree<Key, Value>() {
    protected abstract fun rebalancing()

    enum class RotationType { Left, Right }

    protected fun rotation(key: Key, type: RotationType) {
        //giving the parentNode key
        val parent: Node<Key, Value>? = getNode(key)
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
            transplant(it, node)
            it.parent = node
            node.left = it
        }
    }
}

