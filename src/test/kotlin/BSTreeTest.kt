import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import org.junit.jupiter.api.assertAll

class BSTreeTest {
    fun generateTreeWithInsert(vararg arr: Int): BinTree<Int, String> {
        val tree = BSTree<Int, String>()
        for (i in arr) tree.insert(i, "${i}k")
        return tree
    }

    @Nested
    inner class `Test insert` {
        @Test
        fun `insert one node test`() {
            val expected = "4k"
            assertEquals(expected, generateTreeWithInsert(4).get(4))
        }

        @Nested
        inner class `Equal key` {
            @Test
            fun `two inserts of the first node`() {
                val tree = generateTreeWithInsert(4)
                tree.insert(4, "5k")
                assertEquals("5k", tree.get(4))
            }

            @Test
            fun `two inserts of the first node in non-degenerate tree`() {
                val tree = generateTreeWithInsert(4, 1, 5, 6)
                tree.insert(4, "5k")
                assertEquals("5k", tree.get(4))
            }


            @Test
            fun `two inserts of node`() {
                val tree = generateTreeWithInsert(5, 6, 4)
                tree.insert(4, "5k")
                assertEquals("5k", tree.get(4))
            }
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
    inner class `remove node`() {
        @Test
        fun `remove one root node`() {
            val tree = generateTreeWithInsert(4)
            tree.remove(4)
            assertNull(tree.get(4))
        }

        @Test
        fun `remove non-root node`() {
            val tree = generateTreeWithInsert(4, 6, 5, 7)
            tree.remove(6)
            assertAll({ assertNull(tree.get(6)) },
                { assertArrayEquals(arrayOf("4k", "5k", "7k"), tree.get(4, 5, 7).toTypedArray()) })
        }

        @Test
        fun `remove left leaf`() {
            val tree = generateTreeWithInsert(4, 6, 5, 7)
            tree.remove(5)
            assertAll({ assertNull(tree.get(5)) },
                { assertArrayEquals(arrayOf("4k", "6k", "7k"), tree.get(4, 6, 7).toTypedArray()) })
        }

        @Test
        fun `remove right leaf`() {
            val tree = generateTreeWithInsert(4, 6, 5, 7)
            tree.remove(7)
            assertAll({ assertNull(tree.get(7)) },
                { assertArrayEquals(arrayOf("4k", "6k", "5k"), tree.get(4, 6, 5).toTypedArray()) })
        }

        @Test
        fun `remove root node in non-degenerate tree`() {
            val tree = generateTreeWithInsert(4, 6, 5, 7)
            tree.remove(4)
            assertAll({ assertNull(tree.get(4)) },
                { assertArrayEquals(arrayOf("5k", "6k", "7k"), tree.get(5, 6, 7).toTypedArray()) })
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
