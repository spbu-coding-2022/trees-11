package coordinateTreeWithStored

import BSTree
import interfaces.DataBase
import org.neo4j.driver.Session
import org.neo4j.driver.exceptions.value.Uncoercible
import java.io.IOException

class StoredCoordBSTree : BSTree<String, Pair<String, Pair<Double, Double>>> {
    constructor() : super()
    constructor(key: String, value: Pair<String, Pair<Double, Double>>) : super(key, value)
    constructor(vararg pairs: Pair<String, Pair<String, Pair<Double, Double>>>) : super(*pairs)

    inner class Neo4j(private val session: Session, private val treeName: String) : DataBase {
        override fun writeTree() {
            removeTree()
            session.run("CREATE (:$treeName)")
            var prevKey: String? = null
            breadthFirstSearch({ node -> saveNode(node, prevKey); prevKey = node?.key ?: prevKey })
        }

        private fun saveNode(node: BinNode<String, Pair<String, Pair<Double, Double>>>?, prevKey: String?) {
            if (node == null) return
            session.executeWrite { tx ->
                tx.run(
                    "OPTIONAL MATCH (prevNode:${if (prevKey == null) treeName else "${treeName}Node WHERE prevNode.key = '$prevKey'"})  " + "CREATE (prevNode)-[:next]->(b:${treeName}Node {key:\$key, value:\$value, x:\$x, y:\$y})",
                    mutableMapOf(
                        "key" to node.key,
                        "value" to node.value.first,
                        "x" to node.value.second.first,
                        "y" to node.value.second.second
                    ) as Map<String, Any>
                )
            }
        }

        override fun readTree() {
            session.executeRead { tx ->
                val result = tx.run(
                    "OPTIONAL MATCH ($treeName)-[n:next*]->(node)" + "RETURN node.key AS key, node.value AS value, node.x AS x, node.y AS y ORDER BY n"
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

        override fun removeTree() {
            session.run("OPTIONAL MATCH (tree:$treeName)-[:next*]->(node) DETACH DELETE node, tree")
        }
    }
}