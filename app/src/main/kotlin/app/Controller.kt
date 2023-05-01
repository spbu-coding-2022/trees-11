package app

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dataBase.*
import trees.*
import java.io.IOException
import kotlin.math.pow

class Controller {
    init {
        try {
            System.getProperties().load(ClassLoader.getSystemResourceAsStream("App.properties"))
            System.getProperties().load(ClassLoader.getSystemResourceAsStream("Json.properties"))
            System.getProperties().load(ClassLoader.getSystemResourceAsStream("Neo4j.properties"))
            System.getProperties().load(ClassLoader.getSystemResourceAsStream("SQLite.properties"))
        } catch (ex: Exception) {
            throw IOException("Cannot get properties file\nCheck that all properties file exist in the src/main/kotlin/app/resources\n$ex")
        }
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

    fun validateName(name: String) {
        for (i in name)
            if (i !in 'a'..'z' && i !in 'A'..'Z' && i !in '0'..'9')
                throw IllegalArgumentException("Unsupported tree name, please use only ascii letters or digits")
        if (name[0] in '0'..'9')
            throw IllegalArgumentException("Unsupported tree name, please don't use a digit as the first char")
        if (name.length !in 1..System.getProperty("max_string_len")
                .toInt()
        ) throw IllegalArgumentException("Incorrect tree name\nThe name must be less than ${System.getProperty("max_string_len")} and greater than 0")
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

    inner class Database(private val databaseType: DatabaseType) {
        private val database = getDatabase(databaseType)

        fun getAllTrees() = database.getAllTrees()
        fun removeTree(treeName: String) = database.removeTree(treeName)
        fun clean() = database.clean()
        fun close() = database.close()
    }

    open class DrawNode(
        var key: String,
        var value: String,
        var coordinates: MutableState<Pair<Float, Float>>,
        var prevCoordinates: MutableState<Pair<Float, Float>?>
    )

    inner class DrawTree {
        private var tree: BinTree<String, Pair<String, Pair<Float, Float>>>
        private var treeName: String
        var viewCoordinates = Pair(0F, 0F)

        constructor(treeName: String, databaseType: DatabaseType) {
            this.treeName = treeName
            val treeData = getDatabase(databaseType).readTree(treeName)
            tree = treeData.first
            viewCoordinates = treeData.second
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

        private fun rewriteAllCoordinates() {
            val startCoordinate = Pair(0F, 0F) //coordinates of the root node

            val xMinInterval = 4F //interval between nodes
            val yInterval = 4F

            fun offsetOnLevel(level: Int, height: Int) =
                ((height - 2) * xMinInterval * (0.5.pow(level) - 1) * (-2)).toFloat() //the sum of the terms of the geometric progression

            var lastLevel = -1
            var curX = startCoordinate.first
            var curY = startCoordinate.second + yInterval
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

        fun updateCoordinate(node: DrawNode) {
            tree.insert(node.key, Pair(node.value, node.coordinates.value))
        }

        fun saveToDB(databaseType: DatabaseType) {
            getDatabase(databaseType).saveTree(treeName, tree, viewCoordinates)
        }

        fun clean() {
            tree.clean()
        }
    }
}
