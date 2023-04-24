package trees

import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

abstract class BinTree<Key : Comparable<Key>, Value> : Tree<Key, Value> {
    protected open class BinNode<Key : Comparable<Key>, Value>(
        var key: Key,
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

    constructor(array: Array<out Pair<Key, Value>>, sort: Boolean = false) {
        if (sort) sortInsert(*array)
        else insert(*array)
    }

    fun sortInsert(vararg array: Pair<Key, Value>) {
        val serArray = array.sortedBy { it.first }.toTypedArray()
        var indices = serArray.indices.toList()
        indices = indices.sortedBy { abs(serArray.size / 2 - it) }
        for (i in indices) {
            insert(serArray[i].first, serArray[i].second)
        }
    }

    override fun insert(vararg array: Pair<Key, Value>) {
        for (i in array) insert(i.first, i.second)
    }

    override fun remove(vararg keys: Key) {
        for (i in keys) remove(i)
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
                if (parent < node.key) if (parent.right == null) {
                    node.parent = parent
                    parent.right = node
                    return node
                } else parent.right?.value = node.value ?: error("unexpected null")
                else if (parent.left == null) {
                    node.parent = parent
                    parent.left = node
                    return node
                } else (parent.left)?.value = node.value ?: error("unexpected null")
            } else rootNode?.value = node.value ?: error("unexpected null")
        }
        return null
    }

    protected fun removeService(node: BinNode<Key, Value>) {
        if ((node.left == null) && (node.right == null)) {
            val parent: BinNode<Key, Value>? = node.parent
            if (parent == null) rootNode = null
            else if (node == parent.left) parent.left = null
            else parent.right = null
        } else if (node.left == null) replaceNodeParent(node, node.right ?: error("remove error: unexpected null"))
        else if (node.right == null) replaceNodeParent(node, node.left ?: error("remove error: unexpected null"))
        else {
            val nextNode = nextElement(node) ?: error("remove error: unexpected null")
            val parent = nextNode.parent ?: error("remove error: unexpected null")
            if (parent != node) {
                if (nextNode.right != null) replaceNodeParent(nextNode, nextNode.right)
                else parent.left = null
                nextNode.right = node.right
                nextNode.right?.parent = nextNode
            }
            nextNode.left = node.left
            nextNode.left?.parent = nextNode
            replaceNodeParent(node, nextNode)
        }
    }

    protected fun getParent(key: Key): BinNode<Key, Value>? {
        tailrec fun recFind(curNode: BinNode<Key, Value>?): BinNode<Key, Value>? {
            return if (curNode == null) null
            else if (curNode > key) {
                if (curNode.left?.equalKey(key) != false) curNode
                else recFind(curNode.left)
            } else if (curNode.equalKey(key)) {
                return curNode.parent
            } else {
                if (curNode.right?.equalKey(key) != false) curNode
                else recFind(curNode.right)
            }
        }
        return recFind(rootNode)
    }

    protected fun getNode(key: Key): BinNode<Key, Value>? {
        if (rootNode?.equalKey(key) == true) return rootNode
        val parent = getParent(key)
        return if (parent == null) null
        else if (parent.left?.equalKey(key) == true) parent.left
        else if (parent.right?.equalKey(key) == true) parent.right
        else null
    }

    override fun get(key: Key): Value? {
        return getNode(key)?.value
    }

    override fun get(vararg keys: Key): List<Value?> {
        return List(keys.size) { get(keys[it]) }
    }

    protected open fun nextElement(node: BinNode<Key, Value>): BinNode<Key, Value>? {
        val nodeRight: BinNode<Key, Value> = node.right ?: return null
        return minElement(nodeRight.key)
    }

    protected fun minElement(key: Key): BinNode<Key, Value>? {
        var minNode: BinNode<Key, Value>? = getNode(key) ?: return null
        while (minNode?.left != null) {
            minNode = minNode.left ?: error("min element not found: unexpected null")
        }
        return minNode
    }

    protected fun maxElement(key: Key): BinNode<Key, Value>? {
        var maxNode: BinNode<Key, Value>? = getNode(key) ?: return null
        while (maxNode?.right != null) {
            maxNode = maxNode.right ?: error("max element not found: unexpected null")
        }
        return maxNode
    }

    protected open fun replaceNodeParent(oldNode: BinNode<Key, Value>, newNode: BinNode<Key, Value>?) {
        val parent: BinNode<Key, Value>? = oldNode.parent
        if (parent == null) rootNode = newNode
        else if (oldNode == parent.left) {
            parent.left = newNode
        } else {
            parent.right = newNode
        }
        newNode?.let { it.parent = parent }
    }

    protected fun breadthFirstSearch(addNullNodes: Boolean = false, function: (BinNode<Key, Value>?) -> Unit) {
        val queue: Queue<BinNode<Key, Value>?> = LinkedList(listOf(rootNode))

        fun notNullInQueue(): Boolean {
            for (i in queue) if (i != null) return true
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
            if (!notNullInQueue()) return
        }
    }

    fun getKeyValueList(): List<Pair<Key, Value>> {
        val list = mutableListOf<Pair<Key, Value>>()
        breadthFirstSearch { node -> if (node != null) list.add(Pair(node.key, node.value)) }
        return list
    }

    internal open inner class Debug {
        fun treeKeysInString(): String {
            var sizeOfLevel = 1
            var elemInTheLevel = 0
            var string = ""

            breadthFirstSearch(true) { node ->
                string += node?.key ?: "-"
                string += " "
                elemInTheLevel += 1
                if (elemInTheLevel == sizeOfLevel) {
                    sizeOfLevel *= 2
                    elemInTheLevel = 0
                    string += "\n"
                }
            }
            return string
        }
    }
}
