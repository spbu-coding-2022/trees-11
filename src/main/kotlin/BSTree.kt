open class BSTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(vararg pairs: Pair<Key, Value>) : super(pairs)

    override fun insert(key: Key, value: Value) {
        insertNode(key, value)
    }

    override fun remove(key: Key) {
       removeNode(key)
    }
}
