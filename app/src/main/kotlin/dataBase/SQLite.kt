package dataBase

import trees.BinTree
import java.io.IOException
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

class SQLite(dbPath: String, maxStringLen: UInt) : DataBase {
    companion object {
        private const val maxStringLen = 255
        private const val DB_DRIVER = "jdbc:sqlite"
    }

    private val connection = DriverManager.getConnection("$DB_DRIVER:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val addTreeStatement by lazy { connection.prepareStatement("INSERT INTO trees (name, type) VALUES (?, ?);") }
    private val getAllTreesStatement by lazy { connection.prepareStatement("SELECT trees.name as name, trees.type as type FROM trees;") }


    override fun saveTree(treeName: String, tree: BinTree<String, Pair<String, Pair<Double, Double>>>) {
        if (!isSupportTreeType(tree)) throw IllegalArgumentException("Unsupported tree type")
        validateName(treeName)

        removeTree(treeName)
        createTreesTable()
        createTableForTree(treeName)
        addTree(treeName, tree::class.simpleName ?: throw IllegalArgumentException("Cannot get tree type"))

        val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO ${treeName}Nodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
        tree.getKeyValueList()
            .forEach { saveNode(it.first, it.second.first, it.second.second, treeName, addNodeStatement) }
        addNodeStatement.close()
    }

    private fun saveNode(
        key: String,
        value: String,
        coordinate: Pair<Double, Double>,
        treeName: String,
        addNodeStatement: PreparedStatement
    ) {
        try {
            addNodeStatement.setString(1, key)
            addNodeStatement.setString(2, value)
            addNodeStatement.setDouble(3, coordinate.first)
            addNodeStatement.setDouble(4, coordinate.second)

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
                        "type varchar($maxStringLen));"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database")
        }
    }

    private fun createTableForTree(treeName: String) {
        validateName(treeName)

        try {
            executeQuery(
                "CREATE TABLE if not exists ${treeName}Nodes (nodeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "key varchar($maxStringLen), " +
                        "value varchar($maxStringLen), " +
                        "x INTEGER, " +
                        "y INTEGER);"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database")
        }
    }

    private fun executeQuery(query: String) {
        connection.createStatement().also { stmt ->
            try {
                stmt.execute(query)
            } catch (ex: Exception) {
                throw SQLException("Cannot execute query: \"$query\"")
            } finally {
                stmt.close()
            }
        }
    }

    private fun addTree(treeName: String, treeType: String) {
        try {
            addTreeStatement.setString(1, treeName)
            addTreeStatement.setString(2, treeType)

            addTreeStatement.execute()
        } catch (ex: Exception) {
            throw SQLException("Cannot add tree: $treeName")
        }
    }

    private fun getTreeType(treeName: String): String {
        val getTreeTypeStatement by lazy { connection.prepareStatement("SELECT trees.type FROM trees WHERE name = ?") }
        getTreeTypeStatement.setString(1, treeName)
        try {
            return getTreeTypeStatement.executeQuery().getString(1)
        } catch (ex: Exception) {
            throw SQLException("Cannot get tree type from database")
        } finally {
            getTreeTypeStatement.close()
        }

    }

    override fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Double, Double>>> {
        validateName(treeName)

        val nodes = "${treeName}Nodes"
        val getAllNodesStatement by lazy { connection.prepareStatement("SELECT $nodes.key as key, $nodes.value as value, $nodes.x as x, $nodes.y as y FROM $nodes;") }

        val tree = typeToTree(getTreeType(treeName))

        try {
            val nodesSet = getAllNodesStatement.executeQuery()
            while (nodesSet.next()) {
                tree.insert(
                    nodesSet.getString("key"),
                    Pair(
                        nodesSet.getString("value"),
                        Pair(nodesSet.getDouble("x"), nodesSet.getDouble("y"))
                    )
                )
            }
        } catch (ex: Exception) {
            throw IOException("Cannot get nodes from database")
        } finally {
            getAllNodesStatement.close()
        }

        return tree
    }

    override fun removeTree(treeName: String) {
        validateName(treeName)

        executeQuery("DROP TABLE IF EXISTS ${treeName}Nodes;")

        executeQuery("DELETE FROM trees WHERE name = '$treeName';")
    }

    override fun getAllTree(): List<Pair<String, String>> {
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
        for (i in getAllTree())
            removeTree(i.first)
    }
}
