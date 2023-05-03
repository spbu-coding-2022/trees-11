package app.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dataBase.*
import trees.*
import java.io.IOException
import kotlin.math.pow

object Controller {
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

    enum class KeysType {
        Int,
        Float,
        String
    }

    enum class DatabaseType {
        Json,
        Neo4j,
        SQLite
    }

    fun validKey(key: String) = run {
        try {
            key.toInt()
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun validateName(name: String) {
        for (i in name)
            if (i !in 'a'..'z' && i !in 'A'..'Z' && i !in '0'..'9')
                throw IllegalArgumentException("Please use only ascii letters or digits")
        if (name.isNotEmpty() && name[0] in '0'..'9')
            throw IllegalArgumentException("Please don't use a digit as the first char")
        if (name.length !in 1..System.getProperty("max_string_len")
                .toInt()
        ) throw IllegalArgumentException("The name must be less than ${System.getProperty("max_string_len")} and greater than 0")
    }

    fun getTree(treeType: TreeType, keysType: KeysType) = when(keysType) {
        KeysType.Int -> getTree<Int>(treeType)
        else -> throw IllegalArgumentException("Only Int support now")
    }

    private fun <Key: Comparable<Key>> getTree(treeType: TreeType) = when (treeType) {
        TreeType.BSTree -> BSTree<Key, Pair<String, Pair<Float, Float>>>()
        TreeType.AVLTree -> AVLTree<Key, Pair<String, Pair<Float, Float>>>()
        TreeType.RBTree -> RBTree<Key, Pair<String, Pair<Float, Float>>>()
    }

    fun getDatabase(databaseType: DatabaseType) = when (databaseType) {
        DatabaseType.Json -> Json(System.getProperty("json_save_dir"))
        DatabaseType.Neo4j -> Neo4j(
            System.getProperty("neo4j_uri"),
            System.getProperty("neo4j_user"),
            System.getProperty("neo4j_password")
        )

        DatabaseType.SQLite -> SQLite(System.getProperty("sqlite_path"), System.getProperty("max_string_len").toUInt())
    }

    class Database(databaseType: DatabaseType) {
        private val database = getDatabase(databaseType)

        fun getAllTrees() = database.getAllTrees()
        fun removeTree(treeName: String) = database.removeTree(treeName)
        fun clean() = database.clean()
        fun close() = database.close()
    }

    open class DrawNode(
        var key: String,
        var value: String,
        var x: MutableState<Float>,
        var y: MutableState<Float>,
        var parent: DrawNode?
    )

    class DrawTree {
        private var tree: BinTree<Int, Pair<String, Pair<Float, Float>>>
        private var treeName: String
        private var keysType: KeysType

        var viewCoordinates = Pair(0F, 0F)

        var startCoordinate = Pair(0F, 0F) //coordinates of the root node

        var xMinInterval = 100F //interval between nodes
        var yInterval = -100F //interval between nodes
        var content = mutableStateOf(listOf<DrawNode>())

        constructor(treeName: String, databaseType: DatabaseType) {
            this.treeName = treeName
            val treeData = getDatabase(databaseType).readTree(treeName)
            tree = treeData.first
            viewCoordinates = treeData.second
            keysType = KeysType.String
        }

        constructor(treeName: String, treeType: TreeType, keysType: KeysType) {
            this.treeName = treeName
            this.keysType = keysType
            tree = getTree(treeType, keysType)
        }

        fun getAllDrawNodes(): MutableList<DrawNode> {
            val listOfDrawNodes = mutableListOf<DrawNode>()
            val mapOfKeysNodes = mutableMapOf<Int?, MutableList<DrawNode>>()
            for (i in tree.getNodesDataWithParentKeys().reversed()) {
                val node = DrawNode(
                    i.first.toString(),
                    i.second.first,
                    mutableStateOf(i.second.second.first),
                    mutableStateOf(i.second.second.second),
                    parent = null
                )
                listOfDrawNodes.add(node)
                if (mapOfKeysNodes[i.third] == null)
                    mapOfKeysNodes[i.third] = mutableListOf(node)
                else mapOfKeysNodes[i.third]?.add(node)
                mapOfKeysNodes[i.first]?.forEach { it.parent = node }
            }

            return listOfDrawNodes
        }

        fun reInitAllDrawNodes() {
            content.value = getAllDrawNodes()
        }

        private fun rewriteAllCoordinates() {
            fun offsetOnLevel(level: Int, height: Int) = if (height == 2 && level != 0) xMinInterval / 2 else
                ((0.5.pow(level) - 1) * (height - 2) * xMinInterval * (-2)).toFloat() //the sum of the terms of the geometric progression

            var lastLevel = -1
            var curX = startCoordinate.first
            var curY = startCoordinate.second + yInterval
            var levelInterval = 0F
            tree.rewriteAllValue(true) { value, level, height ->
                if (level != lastLevel) {
                    curY -= yInterval
                    curX = startCoordinate.first - offsetOnLevel(level, height)
                    levelInterval = xMinInterval * 2F.pow(height - level - 1)
                } else curX += levelInterval
                lastLevel = level
                if (value != null)
                    Pair(value.first, Pair(curX, curY))
                else null
            }
        }

        fun drawInsert(key: String, value: String) {
            tree.insert(key.toInt(), Pair(value, Pair(0F, 0F)))

            rewriteAllCoordinates()
        }

        fun drawRemove(key: String) {
            tree.remove(key.toInt())

            rewriteAllCoordinates()
        }

        fun drawFind(key: String) = tree.get(key.toInt())?.first

        fun updateCoordinate(node: DrawNode) {
            tree.insert(node.key.toInt(), Pair(node.value, Pair(node.x.value, node.y.value)))
        }

        fun saveToDB(databaseType: DatabaseType) {
            getDatabase(databaseType).saveTree(treeName, tree, viewCoordinates)
        }

        fun clean() {
            tree.clean()
        }
    }
}
