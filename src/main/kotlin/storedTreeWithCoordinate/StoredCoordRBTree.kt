package storedTreeWithCoordinate

import RBTree
import abstractTree.BinTree
import interfaces.DataBase
import java.io.IOException
import java.sql.DriverManager
import java.sql.SQLException

class StoredCoordRBTree: RBTree<String, Pair<String, Pair<Double, Double>>> {
    constructor() : super()
    constructor(key: String, value: Pair<String, Pair<Double, Double>>) : super(key, value)
    constructor(vararg pairs: Pair<String, Pair<String, Pair<Double, Double>>>) : super(*pairs)

    inner class SQLite(private val dbPath: String) : DataBase {
        private val connection = DriverManager.getConnection("$DB_DRIVER:$dbPath")
            ?: throw SQLException("Cannot connect to database")
        private val addNodeStatement by lazy { connection.prepareStatement("INSERT INTO nodes (key, value, x, y) VALUES (?, ?, ?, ?);") }
        private val getAllNodesStatement by lazy { connection.prepareStatement("SELECT nodes.key as key, nodes.value as value, nodes.x as x, nodes.y as y FROM nodes;") }

        fun createDb() {
            connection.createStatement()
                .also { stmt ->
                    try {
                        stmt.execute("CREATE TABLE if not exists nodes (nodeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                                "key varchar(255), " +
                                "value varchar(255), " +
                                "x INTEGER, " +
                                "y INTEGER);"
                        )
                    } catch (ex: Exception) {
                        throw IOException("Cannot create table in database")
                    } finally {
                        stmt.close()
                    }
                }
        }

        fun executeQuery(query: String) {
            connection.createStatement().also { stmt ->
                try {
                    stmt.execute(query)
                } catch (ex: Exception) {
                    createDb()
                } finally {
                    stmt.close()
                }
            }
        }

        override fun writeTree() {
            removeTree()
            breadthFirstSearch({ node -> saveNode(node)})
        }

        private fun saveNode(node: BinTree.BinNode<String, Pair<String, Pair<Double, Double>>>?) {
            if (node != null)
                try {
                    addNodeStatement.setString(1, node.key)
                    addNodeStatement.setString(2, node.value.first)
                    addNodeStatement.setDouble(3, node.value.second.first)
                    addNodeStatement.setDouble(4, node.value.second.second)

                    addNodeStatement.execute()
                } catch (ex: Exception) {
                    throw IOException("Cannot add user: ${node.key}")
                }
            }

        override fun readTree() {
            try {
                val resSet = getAllNodesStatement.executeQuery()
                while (resSet.next()) {
                    insert(
                        resSet.getString("key"),
                        Pair(resSet.getString("value"),
                            Pair(resSet.getDouble("x"), resSet.getDouble("y"))
                        )
                    )
                }
            } catch (ex: Exception) {
                throw  IOException("Cannot get nodes from database")
            }
        }

        override fun removeTree() {
            executeQuery("DELETE FROM nodes;")
        }

        fun close() {
            addNodeStatement.close()
            getAllNodesStatement.close()
            connection.close()
        }
    }

    companion object {
        private const val DB_DRIVER = "jdbc:sqlite"
    }
}