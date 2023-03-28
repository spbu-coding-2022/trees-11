import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BSTreeTest {
    @Nested
    inner class `Test insert` {
        fun generateTreeWithInsert(vararg arr: Int): BinTree<Int, String> {
            val tree = BSTree<Int, String>()
            for (i in arr)
                tree.insert(i, "${i}k")
            return tree
        }

        @Test
        fun `insert one node test`() {
            val expected = "4k"
            assertEquals(expected, generateTreeWithInsert(4).get(4))
        }

        @Test
        fun `insert three nodes test`() {
            assertEquals("2 \n1 3 \n", generateTreeWithInsert(2, 1, 3).Debug().treeKeysInString())
        }

        @Test
        fun `degenerate tree`() {
            assertEquals("1 \n- 2 \n- - - 3 \n", generateTreeWithInsert(1, 2, 3).Debug().treeKeysInString())
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
                assertEquals("5 \n4 6 \n", tree.Debug().treeKeysInString())
                assertEquals("5k", tree.get(4))
            }
        }
    }
}
