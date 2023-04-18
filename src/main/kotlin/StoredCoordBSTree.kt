import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.exceptions.value.Uncoercible
import java.io.Closeable
import java.io.IOException

class StoredCoordBSTree : BSTree<String, Pair<String, Pair<Double, Double>>> {
    constructor() : super()
    constructor(key: String, value: Pair<String, Pair<Double, Double>>) : super(key, value)
    constructor(vararg pairs: Pair<String, Pair<String, Pair<Double, Double>>>) : super(*pairs)

    inner class neo4j(uri: String, user: String, password: String) : Closeable {
        private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
        private val session = driver.session()

        fun addTree() {
            cleanDB()
            breadthFirstSearch({ node -> addNode(node) })
        }

        private fun addNode(node: BinNode<String, Pair<String, Pair<Double, Double>>>?) {
            if (node == null) return
            session.executeWrite { tx ->
                tx.run(
                    "CREATE (:Node{key:\$key, value:\$value, x:\$x, y:\$y}) ", mutableMapOf(
                        "key" to node.key,
                        "value" to node.value.first,
                        "x" to node.value.second.first,
                        "y" to node.value.second.second
                    ) as Map<String, Any>
                )
            }
        }

        fun readTree() {
            session.executeRead { tx ->
                val result = tx.run(
                    "MATCH (node: Node) " + "RETURN node.key AS key, node.value AS value, node.x AS x, node.y AS y ORDER BY node.id"
                )
                result.stream().forEach {
                    try {
                        insert(
                            it["key"].asString(),
                            Pair(it["value"].asString(), Pair(it["x"].asDouble(), it["y"].asDouble()))
                        )
                    } catch (e: Uncoercible) {
                        throw IOException("Corrupted data in the database.\n Possible solution: Clear the data.")
                    }
                }
            }
        }

        fun cleanDB() {
            session.run("MATCH (n) DETACH DELETE n")
        }

        override fun close() {
            session.close()
            driver.close()
        }
    }
}
