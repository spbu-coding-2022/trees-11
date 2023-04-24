package dataBase

import AVLTree
import BSTree
import BinTree
import RBTree
import java.sql.SQLException

interface DataBase {
    fun isSupportTreeType(treeType: String): Boolean {
        val supportTypes = arrayOf("BSTree", "RBTree", "AVLTree")
        return (treeType in supportTypes)
    }

    fun typeToTree(type: String): BinTree<String, Pair<String, Pair<Double, Double>>> = when (type) {
        "BSTree" -> BSTree()
        "RBTree" -> RBTree()
        "AVLTree" -> AVLTree()
        else -> throw SQLException("invalid type of tree")
    }
    fun saveTree(treeName: String, tree: BinTree<String, Pair<String, Pair<Double, Double>>>, treeType: String)
    fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Double, Double>>>
    fun removeTree(treeName: String)
    fun getAllTree(): List<Pair<String, String>>
    fun clean()
    fun close()
    fun cleanAndClose() {
        clean()
        close()
    }
}