import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BSTreeTest {
    @Nested
    inner class `Test add` {
        fun generateTreeWithAdd(vararg arr: Int): BinTree<Int, String> {
            val tree = BSTree<Int, String>()
            for (i in arr)
                tree.insert(i, "${i}k")
            return tree
        }

        @Test
        fun `add one node test`() {
            val expected = "4k"
            assertEquals(expected, generateTreeWithAdd(4).get(4))
        }

        @Test
        fun `add three nodes test`() {
            assertEquals("2 \n1 3 \n", generateTreeWithAdd(2, 1, 3).Debug().treeKeysInString())
        }
    }
}
