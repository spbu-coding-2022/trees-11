package app.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import app.controller.Controller
import kotlin.math.roundToInt

open class ViewTree {

    @Composable
    open fun drawTree(
        tree: Controller.DrawTree,
        offSetX: MutableState<Float>,
        offSetY: MutableState<Float>
    ) {
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .clipToBounds()
                .padding(top = 15.dp)
                .pointerInput(offSetX, offSetY) {
                    detectDragGestures { _, dragAmount ->
                        offSetX.value += dragAmount.x
                        offSetY.value += dragAmount.y
                    }
                }

        ) {
            Box(modifier = Modifier.size(50.dp)) {
                tree.content.value.forEach() { node ->
                    drawNode(node, 50, offSetX, offSetY)
                    node.parent?.let {
                        drawLine(node, it, 50, offSetX, offSetY)
                    }
                    tree.updateCoordinate(node)
                }
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun drawNode(
        node: Controller.DrawNode,
        size: Int,
        offSetX: MutableState<Float>,
        offSetY: MutableState<Float>
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            TooltipArea(
                tooltip = {
                    Surface {
                        Text(
                            text = "value: ${node.value}"
                        )
                    }
                },
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (node.x.value + offSetX.value).roundToInt(),
                            y = (node.y.value + offSetY.value).roundToInt(),
                        )
                    }
                    .pointerInput(node.x, node.y) {
                        detectDragGestures { _, dragAmount ->
                            node.x.value += dragAmount.x
                            node.y.value += dragAmount.y
                        }
                    }

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(size.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = node.key,
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }

    @Composable
    fun drawLine(
        node: Controller.DrawNode,
        parent : Controller.DrawNode,
        size: Int = 50,
        offSetX: MutableState<Float>,
        offSetY: MutableState<Float>
    ) {
        Canvas(modifier = Modifier.fillMaxSize().zIndex(-1f)) {
            drawLine(
                color = Color.DarkGray,
                start = Offset(node.x.value + size/2 + offSetX.value, node.y.value + size/2 + offSetY.value),
                end = Offset(parent.x.value + size/2 + offSetX.value, parent.y.value + size/2 + offSetY.value),
                strokeWidth = 3f
            )
        }
    }
}
