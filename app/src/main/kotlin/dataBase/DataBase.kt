package dataBase

import trees.*
import java.io.IOException

interface DataBase {
    fun isSupportTreeType(tree: BinTree<*, *>) = when (tree) {
            is BSTree,
            is RBTree,
            is AVLTree -> true
            else -> false
        }   

    fun typeToTree(type: String): BinTree<String, Pair<String, Pair<Float, Float>>> = when (type) {
        BSTree::class.simpleName -> BSTree()
        RBTree::class.simpleName -> RBTree()
        AVLTree::class.simpleName -> AVLTree()
        else -> throw IOException("invalid type of tree")
    }

    fun validateName(name: String) {
        for (i in name)
            if (i !in 'a'..'z' && i !in 'A'..'Z' && i !in '0'..'9')
                throw IllegalArgumentException("Unsupported tree name, please use only ascii letters or digits")
        if (name[0] in '0'..'9')
            throw IllegalArgumentException("Unsupported tree name, please don't use a digit as the first char")
        if (name.isEmpty()) throw IllegalArgumentException("Incorrect tree name")
    }

    fun saveTree(treeName: String, tree: BinTree<String, Pair<String, Pair<Float, Float>>>)
    fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Float, Float>>>
    fun removeTree(treeName: String)
    fun getAllTree(): List<Pair<String, String>>
    fun clean()
    fun close()
    fun cleanAndClose() {
        clean()
        close()
    }
}
