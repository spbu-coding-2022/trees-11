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

open class ViewTree {

    @Composable
    open fun drawTree(tree: Controller.DrawTree ) {
        tree.getAllDrawNodes().forEach { node ->
            drawNode(node.key, node.coordinates)}
//            node.prevCoordinates.value?.let {
//                drawLine(node.coordinates, node.prevCoordinates)
//            }
//        }
    }

    @Composable
    fun drawNode(
        key: String,
        coordinates: MutableState<Pair<Float, Float>>
    ) {

        val offsetX = remember { mutableStateOf(coordinates.value.first) }
        val offsetY = remember { mutableStateOf(coordinates.value.second) }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offsetX.value.roundToInt(),
                        y = offsetY.value.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                        //coordinates.value = Pair(offsetX.value, offsetY.value)
                    }
                }
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .onGloballyPositioned { layoutCoordinates ->
                    val rect = layoutCoordinates.boundsInParent()
                    coordinates.value = Pair(rect.center.x, rect.center.y)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = key,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun drawLine(coordinates:MutableState<Pair<Float, Float>>, prevCoordinates: MutableState<Pair<Float, Float>>) {
        Canvas(modifier = Modifier.fillMaxSize().zIndex(-1f)) {
            drawLine(
                color = Color.DarkGray,
                start = Offset(coordinates.value.first,coordinates.value.second),
                end = Offset(prevCoordinates.value.first, prevCoordinates.value.second),
                strokeWidth = 5f
            )
        }
    }
}

