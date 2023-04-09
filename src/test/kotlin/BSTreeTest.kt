import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.assertContains

class BSTreeTest {
    fun generateTreeWithInsert(vararg arr: Int): BinTree<Int, String> {
        val tree = BSTree<Int, String>()
        for (i in arr) tree.insert(i, "${i}k")
        return tree
    }

    fun keysToValues(vararg arr: Int, remove: Int? = null, chValue: Pair<Int, String>? = null): Array<String?> {
        return Array(arr.size) {
            if (arr[it] != remove) {
                if (arr[it] == chValue?.first) chValue.second else "${arr[it]}k"
            } else null
        }
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("insertTestsFactory")
    @DisplayName("insert-get simple tests")
    fun `insert-get simple tests`(arrKeys: Array<Int>, extraInsert: Pair<Int, String>?, name: String) {
        val tree = generateTreeWithInsert(*arrKeys.toIntArray())
        if (extraInsert != null) tree.insert(extraInsert.first, extraInsert.second)
        assertArrayEquals(keysToValues(*arrKeys.toIntArray(), chValue = extraInsert), tree.get(*arrKeys).toTypedArray())
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("removeTestsFactory")
    @DisplayName("remove tests")
    fun `remove tests`(arrKeys: Array<Int>, remove: Int, name: String) {
        val tree = generateTreeWithInsert(*arrKeys.toIntArray())
        tree.remove(remove)
        assertArrayEquals(keysToValues(*arrKeys.toIntArray(), remove = remove), tree.get(*arrKeys).toTypedArray())
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
                Arguments.of(arrayOf(4, 6, 5, 7), 5, "remove left leaf"),
                Arguments.of(arrayOf(4, 6, 5, 7), 7, "remove right leaf"),
                Arguments.of(arrayOf(4, 6, 5, 7), 4, "remove root node in non-degenerate tree"),
                Arguments.of(Array(0) { it }, 4, "remove in empty tree"),
                Arguments.of(arrayOf(2, 1, 3, 5), 4, "remove non-inserted node"),
                Arguments.of(Array(1000) { Random.nextInt() }, Random.nextInt(), "random remove")
            )
        }
    }

    @Nested
    inner class `constructors test` {
        @Test
        fun `insert key, value`() {
            val tree = BSTree(4, "4k")
            assertEquals("4k", tree.get(4))
        }

        @Test
        fun `insert two node`() {
            val tree = BSTree(Pair(4, "4k"), Pair(5, "5k"))
            assertArrayEquals(arrayOf("4k", "5k"), tree.get(4, 5).toTypedArray())
        }

        @Test
        fun `insert equal nodes`() {
            val tree = BSTree(Pair(4, "4k"), Pair(5, "5k"), Pair(4, "7k"))
            assertAll({ assertContains(arrayOf("4k", "7k"), tree.get(4)) }, { assertEquals("5k", tree.get(5)) })
        }
    }


    @Nested
    inner class `tests using debug` {
        @Test
        fun `insert three nodes test`() {
            assertEquals("2 \n1 3 \n", generateTreeWithInsert(2, 1, 3).Debug().treeKeysInString())
        }

        @Test
        fun `degenerate tree`() {
            assertEquals("1 \n- 2 \n- - - 3 \n", generateTreeWithInsert(1, 2, 3).Debug().treeKeysInString())
        }

        @Test
        fun `two inserts of node with equal keys`() {
            val tree = generateTreeWithInsert(5, 6, 4)
            tree.insert(4, "5k")
            assertEquals("5 \n4 6 \n", tree.Debug().treeKeysInString())
        }

        @Test
        fun `multiple removal`() {
            val tree = generateTreeWithInsert(10, 7, 15, 13, 17, 16, 18, 14, 12, 6, 9)
            tree.remove(15)
            assertEquals("10 \n7 16 \n6 9 13 17 \n- - - - 12 14 - 18 \n", tree.Debug().treeKeysInString())
            tree.remove(10)
            assertEquals("12 \n7 16 \n6 9 13 17 \n- - - - - 14 - 18 \n", tree.Debug().treeKeysInString())
            tree.remove(17)
            assertEquals("12 \n7 16 \n6 9 13 18 \n- - - - - 14 ", tree.Debug().treeKeysInString())
        }
    }


}
