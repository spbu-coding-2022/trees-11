abstract class BalanceTree<Key : Comparable<Key>, Value> : BinTree<Key, Value>() {
    protected abstract fun rebalancing()

    protected fun rotation(key: Key, type: String) {
        //giving the parentNode key
        val parent: Node<Key, Value>? = getNode(key)
        parent?.let {
            val node = if (type == "left")
                it.right ?: error("rotation is not possible")
            else it.left ?: error("rotation is not possible")

            when (type) {
                "left" -> {
                    it.right = node.left
                    node.left?.parent = it
                }

                "right" -> {
                    it.left = node.right
                    node.right?.parent = it
                }

                else -> error("rotation is not possible: wrong type entered")
            }
            transplant(it, node)
            it.parent = node
            node.left = it
        }
    }

}
