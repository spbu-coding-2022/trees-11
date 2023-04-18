import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertContains

class AVLTreeTest {
    fun generateTreeWithInsert(vararg arr: Int): AVLTree<Int, String> {
        val tree = AVLTree<Int, String>()
        for (i in arr) tree.insert(i, "${i}k")
        return tree
    }

    companion object {
        @JvmStatic
        fun insertTestsFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(4), null, "insert one node test"),
                Arguments.of(arrayOf(4), Pair(4, "5k"), "two inserts with eq. keys of the first node"),
                Arguments.of(
                    arrayOf(4, 1, 5, 6),
                    Pair(4, "5k"),
                    "two inserts with eq. keys of the first node in non-degenerate tree"
                ),
                Arguments.of(arrayOf(5, 6, 4), Pair(4, "5k"), "two inserts with eq. keys of node"),
                Arguments.of(Array(1000) { Random.nextInt() }, Pair(Random.nextInt(), "random"), "random insert")
            )
        }

        @JvmStatic
        fun removeTestsFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(4), 4, "remove one root node"),
                Arguments.of(arrayOf(4, 6, 5, 7), 6, "remove non-root node"),
                Arguments.of(arrayOf(4, 6, 5, 7), 4, "remove left leaf"),
                Arguments.of(arrayOf(4, 6, 5, 7), 7, "remove right leaf"),
                Arguments.of(arrayOf(4, 6, 5, 7), 5, "remove root node in non-degenerate tree"),
                Arguments.of(Array(0) { it }, 4, "remove in empty tree"),
                Arguments.of(arrayOf(2, 1, 3, 5), 4, "remove non-inserted node"),
                Arguments.of(Array(1000) { Random.nextInt() }, Random.nextInt(), "random remove")
            )
        }

        @JvmStatic
        fun debugTestsFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(4, 2, 1), "2 \n1 4 \n", "right rotation"),
                Arguments.of(arrayOf(2, 5, 4, 3, 1, 0), "2 \n1 4 \n0 - 3 5 \n", "difficult right rotation 1"),
                Arguments.of(arrayOf(2, 5, 4, 3, 0, 1), "2 \n0 4 \n- 1 3 5 \n", "difficult right rotation 2"),
                Arguments.of(arrayOf(2, 1, 4, 3, 5, 6), "4 \n2 5 \n1 3 - 6 \n", "difficult left rotation 1"),
                Arguments.of(arrayOf(2, 1, 4, 3, 6, 5), "4 \n2 6 \n1 3 5 ", "difficult left rotation 2"),
                Arguments.of(arrayOf(1, 3, 2), "2 \n1 3 \n", "right left rotation"),
                Arguments.of(arrayOf(3, 1, 2), "2 \n1 3 \n", "left right rotation"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), "6 \n4 8 \n2 5 7 - \n1 3 ", "combine"),
            )
        }

        @JvmStatic
        fun debugRemoveTestsFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 1, "6 \n4 8 \n2 5 7 - \n- 3 ", "remove right leaf"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 3, "6 \n4 8 \n2 5 7 - \n1 ", "remove left leaf"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 2, "6 \n4 8 \n3 5 7 - \n1 ", "remove root of two leafs"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 8, "4 \n2 6 \n1 3 5 7 \n", "remove with rebalancing"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 6, "4 \n2 7 \n1 3 5 8 \n", "remove root"),
                Arguments.of(arrayOf(4, 3, 6, 5, 8, 7, 1, 2), 4, "6 \n2 8 \n1 5 7 - \n- - 3 ", "one more remove"),
            )
        }
    }

    @ParameterizedTest(name = "{2} ({0}, {1})")
    @MethodSource("insertTestsFactory")
    @DisplayName("insert-get simple tests")
    fun `insert-get simple tests`(arrKeys: Array<Int>, extraInsert: Pair<Int, String>?, name: String) {
        val tree = generateTreeWithInsert(*arrKeys.toIntArray())
        if (extraInsert != null) tree.insert(extraInsert.first, extraInsert.second)
        Assertions.assertArrayEquals(
            keysToValues(*arrKeys.toIntArray(), chValue = extraInsert), tree.get(*arrKeys).toTypedArray()
        )
    }

    @ParameterizedTest(name = "{2} ({0}, {1})")
    @MethodSource("removeTestsFactory")
    @DisplayName("remove tests")
    fun `remove tests`(arrKeys: Array<Int>, remove: Int, name: String) {
        val tree = generateTreeWithInsert(*arrKeys.toIntArray())
        tree.remove(remove)
        Assertions.assertArrayEquals(
            keysToValues(*arrKeys.toIntArray(), remove = remove), tree.get(*arrKeys).toTypedArray()
        )
    }

    @ParameterizedTest(name = "{2} ({0})")
    @MethodSource("debugTestsFactory")
    @DisplayName("insert tests using debug")
    fun testsWithDebug(keys: Array<Int>, treeInString: String, name: String) {
        val tree = generateTreeWithInsert(*keys.toIntArray())
        Assertions.assertEquals(treeInString, tree.Debug().treeKeysInString())
    }

    @ParameterizedTest(name = "{3} ({0}, {1})")
    @MethodSource("debugRemoveTestsFactory")
    @DisplayName("remove tests using debug")
    fun removeTestsWithDebug(keys: Array<Int>, remove: Int, treeInString: String, name: String) {
        val tree = generateTreeWithInsert(*keys.toIntArray())
        tree.remove(remove)
        Assertions.assertEquals(treeInString, tree.Debug().treeKeysInString())
    }

    @Nested
    @DisplayName("constructors test")
    inner class ConstructorsTest {
        @Test
        fun `insert key, value`() {
            val tree = AVLTree(4, "4k")
            Assertions.assertEquals("4k", tree.get(4))
        }

        @Test
        fun `insert two node`() {
            val tree = AVLTree(Pair(4, "4k"), Pair(5, "5k"))
            Assertions.assertArrayEquals(arrayOf("4k", "5k"), tree.get(4, 5).toTypedArray())
        }

        @Test
        fun `insert equal nodes`() {
            val tree = AVLTree(Pair(4, "4k"), Pair(5, "5k"), Pair(4, "7k"))
            Assertions.assertAll({ assertContains(arrayOf("4k", "7k"), tree.get(4)) },
                { Assertions.assertEquals("5k", tree.get(5)) })
        }
    }


    @Test
    fun `my struct`() {
        class My (
            val arg1: String
        ) : Comparable<My> {
            override fun compareTo(other: My): Int = arg1.compareTo(other.arg1)
        }

        val tree = AVLTree(Pair(My("11"), 1), Pair(My("111"), 111), Pair(My("321"), 321))
        tree.remove(My("321"))
        Assertions.assertAll({ Assertions.assertEquals(1, tree.get(My("11"))) },
            { Assertions.assertEquals(111, tree.get(My("111"))) },
            { Assertions.assertNull(tree.get(My("321"))) })
    }
}
