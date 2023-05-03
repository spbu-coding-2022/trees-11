package trees

interface Tree<Key, Value> {
    /**
     * inserts node in tree
     *
     * @param key which will have a node, affects its position in the tree, must be a Comparable type
     *
     * If a node with this key already exists in the tree, its value will be overwritten
     * @param value which will have a node, can be any type
     */
    fun insert(key: Key, value: Value)
    /**
     * Calls an [insert] for each pair in the order of pairs' order.
     */
    fun insert(vararg array: Pair<Key, Value>)

    /**
     * deletes node with this key
     *
     * if node with this key doesn't exist, do nothing
     */
    fun remove(key: Key)
    /**
     * Calls an [remove] for each key in the order of keys' order.
     */
    fun remove(vararg keys: Key)

    /**
     * @return value of node with this key or null if node doesn't exist
     */
    fun get(key: Key): Value?
    /**
     * Calls an [get] for each key in the order of keys' order.
     *
     * @return List of values
     */
    fun get(vararg keys: Key): List<Value?>
}
