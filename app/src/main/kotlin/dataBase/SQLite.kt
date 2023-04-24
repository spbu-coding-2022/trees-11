package dataBase

import trees.BinTree
import java.io.IOException
import java.sql.DriverManager
import java.sql.SQLException

class SQLite (dbPath: String): DataBase  {
    companion object {
        private const val MAX_STRING_LEN = 255 //need to take out to properties
        private const val DB_DRIVER = "jdbc:sqlite"
    }

    private val connection = DriverManager.getConnection("$DB_DRIVER:$dbPath")
        ?: throw SQLException("Cannot connect to database")
    private val addTreeStatement by lazy { connection.prepareStatement("INSERT INTO trees (name, type) VALUES (?, ?);") }
    private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO ?Nodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
    private val getAllTreesStatement by lazy { connection.prepareStatement("SELECT trees.name as name, trees.type as type FROM trees;") }

    override fun saveTree(treeName: String, tree: BinTree<String, Pair<String, Pair<Double, Double>>>, treeType: String) {
        if (isSupportTreeType(treeName)) throw IllegalArgumentException("Unsupported tree type")
        if (treeName.isEmpty()) throw IllegalArgumentException("Incorrect tree name")
        createTreesTable()
        createTableForTree(treeName)
        removeTree(treeName)
        addTree(treeName, treeType)
        tree.getKeyValueList().forEach{ saveNode(it.first, it.second.first, it.second.second, treeName) }
    }

    private fun saveNode(
        key: String,
        value: String,
        coordinate: Pair<Double, Double>,
        treeName: String
    ) {
        try {
            addNodeStatement.setString(1, treeName)
            addNodeStatement.setString(2, key)
            addNodeStatement.setString(3, value)
            addNodeStatement.setDouble(4, coordinate.first)
            addNodeStatement.setDouble(5, coordinate.second)

            addNodeStatement.execute()
        } catch (ex: Exception) {
            throw SQLException("Cannot add node with key: \"${key}\" in tree: $treeName")
        }
    }

    private fun createTreesTable() {
        try {
            executeQuery("CREATE TABLE if not exists trees (treeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name varchar($MAX_STRING_LEN), " +
                    "type varchar($MAX_STRING_LEN);"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database")
        }
    }

    private fun createTableForTree(treeName: String) {
        try {
            executeQuery(
                "CREATE TABLE if not exists ${treeName}Nodes (nodeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        "key varchar($MAX_STRING_LEN), " +
                        "value varchar($MAX_STRING_LEN), " +
                        "x INTEGER, " +
                        "y INTEGER);"
            )
        } catch (ex: Exception) {
            throw SQLException("Cannot create table in database")
        }
    }

    private fun executeQuery(query: String) =
        connection.createStatement().also { stmt ->
            try {
                stmt.execute(query)
            } catch (ex: Exception) {
                throw SQLException("Cannot execute query: \"$query\"")
            } finally {
                stmt.close()
            }
        }

    private fun addTree(treeName: String, treeType: String) {
        try {
            addTreeStatement.setString(1, treeName)
            addTreeStatement.setString(2, treeType)

            addTreeStatement.execute()
        } catch(ex: Exception) {
            throw SQLException("Cannot add tree: $treeName")
        }
    }

    private fun getTreeType(treeName: String) = executeQuery("SELECT tree FROM trees WHERE tree.name = $treeName").toString()

    override fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Double, Double>>> {
        val nodes = "${treeName}Nodes"
        val getAllNodesStatement by lazy { connection.prepareStatement("SELECT $nodes.key as key, $nodes.value as value, $nodes.x as x, $nodes.y as y FROM $nodes;") }

        val tree = typeToTree(getTreeType(treeName))

        try {
            val nodesSet = getAllNodesStatement.executeQuery()
            while (nodesSet.next()) {
                tree.insert(
                    nodesSet.getString("key"),
                    Pair(nodesSet.getString("value"),
                        Pair(nodesSet.getDouble("x"), nodesSet.getDouble("y"))
                    )
                )
            }
        } catch (ex: Exception) {
            throw  IOException("Cannot get nodes from database")
        }

        return tree
    }

    override fun removeTree(treeName: String) {
        executeQuery("DELETE FROM ${treeName}Nodes; DELETE FROM trees WHERE trees.name = $treeName")
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
        addNodeStatement.close()
        getAllTreesStatement.close()
        connection.close()
    }

    override fun clean() {
        executeQuery("DROP DATABASE;")
    }
}
