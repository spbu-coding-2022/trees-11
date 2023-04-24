package dataBase

import BinTree
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.extension

class Json(private val dirName: String) : DataBase {
    private val mapper = jacksonObjectMapper()

    init {
        if (dirName.last() != '\\' && dirName.last() != '/') throw IllegalArgumentException("Please, don't use '/' or '\\' in the end of dir path")
    }

    private fun getFile(treeName: String) = try {
        File("${dirName}${treeName}/.json")
    } catch (ex: Exception) {
        throw IOException("cannot get file with name: ${dirName}${treeName}/.json")
    }

    override fun saveTree(
        treeName: String, tree: BinTree<String, Pair<String, Pair<Double, Double>>>, treeType: String
    ) {
        if (isSupportTreeType(treeName)) throw IllegalArgumentException("Unsupported tree type")
        if (treeName.isEmpty()) throw IllegalArgumentException("Incorrect tree name")
        removeTree(treeName)
        val jsonFile = getFile(treeName)
        jsonFile.createNewFile()
        jsonFile.appendText(mapper.writeValueAsString(Pair(treeName, treeType)))
        jsonFile.appendText(mapper.writeValueAsString(tree.getKeyValueList()))
    }

    override fun readTree(treeName: String): BinTree<String, Pair<String, Pair<Double, Double>>> {
        val jsonFile = getFile(treeName)

        val treeParams = mapper.readValue<Pair<String, String>>(jsonFile)
        val tree = typeToTree(treeParams.second)
        tree.insert(*mapper.readValue(jsonFile))
        return tree
    }

    override fun removeTree(treeName: String) {
        getFile(treeName).delete()
    }

    private fun forAllJsonFile(function: (File) -> Unit) {
        Files.walk(Paths.get(dirName)).use { path ->
            path.filter { Files.isRegularFile(it) && Files.isWritable(it) && (it.extension == "json") }
                .forEach { function(it.toFile()) }
        }
    }

    override fun getAllTree(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        forAllJsonFile { list.add(mapper.readValue(it)) }

        return list
    }

    override fun clean() {
        forAllJsonFile { it.delete() }
    }

    override fun close() {
    }
}
