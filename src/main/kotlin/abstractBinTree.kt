import kotlin.math.abs

abstract class BinTree<Key : Comparable<Key>, Value> {
    protected open class Node<Key : Comparable<Key>, Value>(
        val key: Key,
        var value: Value,
        var parent: Node<Key, Value>? = null,
        var left: Node<Key, Value>? = null,
        var right: Node<Key, Value>? = null

    ) : Comparable<Key> {
        override fun compareTo(other: Key): Int {
            return key.compareTo(other)
        }

        fun equalKey(other: Key): Boolean {
            return this.compareTo(other) == 0
        }
    }

    protected open var rootNode: Node<Key, Value>? = null

    constructor()
    constructor(key: Key, value: Value) {
        insert(key, value)
    }

    private fun sortInsert(array: Array<out Pair<Key, Value>>) {
        val serArray = array.sortedBy { it.first }.toTypedArray()
        var indices = serArray.indices.toList()
        indices = indices.sortedBy { abs(serArray.size / 2 - it) }
        for (i in indices) {
            insert(serArray[i].first, serArray[i].second)
        }
    }

    constructor(array: Array<out Pair<Key, Value>>) {
        sortInsert(array)
    }

    abstract fun insert(key: Key, value: Value)
    abstract fun remove(key: Key)


    protected fun getParent(key: Key): Node<Key, Value>? {
        tailrec fun recFind(curNode: Node<Key, Value>?): Node<Key, Value>? {
            return if (curNode == null)
                null
            else if (curNode > key) {
                if (curNode.left?.equalKey(key) != false)
                    curNode
                else
                    recFind(curNode.left)
            } else {
                if (curNode.right?.equalKey(key) != false)
                    curNode
                else
                    recFind(curNode.right)
            }
        }

        return recFind(rootNode)
    }

    protected fun getNode(key: Key): Node<Key, Value>? {
        if (rootNode?.equalKey(key) == true)
            return rootNode
        val parent = getParent(key)
        return if (parent == null)
            null
        else if (parent.left?.equalKey(key) == true)
            parent.left
        else
            parent.right
    }

    open fun get(key: Key): Value? {
        return getNode(key)?.value
    }

    protected open fun nextElement(node: Node<Key, Value>): Node<Key, Value>? {
        val nodeRight: Node<Key, Value> = node.right ?: return null
        return minElement(nodeRight.key)
    }

    protected fun minElement(key: Key): Node<Key, Value>? {
        var minNode: Node<Key, Value>? = getNode(key) ?: return null
        while (minNode?.left != null) {
            minNode = minNode.left ?: error("unexpected null")
        }
        return minNode
    }

    protected fun maxElement(key: Key): Node<Key, Value>? {
        var maxNode: Node<Key, Value>? = getNode(key) ?: return null
        while (maxNode?.right != null) {
            maxNode = maxNode.right ?: error("unexpected null")
        }
        return maxNode
    }

    protected open fun transplant(oldNode: Node<Key, Value>, newNode: Node<Key, Value>) {
        val parent: Node<Key, Value>? = oldNode.parent
        if (parent == null)
            rootNode = newNode
        else if (oldNode == parent.left) {
            parent.left = newNode
        } else {
            parent.right = newNode
        }
        newNode.parent = parent
    }
}
