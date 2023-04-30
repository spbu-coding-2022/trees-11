package app

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dataBase.*
import trees.*
import kotlin.math.pow

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

        fun getAllDrawNodes() =
            tree.getNodesDataWithParentValue().map { data ->
                DrawNode(
                    data.first,
                    data.second.first,
                    mutableStateOf(data.second.second),
                    mutableStateOf(data.third?.second)
                )
            }

        fun rewriteAllCoordinates() {
            val startCoord = Pair(0F, 0F) //coordinates of the root node

            val xMinInterval = 4F //interval between nodes
            val yInterval = 4F

            fun offsetOnLevel(level: Int, height: Int) =
                ((height - 2) * xMinInterval * (0.5.pow(level) - 1) * (-2)).toFloat() //the sum of the terms of the geometric progression

            var lastLevel = -1
            var curX = startCoord.first
            var curY = startCoord.second + yInterval
            var levelInterval = 0F
            tree.rewriteAllValue(true) { value, level, height ->
                if (level != lastLevel) {
                    curY -= yInterval
                    curX = -offsetOnLevel(level, height)
                    levelInterval = xMinInterval * 2F.pow(height - level - 1)
                } else curX += levelInterval
                lastLevel = level
                if (value != null)
                    Pair(value.first, Pair(curX, curY))
                else null
            }
        }

        fun drawInsert(key: String, value: String) {
            tree.insert(key, Pair(value, Pair(0F, 0F)))

            rewriteAllCoordinates()
        }

        fun drawRemove(key: String) {
            tree.remove(key)

            rewriteAllCoordinates()
        }

        fun drawFind(key: String) = tree.get(key)?.first
    }
}
