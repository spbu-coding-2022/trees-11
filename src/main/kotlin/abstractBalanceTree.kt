abstract class BalanceTree<Key : Comparable<Key>, Value> : BinTree<Key, Value> {
    constructor() : super()
    constructor(key: Key, value: Value) : super(key, value)
    constructor(pairs: Array<out Pair<Key, Value>>) : super(pairs)

    protected abstract fun rebalancing()
    protected abstract fun leftRotation()
    protected abstract fun rightRotation()
}
