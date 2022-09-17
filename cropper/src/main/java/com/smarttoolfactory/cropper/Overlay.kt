package com.smarttoolfactory.cropper

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.smarttoolfactory.cropper.util.drawGrid
import kotlin.math.roundToInt

/**
 * Draw overlay composed of 9 rectangles. When [drawHandles]
 * is set draw handles for changing drawing rectangle
 */
@Composable
internal fun DrawingOverlay(
    modifier: Modifier,
    drawOverlay:Boolean,
    rect: Rect,
    drawGrid: Boolean,
    overlayColor: Color,
    handleColor: Color,
    strokeWidth: Dp,
    drawHandles: Boolean,
    handleSize: Float
) {

    val path = remember(rect, handleSize) {
        Path().apply {

            if (rect != Rect.Zero) {
                // Top left lines
                moveTo(rect.topLeft.x, rect.topLeft.y + handleSize)
                lineTo(rect.topLeft.x, rect.topLeft.y)
                lineTo(rect.topLeft.x + handleSize, rect.topLeft.y)

                // Top right lines
                moveTo(rect.topRight.x - handleSize, rect.topRight.y)
                lineTo(rect.topRight.x, rect.topRight.y)
                lineTo(rect.topRight.x, rect.topRight.y + handleSize)

                // Bottom right lines
                moveTo(rect.bottomRight.x, rect.bottomRight.y - handleSize)
                lineTo(rect.bottomRight.x, rect.bottomRight.y)
                lineTo(rect.bottomRight.x - handleSize, rect.bottomRight.y)

                // Bottom left lines
                moveTo(rect.bottomLeft.x + handleSize, rect.bottomLeft.y)
                lineTo(rect.bottomLeft.x, rect.bottomLeft.y)
                lineTo(rect.bottomLeft.x, rect.bottomLeft.y - handleSize)
            }
        }
    }

    Canvas(modifier = modifier) {
        val strokeWidthPx = strokeWidth.toPx()

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(Color(0x77000000))

            // Source
            drawRect(
                topLeft = rect.topLeft,
                size = rect.size,
                color = Color.Transparent,
                blendMode = BlendMode.Clear
            )
            restoreToCount(checkPoint)
        }

        if(drawOverlay){
            drawRect(
                topLeft = rect.topLeft,
                size = rect.size,
                color = overlayColor,
                style = Stroke(width = strokeWidthPx)
            )

            if (drawGrid) {
                drawGrid(rect = rect, strokeWidth = strokeWidthPx / 2, color = overlayColor)
            }

            if (drawHandles) {
                drawPath(
                    path = path,
                    color = handleColor,
                    style = Stroke(strokeWidthPx * 2)
                )
            }
        }
    }
}

/**
 * Draw image to be cropped
 */
@Composable
internal fun ImageOverlay(
    modifier: Modifier,
    imageBitmap: ImageBitmap,
    imageWidth: Int,
    imageHeight: Int
) {
    Canvas(modifier = modifier) {

        val canvasWidth = size.width.roundToInt()
        val canvasHeight = size.height.roundToInt()

        drawImage(
            image = imageBitmap,
            srcSize = IntSize(imageBitmap.width, imageBitmap.height),
            dstSize = IntSize(imageWidth, imageHeight),
            dstOffset = IntOffset(
                x = (canvasWidth - imageWidth) / 2,
                y = (canvasHeight - imageHeight) / 2
            )
        )
    }
}

