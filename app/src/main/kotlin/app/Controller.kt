package app

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dataBase.*
import trees.*

class Controller {
    init {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("Json.properties"))
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("Neo4j.properties"))
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("SQLite.properties"))
    }


    enum class TreeType {
        RBTree,
        AVLTree,
        BSTree
    }

    enum class DatabaseType {
        Json,
        Neo4j,
        SQLite
    }

    fun getTree(treeType: TreeType) = when (treeType) {
        TreeType.BSTree -> BSTree<String, Pair<String, Pair<Float, Float>>>()
        TreeType.AVLTree -> AVLTree<String, Pair<String, Pair<Float, Float>>>()
        TreeType.RBTree -> RBTree<String, Pair<String, Pair<Float, Float>>>()
    }

    fun getDatabase(databaseType: DatabaseType) = when (databaseType) {
        DatabaseType.Json -> Json(System.getProperty("save_dir"))
        DatabaseType.Neo4j -> Neo4j(
            System.getProperty("uri"),
            System.getProperty("user"),
            System.getProperty("password")
        )
        DatabaseType.SQLite -> SQLite(System.getProperty("sqlite_path"), System.getProperty("max_string_len").toUInt())
    }

    open class DrawNode(
        var key: String,
        var value: String,
        var coordinates: MutableState<Pair<Float, Float>>,
        var prevCoordinates: MutableState<Pair<Float, Float>?>
    )

    fun getAllTrees(databaseType: DatabaseType) = getDatabase(databaseType).getAllTree()

    inner class DrawTree {
        var tree: BinTree<String, Pair<String, Pair<Float, Float>>>
        var treeName: String

        constructor(treeName: String, databaseType: DatabaseType) {
            this.treeName = treeName
            tree = getDatabase(databaseType).readTree(treeName)
        }

        constructor(treeName: String, treeType: TreeType) {
            this.treeName = treeName
            tree = getTree(treeType)
        }

        fun getAllDrawNodes(name: String, treeType: TreeType) =
            tree.getNodesDataWithParentValue().map { data ->
                DrawNode(
                    data.first,
                    data.second.first,
                    mutableStateOf(data.second.second),
                    mutableStateOf(data.third?.second)
                )
            }

        fun drawInsert(key: String, value: String) {
            val parentData = tree.getParentData(key)
            var coordinate = Pair(0F, 0F)

            if (parentData != null) {
                coordinate = Pair(parentData.second.second.first + if (key < parentData.first) -4F else 4F, parentData.second.second.second + 4F)
            }
            tree.insert(key, Pair(value, coordinate))
        }
    }


    fun drawRemove(key: String) {
        TODO()
    }

    fun drawFind(key: String) {
        TODO()
    }
}
