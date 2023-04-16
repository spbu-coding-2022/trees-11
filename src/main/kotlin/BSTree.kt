open class BSTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    open override fun insert(key: Key, value: Value) {
        insertService(BinNode(key, value))
    }

    override fun remove(key: Key) {
        val node = getNode(key) ?: error("remove is not possible: there is no node with this key")
        removeService(node)
    }
}
