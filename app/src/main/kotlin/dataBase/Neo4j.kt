package dataBase

import BinTree
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import org.neo4j.driver.exceptions.value.Uncoercible
import java.io.IOException

class Neo4j(uri: String, user: String, password: String) : DataBase {
    private var driver: Driver
    private var session: Session

    init {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
            session = driver.session()
        } catch (e: IllegalArgumentException) {
            throw IOException("can't start session, try to change uri, user name or password")
        }
    }

    override fun saveTree(treeName: String, tree: BinTree<String, Pair<String, Pair<Double, Double>>>, treeType: String) {
        if (isSupportTreeType(treeName)) throw IllegalArgumentException("Unsupported tree type")
        if (treeName.isEmpty()) throw IllegalArgumentException("Incorrect tree name")
        removeTree(treeName)
        session.run("CREATE (:Tree {name: \$$treeName}, type: \$$treeType)")
        var prevKey: String? = null
        tree.getKeyValueList().forEach { saveNode(it.first, it.second.first, it.second.second, prevKey, treeName); prevKey = it.first }
    }

    private fun saveNode(key: String, value: String, coordinate: Pair<Double, Double>, prevKey: String?, treeName: String) {
        session.executeWrite { tx ->
            tx.run(
                "OPTIONAL MATCH (prevNode:${if (prevKey == null) "Tree WHERE prevNode.name = $treeName" else "${treeName}Node WHERE prevNode.key = '$prevKey'"})  " +
                        "CREATE (prevNode)-[:next]->(b:${treeName}Node {key:\$key, value:\$value, x:\$x, y:\$y})",
                mutableMapOf(
                    "key" to key,
                    "value" to value,
                    "x" to coordinate.first,
                    "y" to coordinate.second
                ) as Map<String, Any>
            )
        }
    }

    override fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Double, Double>>> {
        var type = ""
        session.executeRead { tx ->
            type = tx.run("OPTIONAL MATCH (tree:Tree WHERE tree.name = $treeName) RETURN tree.type").toString()
        }

        val tree = typeToTree(type.toString())

        session.executeRead { tx ->
            val result = tx.run(
                "OPTIONAL MATCH (tree:Tree WHERE tree.name = $treeName)-[n:next*]->(node) RETURN node.key AS key, node.value AS value, node.x AS x, node.y AS y ORDER BY n"
            )

            result.stream().forEach {
                try {
                    tree.insert(
                        it["key"].asString(),
                        Pair(it["value"].asString(),
                        Pair(it["x"].asDouble(), it["y"].asDouble()))
                    )
                } catch (e: Uncoercible) {
                    throw IOException("Corrupted data in the database.\n Possible solution: Clear the data.")
                }
            }
        }
        return tree
    }

    override fun removeTree(treeName: String) {
        session.run("OPTIONAL MATCH (tree: Tree WHERE tree.name = $treeName)-[:next*]->(node) DETACH DELETE node, tree")
    }

    override fun getAllTree(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        session.executeRead { tx ->
            val result = tx.run("OPTIONAL MATCH (tree: Tree) RETURN tree.name AS name, tree.type AS type")
            result.stream().forEach {
                list.add(Pair(it["name"].asString(), it["type"].asString()))
            }
        }
        return list
    }

    override fun close() {
        session.close()
        driver.close()
    }

    override fun clean() {
        session.run("MATCH (n) DETACH DELETE n")
    }
}
