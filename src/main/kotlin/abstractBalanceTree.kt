abstract class BalanceTree<Key : Comparable<Key>, Value>: BinTree<Key, Value>() {
    protected abstract fun rebalancing()
    protected abstract fun leftRotation()
    protected abstract fun rightRotation()
}
