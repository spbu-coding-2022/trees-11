package dataBase

import trees.BinTree
import java.io.IOException
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class SQLite(dbPath: String, val maxStringLen: UInt) : DataBase {
    companion object {
        private const val DB_DRIVER = "jdbc:sqlite"
    }

    private val connection = try {
        DriverManager.getConnection("$DB_DRIVER:$dbPath")
    } catch (ex: Exception) {
        throw SQLException("Cannot connect to database\nCheck that it is running and that there is no error in the path to it\n$ex")
    }
        ?: throw SQLException("Cannot connect to database\nCheck that it is running and that there is no error in the path to it")
    private val addTreeStatement by lazy { connection.prepareStatement("INSERT INTO trees (name, type, viewX, viewY) VALUES (?, ?, ?, ?);") }
    private val getAllTreesStatement by lazy { connection.prepareStatement("SELECT trees.name as name, trees.type as type FROM trees;") }


    override fun saveTree(
        treeName: String,
        tree: BinTree<String, Pair<String, Pair<Float, Float>>>,
        viewCoordinates: Pair<Float, Float>
    ) {
        if (!isSupportTreeType(tree)) throw IllegalArgumentException("Unsupported tree type")
        validateName(treeName)

        createTreesTable()
        removeTree(treeName)
        createTableForTree(treeName)
        addTree(
            treeName,
            tree::class.simpleName ?: throw IllegalArgumentException("Cannot get tree type"),
            viewCoordinates
        )

        val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO ${treeName}Nodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
        tree.getKeyValueList()
            .forEach { saveNode(it.first, it.second.first, it.second.second, treeName, addNodeStatement) }
        addNodeStatement.close()
    }

    private fun saveNode(
        key: String,
        value: String,
        coordinate: Pair<Float, Float>,
        treeName: String,
        addNodeStatement: PreparedStatement
    ) {
        try {
            addNodeStatement.setString(1, key)
            addNodeStatement.setString(2, value)
            addNodeStatement.setFloat(3, coordinate.first)
            addNodeStatement.setFloat(4, coordinate.second)

            addNodeStatement.execute()
        } catch (ex: Exception) {
            throw SQLException("Cannot add node with key: \"${key}\" in tree: $treeName")
        }
    }

    private fun createTreesTable() {
        try {
            executeQuery(
                "CREATE TABLE if not exists trees (treeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "name varchar($maxStringLen), " +
                        "type varchar($maxStringLen), " +
                        "viewX FLOAT, " +
                        "viewY FLOAT);"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database\n$ex")
        }
    }

    private fun createTableForTree(treeName: String) {
        validateName(treeName)

        try {
            executeQuery(
                "CREATE TABLE if not exists ${treeName}Nodes (nodeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "key varchar($maxStringLen), " +
                        "value varchar($maxStringLen), " +
                        "x FLOAT, " +
                        "y FLOAT);"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database\n$ex")
        }
    }

    private fun executeQuery(query: String) {
        connection.createStatement().also { stmt ->
            try {
                stmt.execute(query)
            } catch (ex: Exception) {
                throw SQLException("Cannot execute query: \"$query\"\n$ex")
            } finally {
                stmt.close()
            }
        }
    }

    private fun addTree(treeName: String, treeType: String, viewCoordinates: Pair<Float, Float>) {
        try {
            addTreeStatement.setString(1, treeName)
            addTreeStatement.setString(2, treeType)
            addTreeStatement.setFloat(3, viewCoordinates.first)
            addTreeStatement.setFloat(4, viewCoordinates.second)

            addTreeStatement.execute()
        } catch (ex: Exception) {
            throw SQLException("Cannot add tree: $treeName\n$ex")
        }
    }

    private fun getTreeData(treeName: String): Pair<String, Pair<Float, Float>> {
        val getTreeTypeStatement by lazy { connection.prepareStatement("SELECT trees.type as type, trees.viewX as x, trees.viewY as y FROM trees WHERE name = ?") }
        getTreeTypeStatement.setString(1, treeName)
        try {
            val data = getTreeTypeStatement.executeQuery()
            return Pair(data.getString("type"), Pair(data.getFloat("x"), data.getFloat("y")))
        } catch (ex: Exception) {
            throw SQLException("Cannot get tree type from database\n$ex")
        } finally {
            getTreeTypeStatement.close()
        }

    }

    override fun readTree(treeName: String): Pair<BinTree<String, Pair<String, Pair<Float, Float>>>, Pair<Float, Float>> {
        validateName(treeName)

        val nodes = "${treeName}Nodes"
        val getAllNodesStatement by lazy { connection.prepareStatement("SELECT $nodes.key as key, $nodes.value as value, $nodes.x as x, $nodes.y as y FROM $nodes;") }

        val treeData = getTreeData(treeName)
        val tree = typeToTree(treeData.first)

        try {
            val nodesSet = getAllNodesStatement.executeQuery()
            while (nodesSet.next()) {
                tree.insert(
                    nodesSet.getString("key"),
                    Pair(
                        nodesSet.getString("value"),
                        Pair(nodesSet.getFloat("x"), nodesSet.getFloat("y"))
                    )
                )
            }
        } catch (ex: Exception) {
            throw IOException("Cannot get nodes from database\n$ex")
        } finally {
            getAllNodesStatement.close()
        }

        return Pair(tree, treeData.second)
    }

    override fun removeTree(treeName: String) {
        validateName(treeName)

        executeQuery("DROP TABLE IF EXISTS ${treeName}Nodes;")

        executeQuery("DELETE FROM trees WHERE name = '$treeName';")
    }

    override fun getAllTrees(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val treesSet = getAllTreesStatement.executeQuery()
        while (treesSet.next()) {
            list.add(Pair(treesSet.getString("name"), treesSet.getString("type")))
        }

        return list
    }

    override fun close() {
        addTreeStatement.close()
        getAllTreesStatement.close()
        connection.close()
    }

    override fun clean() {
        for (i in getAllTrees())
            removeTree(i.first)
    }
}
