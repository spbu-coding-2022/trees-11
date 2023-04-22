package storedTreeWithCoordinate

import AVLTree
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dataBase.DataBase
import java.io.File

class StoredCoordAVLTree: AVLTree<String, Pair<String, Pair<Double, Double>>> {
    constructor() : super()
    constructor(key: String, value: Pair<String, Pair<Double, Double>>) : super(key, value)
    constructor(vararg pairs: Pair<String, Pair<String, Pair<Double, Double>>>) : super(*pairs)

    inner class Json(private val fileName: String) : DataBase {
        val jsonFile = File(fileName)
        val mapper = jacksonObjectMapper()

        override fun writeTree() {
            removeTree()
            jsonFile.createNewFile()
            val nodeList = mutableListOf<Pair<String, Pair<String, Pair<Double, Double>>>>()
            breadthFirstSearch({ node -> if (node != null) nodeList.add(Pair(node.key, node.value))})
            jsonFile.appendText(mapper.writeValueAsString(nodeList))
        }

        override fun readTree() {
            insert(*mapper.readValue(jsonFile))
        }

        override fun removeTree() {
            jsonFile.delete()
        }
    }
}