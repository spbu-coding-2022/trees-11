package trees

open class BSTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
        insertService(BinNode(key, value))
    }

    override fun remove(key: Key) {
        val node = getNode(key) ?: return
        removeService(node)
    }
}
