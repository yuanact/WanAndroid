package top.chengdongqing.weui.core.ui.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.utils.format

@Composable
fun WeCircleProgress(
    percent: Float,
    size: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    fontSize: TextUnit = 16.sp,
    formatter: ((percent: Float) -> String)? = {
        "${it.format()}%"
    }
) {
    val localPercent = percent.coerceIn(0f, 100f)
    val animatedAngle by animateFloatAsState(
        targetValue = 360 * (localPercent / 100),
        label = "CircleProgressAnimation"
    )
    val textMeasurer = rememberTextMeasurer()
    val borderColor = MaterialTheme.colorScheme.outline
    val activeColor = MaterialTheme.colorScheme.primary
    val fontColor = MaterialTheme.colorScheme.onPrimary

    Canvas(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .size(size)
    ) {
        val radius = this.size.width / 2
        drawCircle(
            color = borderColor,
            radius = radius,
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = activeColor,
            startAngle = -90f,
            sweepAngle = animatedAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
        formatter?.also {
            val text = AnnotatedString(
                it(localPercent),
                SpanStyle(fontSize = fontSize, color = fontColor)
            )
            val textLayoutResult = textMeasurer.measure(text)
            drawText(
                textMeasurer,
                text,
                Offset(
                    x = radius - textLayoutResult.size.width / 2,
                    y = radius - textLayoutResult.size.height / 2
                )
            )
        }
    }
}