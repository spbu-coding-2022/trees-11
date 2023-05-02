package dataBase

import trees.BinTree
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.extension

class Json(private val saveDirPath: String) : DataBase {
    private val mapper = jacksonObjectMapper()

    init {
        if (saveDirPath.last() == '\\' || saveDirPath.last() == '/') throw IllegalArgumentException("Please, don't use '/' or '\\' in the end of dir path")
        File(saveDirPath).mkdirs()
    }

    private fun getFile(treeName: String) = try {
        File("${saveDirPath}/${treeName}.json")
    } catch (ex: Exception) {
        throw IOException("cannot get file with name: ${saveDirPath}/${treeName}.json\n$ex")
    }

    override fun saveTree(
        treeName: String, tree: BinTree<String, Pair<String, Pair<Float, Float>>>, viewCoordinates: Pair<Float, Float>
    ) {
        if (!isSupportTreeType(tree)) throw IllegalArgumentException("Unsupported tree type")
        validateName(treeName)

        removeTree(treeName)

        val jsonFile = getFile(treeName)
        jsonFile.createNewFile()

        jsonFile.appendText(
            mapper.writeValueAsString(
                Pair(
                    Triple(treeName, tree::class.simpleName, viewCoordinates),
                    tree.getKeyValueList()
                )
            )
        )
    }

    override fun readTree(treeName: String): Pair<BinTree<String, Pair<String, Pair<Float, Float>>>, Pair<Float, Float>> {
        validateName(treeName)

        val jsonFile = getFile(treeName)

        val readTree =
            mapper.readValue<Pair<Triple<String, String, Pair<Float, Float>>, Array<Pair<String, Pair<String, Pair<Float, Float>>>>>>(
                jsonFile
            )
        val tree = typeToTree(readTree.first.second)
        tree.insert(*readTree.second)
        return Pair(tree, readTree.first.third)
    }

    override fun removeTree(treeName: String) {
        validateName(treeName)

        getFile(treeName).delete()
    }

    private fun forAllJsonFile(function: (File) -> Unit) {
        Files.walk(Paths.get(saveDirPath)).use { path ->
            path.filter { Files.isRegularFile(it) && Files.isWritable(it) && (it.extension == "json") }
                .forEach { function(it.toFile()) }
        }
    }

    override fun getAllTrees(): MutableList<Triple<String, String, Pair<Float, Float>>> {
        val list = mutableListOf<Triple<String, String, Pair<Float, Float>>>()
        forAllJsonFile {
            list.add(
                mapper.readValue<Pair<Triple<String, String, Pair<Float, Float>>, Array<Pair<String, Pair<String, Pair<Float, Float>>>>>>(
                    it
                ).first
            )
        }

        return list
    }

    override fun clean() {
        forAllJsonFile { it.delete() }
    }

    override fun close() {
    }
}
