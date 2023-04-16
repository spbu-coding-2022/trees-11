import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

abstract class BinTree<Key : Comparable<Key>, Value> {
    protected open class BinNode<Key : Comparable<Key>, Value>(
        val key: Key,
        var value: Value,
        var parent: BinNode<Key, Value>? = null,
        var left: BinNode<Key, Value>? = null,
        var right: BinNode<Key, Value>? = null

    ) : Comparable<Key> {
        override fun compareTo(other: Key): Int {
            return key.compareTo(other)
        }

        fun equalKey(other: Key): Boolean {
            return this.compareTo(other) == 0
        }
    }


    protected open var rootNode: BinNode<Key, Value>? = null

    constructor()
    constructor(key: Key, value: Value) {
        insert(key, value)
    }
    constructor(array: Array<out Pair<Key, Value>>) {
        sortInsert(array)
    }

    private fun sortInsert(array: Array<out Pair<Key, Value>>) {
        val serArray = array.sortedBy { it.first }.toTypedArray()
        var indices = serArray.indices.toList()
        indices = indices.sortedBy { abs(serArray.size / 2 - it) }
        for (i in indices) {
            insert(serArray[i].first, serArray[i].second)
        }
    }

    abstract fun insert(key: Key, value: Value)

    fun insert(vararg array: Pair<Key, Value>) {
        sortInsert(array)
    }

    abstract fun remove(key: Key)

    fun remove(vararg array: Key) {
        for (i in array)
            remove(i)
    }

    //return the inserted node if the node with the same key wasn't in the tree and null in otherwise
    //doesn't balance the tree
    protected fun insertService(node: BinNode<Key, Value>): BinNode<Key, Value>? {
        if (rootNode == null) {
            rootNode = node
            return node
        } else {
            val parent = getParent(node.key)
            if (parent != null) {
                if (parent < node.key)
                    if (parent.right == null) {
                        node.parent = parent
                        parent.right = node
                        return node
                    } else parent.right?.value = node.value ?: error("unexpected null")
                else
                    if (parent.left == null) {
                        node.parent = parent
                        parent.left = node
                        return node
                    } else (parent.left)?.value = node.value ?: error("unexpected null")
            } else rootNode?.value = node.value ?: error("unexpected null")
        }
        return null
    }

    protected fun getParent(key: Key): BinNode<Key, Value>? {
        tailrec fun recFind(curNode: BinNode<Key, Value>?): BinNode<Key, Value>? {
            return if (curNode == null)
                null
            else if (curNode > key) {
                if (curNode.left?.equalKey(key) != false)
                    curNode
                else
                    recFind(curNode.left)
            } else if (curNode.equalKey(key)) {
                return curNode.parent
            } else {
                if (curNode.right?.equalKey(key) != false)
                    curNode
                else
                    recFind(curNode.right)
            }
        }
        return recFind(rootNode)
    }

    protected fun getNode(key: Key): BinNode<Key, Value>? {
        if (rootNode?.equalKey(key) == true)
            return rootNode
        val parent = getParent(key)
        return if (parent == null)
            null
        else if (parent.left?.equalKey(key) == true)
            parent.left
        else if (parent.right?.equalKey(key) == true)
            parent.right
        else null
    }

    open fun get(key: Key): Value? {
        return getNode(key)?.value
    }

    fun get(vararg keys: Key): List<Value?> {
        return List(keys.size, {get(keys[it])})
    }

    protected fun breadthFirstSearch(function: (BinNode<Key, Value>?) -> Unit, addNullNodes: Boolean) {
        val queue: Queue<BinNode<Key, Value>?> = LinkedList(listOf(rootNode))

        fun notNullInQueue(): Boolean {
            for (i in queue)
                if (i != null)
                    return true
            return false
        }

        while (queue.isNotEmpty()) {
            val node = queue.remove()
            function(node)
            if (node != null) {
                queue.add(node.left)
                queue.add(node.right)
            } else if (addNullNodes) {
                queue.add(null)
                queue.add(null)
            }
            if (!notNullInQueue())
                return
        }
    }

    internal open inner class Debug {
        fun treeKeysInString(): String {
            var sizeOfLevel = 1
            var elemInTheLevel = 0
            var string = ""

            fun function(node: BinNode<Key, Value>?) {
                string += node?.key ?: "-"
                string += " "
                elemInTheLevel += 1
                if (elemInTheLevel == sizeOfLevel) {
                    sizeOfLevel *= 2
                    elemInTheLevel = 0
                    string += "\n"
                }
            }

            breadthFirstSearch(::function, true)
            return string
        }
    }
}
