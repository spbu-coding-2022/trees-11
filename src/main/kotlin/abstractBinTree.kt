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

    protected fun insertNode(key: Key, value: Value): Node<Key, Value>? {
        if (rootNode == null)
            rootNode = Node(key, value)
        else {
            val parent = getParent(key)
            if (parent != null) {
                if (parent < key)
                    if (parent.right == null) {
                        parent.right = Node(key, value)
                        Node(key, value).parent = parent
                    }
                    else parent.right?.value = value ?: error("unexpected null")
                else
                    if (parent.left == null) {
                        parent.left = Node(key, value)
                        Node(key, value).parent = parent
                    }
                    else (parent.left)?.value = value ?: error("unexpected null")
            }
            return parent
        }
        return null
    }

    abstract fun remove(key: Key)

    protected fun removeNode(key: Key): Node<Key, Value>? {
        val node: Node<Key, Value>? = getNode(key)
        if (node == null)
            return null
        else if ((node.left == null) && (node.right == null)) {
            val parent: Node<Key, Value>? = node.parent
            if (parent == null)
                rootNode = null
            else if (node == parent.left)
                parent.left = null
            else
                parent.right = null
        } else if (node.left == null)
            transplant(node, node.right ?: error("unexpected null"))
        else if (node.right == null)
            transplant(node, node.left ?: error("unexpected null"))
        else {
            val nextNode = nextElement(node)
            if (nextNode != null) {
                nextNode.right?.let { transplant(nextNode, it) }
                nextNode.right = node.right
                nextNode.left = node.left
                nextNode.right?.parent = nextNode
                nextNode.left?.parent = nextNode
                transplant(node, nextNode)
            }
        }
        return node.parent
    }

    protected fun getParent(key: Key): Node<Key, Value>? {
        tailrec fun recFind(curNode: Node<Key, Value>?): Node<Key, Value>? {
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

    protected fun breadthFirstSearch(function: (Node<Key, Value>?) -> Unit, addNullNodes: Boolean) {
        val queue = mutableListOf(rootNode)

        fun notNullInQueue(): Boolean {
            for (i in queue)
                if (i != null)
                    return true
            return false
        }

        while (queue.isNotEmpty()) {
            val node = queue.last()
            queue.removeLast()
            function(node)
            if (node != null) {
                queue.add(0, node.left)
                queue.add(0, node.right)
            } else if (addNullNodes) {
                queue.add(0, null)
                queue.add(0, null)
            }
            if (!notNullInQueue())
                return
        }
    }

    open inner class Debug {
        fun treeKeysInString(): String {
            var sizeOfLevel = 1
            var elemInTheLevel = 0
            var string = ""

            fun function(node: Node<Key, Value>?) {
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
