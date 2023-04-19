package interfaces

interface Tree<Key, Value> {
    fun insert(key: Key, value: Value)
    fun insert(vararg array: Pair<Key, Value>)
    fun remove(key: Key)
    fun remove(vararg keys: Key)
    fun get(key: Key): Value?
    fun get(vararg keys: Key): List<Value?>
}