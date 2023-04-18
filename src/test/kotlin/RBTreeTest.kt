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

class RBTreeTest {
    private fun generateTreeWithInsert(vararg arr: Int): RBTree<Int, String> {
        val tree = RBTree<Int, String>()
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
                Arguments.of(arrayOf(4), "4 \n", "insert root"),
                Arguments.of(
                    arrayOf(5, 6, 3, 4, 1, 2), "5 \n3 6 \n1 4 - - \n- 2 ", "grandfather isn't root, uncle red"
                ),
                Arguments.of(arrayOf(6, 3, 8, 4), "6 \n3 8 \n- 4 ", "grandfather root, red uncle)"),
                Arguments.of(arrayOf(6, 4, 5), "5 \n4 6 \n", "zigzag, null uncle"),
                Arguments.of(arrayOf(5, 4, 3), "4 \n3 5 \n", "straight line, null uncle"),
                Arguments.of(arrayOf(8, 9, 5, 6, 3, 2, 4, 1), "5 \n3 8 \n2 4 6 9 \n1 ", "change color, right rotation"),
                Arguments.of(arrayOf(8, 9, 5, 6, 3, 1, 2), "8 \n5 9 \n2 6 - - \n1 3 ", "two rotation"),
            )
        }

        @JvmStatic
        fun debugRemoveTestsFactory(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(arrayOf(4, 2, 5, 3), 3, "4 \n2 5 \n", "remove red leaf"),
                Arguments.of(arrayOf(4, 2, 5, 3), 2, "4 \n3 5 \n", "remove black with red child"),
                Arguments.of(
                    arrayOf(5, 2, 8, 7, 9, 10, 6), 2, "8 \n6 9 \n5 7 - 10 \n", "remove left black with red brother 1"
                ),
                Arguments.of(
                    arrayOf(5, 2, 8, 10, 6, 7, 9), 2, "8 \n6 10 \n5 7 9 ", "remove left black with red brother 2"
                ),
                Arguments.of(
                    arrayOf(5, 2, 8, 1, 3, 0, 4), 8, "2 \n1 4 \n0 - 3 5 \n", "remove right black with red brother 1"
                ),
                Arguments.of(
                    arrayOf(5, 2, 8, 0, 4, 1, 3), 8, "2 \n0 4 \n- 1 3 5 \n", "remove right black with red brother 2"
                ),
                Arguments.of(arrayOf(5, 2, 8, 7, 9), 2, "8 \n5 9 \n- 7 ", "remove black with black brother right"),
                Arguments.of(arrayOf(5, 2, 8, 1, 3), 8, "2 \n1 5 \n- - 3 ", "remove black with black brother left"),
                Arguments.of(
                    arrayOf(3, 1, 9, 7, 11, 5, 8),
                    7,
                    "3 \n1 9 \n- - 8 11 \n- - - - 5 ",
                    "remove node with two red children"
                ),
            )
        }
    }

    @ParameterizedTest(name = "{2} ({1}, {2})")
    @MethodSource("insertTestsFactory")
    @DisplayName("insert-get simple tests")
    fun `insert-get simple tests`(arrKeys: Array<Int>, extraInsert: Pair<Int, String>?, name: String) {
        val tree = generateTreeWithInsert(*arrKeys.toIntArray())
        if (extraInsert != null) tree.insert(extraInsert.first, extraInsert.second)
        Assertions.assertArrayEquals(
            keysToValues(*arrKeys.toIntArray(), chValue = extraInsert), tree.get(*arrKeys).toTypedArray()
        )
    }

    @ParameterizedTest(name = "{2}, ({0}, {1})")
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

    @ParameterizedTest(name = "{3} ({0}, {1}")
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
            val tree = RBTree(4, "4k")
            Assertions.assertEquals("4k", tree.get(4))
        }

        @Test
        fun `insert two node`() {
            val tree = RBTree(Pair(4, "4k"), Pair(5, "5k"))
            Assertions.assertArrayEquals(arrayOf("4k", "5k"), tree.get(4, 5).toTypedArray())
        }

        @Test
        fun `insert equal nodes`() {
            val tree = RBTree(Pair(4, "4k"), Pair(5, "5k"), Pair(4, "7k"))
            Assertions.assertAll({ assertContains(arrayOf("4k", "7k"), tree.get(4)) },
                { Assertions.assertEquals("5k", tree.get(5)) })
        }
    }


    @Test
    fun `my struct`() {
        class My(
            val arg1: String
        ) : Comparable<My> {
            override fun compareTo(other: My): Int = arg1.compareTo(other.arg1)
        }

        val tree = RBTree(Pair(My("11"), 1), Pair(My("111"), 111), Pair(My("321"), 321))
        tree.remove(My("321"))
        Assertions.assertAll({ Assertions.assertEquals(1, tree.get(My("11"))) },
            { Assertions.assertEquals(111, tree.get(My("111"))) },
            { Assertions.assertNull(tree.get(My("321"))) })
    }
}
