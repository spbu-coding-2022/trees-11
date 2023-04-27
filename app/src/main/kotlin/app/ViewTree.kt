package app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

open class ViewTree<Key : Comparable<Key>, Value> {

    protected class ViewNode<Int : Comparable<Int>, String>(
        var key: Int,
        var value: String,
        var x: MutableState<Float>,
        var y: MutableState<Float>,
        var left: ViewNode<Int, String>? = null,
        var right: ViewNode<Int, String>? = null,
        var color: Boolean,
    )

    @Composable
    protected fun drawTree(node: ViewNode<Int, String>){

        node.left?.let {
            drawLine(node.x, node.y, it.x, it.y)
            drawTree(it)

        }
        drawNode(node.key.toString(), node.x, node.y)
        node.right?.let {
            drawLine(node.x, node.y, it.x, it.y)
            drawTree(it)
        }
    }

    @Composable
    fun drawNode(key: String,
                 centerX: MutableState<Float>,
                 centerY: MutableState<Float>) {

        val offsetX = remember { mutableStateOf(0f) }
        val offsetY = remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offsetX.value.roundToInt(),
                        y = offsetY.value.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                     detectDragGestures { change, dragAmount ->
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                        centerX.value = offsetX.value
                        centerY.value = offsetY.value
                    }
                }
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInParent()
                    centerX.value = rect.center.x
                    centerY.value = rect.center.y
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = key,
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun drawLine(x1: MutableState<Float>, y1: MutableState<Float>, x2: MutableState<Float>, y2: MutableState<Float>) {
        Canvas(modifier = Modifier.fillMaxSize().zIndex(-1f)) {
            drawLine(color = Color.DarkGray,
                     start = Offset(x1.value, y1.value),
                     end = Offset(x2.value, y2.value),
                     strokeWidth = 5f )
        }
    }
}

