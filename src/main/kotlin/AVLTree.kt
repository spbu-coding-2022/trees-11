class AVLTree<Key : Comparable<Key>, Value> : BalanceTree<Key, Value>() {
    protected class AVLNode<Key : Comparable<Key>, Value>(
        key: Key,
        value: Value,
        parent: AVLNode<Key, Value>? = null,
        left: AVLNode<Key, Value>? = null,
        right: AVLNode<Key, Value>? = null,
        var height: UByte = 0U
    ) : BinNode<Key, Value>(key, value, parent, left, right)

    override fun insert(key: Key, value: Value) {
        if (rootNode != null) {
            if ((rootNode as AVLNode).height != 255.toUByte()) {
                insertService(AVLNode(key, value))
            }
        } else
            rootNode = AVLNode(key, value)
        TODO("add balancing")
    }

    override fun remove(key: Key) {
        TODO("Not yet implemented")
    }
}
