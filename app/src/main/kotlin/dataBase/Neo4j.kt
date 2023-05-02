package dataBase

import trees.BinTree
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import org.neo4j.driver.exceptions.ServiceUnavailableException
import org.neo4j.driver.exceptions.value.Uncoercible
import java.io.IOException

class Neo4j(uri: String, user: String, password: String) : DataBase {
    private var driver: Driver
    private var session: Session

    init {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
            session = driver.session()
        } catch (ex: Exception) {
            throw IOException("can't start session, try to change uri, user name or password\n$ex")
        }
    }

    private fun executeQuery(query: String) {
        try {
            session.run(query)
        } catch (ex: ServiceUnavailableException) {
            throw IOException(
                "Cannot connect to Neo4j database\n" +
                        "Check that Neo4j is running and that all the data in the app/src/main/resources/Neo4j.properties file is correct\n" +
                        "$ex"
            )
        }
    }

    override fun saveTree(
        treeName: String,
        tree: BinTree<String, Pair<String, Pair<Float, Float>>>,
        viewCoordinates: Pair<Float, Float>
    ) {
        if (!isSupportTreeType(tree)) throw IllegalArgumentException("Unsupported tree type")
        validateName(treeName)

        removeTree(treeName)
        executeQuery("CREATE (:Tree {name: '$treeName', type: '${tree::class.simpleName}', " +
                "viewX: ${viewCoordinates.first}, viewY: ${viewCoordinates.second}})")
        var prevKey: String? = null
        tree.getKeyValueList()
            .forEach { saveNode(it.first, it.second.first, it.second.second, prevKey, treeName); prevKey = it.first }
    }

    private fun saveNode(
        key: String,
        value: String,
        coordinate: Pair<Float, Float>,
        prevKey: String?,
        treeName: String
    ) {
        session.executeWrite { tx ->
            tx.run(
                "OPTIONAL MATCH (prevNode:${if (prevKey == null) "Tree WHERE prevNode.name = '$treeName'" else "${treeName}Node WHERE prevNode.key = '$prevKey'"})  " +
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

    override fun readTree(treeName: String): Pair<BinTree<String, Pair<String, Pair<Float, Float>>>, Pair<Float, Float>> {
        validateName(treeName)

        var type = ""
        var viewCoordinates = Pair(0F, 0F)
        session.executeRead { tx ->
            val result = tx.run("OPTIONAL MATCH (tree: Tree WHERE tree.name = '$treeName') RETURN tree.type AS type, tree.viewX AS x, tree.viewY AS y").single()
            try {
                type = result["type"].asString()
                viewCoordinates = Pair(result["x"].asFloat(), result["y"].asFloat())
            } catch (ex: Uncoercible) {
                throw IOException("Corrupted data in the database.\nPossible solution: Clear the data.\n$ex")
            } catch (ex: Exception) {
                throw IOException("Cannot get or recognise data\n$ex")
            }
        }

        val tree = typeToTree(type)

        session.executeRead { tx ->
            val result = tx.run(
                "OPTIONAL MATCH (tree: Tree WHERE tree.name = '$treeName')-[n:next*]->(node) RETURN node.key AS key, node.value AS value, node.x AS x, node.y AS y ORDER BY n"
            )

            result.stream().forEach {
                try {
                    tree.insert(
                        it["key"].asString(),
                        Pair(
                            it["value"].asString(),
                            Pair(it["x"].asFloat(), it["y"].asFloat())
                        )
                    )
                } catch (ex: Uncoercible) {
                    throw IOException("Corrupted data in the database.\nPossible solution: Clear the data.\n$ex")
                } catch (ex: Exception) {
                    throw IOException("Cannot get or recognise data\n$ex")
                }
            }
        }
        return Pair(tree, viewCoordinates)
    }

    override fun removeTree(treeName: String) {
        validateName(treeName)

        executeQuery("OPTIONAL MATCH (tree: Tree WHERE tree.name = '$treeName')-[:next*]->(node) DETACH DELETE node, tree")
    }

    override fun getAllTrees(): List<Triple<String, String, Pair<Float, Float>>> {
        val list = mutableListOf<Triple<String, String, Pair<Float, Float>>>()
        session.executeRead { tx ->
            val result = tx.run("OPTIONAL MATCH (tree: Tree) RETURN tree.name AS name, tree.type AS type, tree.viewX AS x, tree.viewY AS y")
            result.stream().forEach {
                list.add(Triple(it["name"].asString(), it["type"].asString(), Pair(it["x"].asFloat(), it["y"].asFloat())))
            }
        }
        return list
    }

    override fun close() {
        session.close()
        driver.close()
    }

    override fun clean() {
        executeQuery("MATCH (n) DETACH DELETE n")
    }
}
