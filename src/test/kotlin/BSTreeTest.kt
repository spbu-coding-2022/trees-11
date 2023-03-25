import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BSTreeTest {
    @Test
    fun addFirstNodeCorrectTest() {
        val tree = BSTree<Int, String>()
        val expected = "4k"
        tree.insert(4, "4k")
        Assertions.assertEquals(expected, tree.get(4))
    }
}