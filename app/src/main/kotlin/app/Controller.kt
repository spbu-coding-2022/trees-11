package app

import androidx.compose.runtime.MutableState

enum class treeType {
    RBTree,
    AVLTree,
    BSTree
}

open class DrawNode(
    var key: String,
    var value: String,
    var x: MutableState<Float>,
    var y: MutableState<Float>,
    var prevX: MutableState<Float>,
    var prevY: MutableState<Float>,
)
fun getAllNodes( name: String, treeType: treeType ): Array<DrawNode> {
    TODO()
}

fun drawInsert(key: String, value: String) {
    TODO()
}

fun drawRemove(key: String) {
    TODO()
}

fun drawFind(key: String) {
    TODO()
}