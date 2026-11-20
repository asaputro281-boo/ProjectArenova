package com.example.arenova.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arenova.ui.theme.ArenovaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ArenovaSplashScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isInspectionMode = LocalInspectionMode.current

    val contentAlpha = remember { Animatable(if (isInspectionMode) 1f else 0f) }
    val logoScale = remember { Animatable(if (isInspectionMode) 1f else 0.6f) }
    val logoOffsetY = remember { Animatable(if (isInspectionMode) 0f else 60f) }
    val fieldLinesAlpha = remember { Animatable(if (isInspectionMode) 1f else 0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "DotsWaveTransition")

    if (!isInspectionMode) {
        LaunchedEffect(Unit) {
            launch {
                fieldLinesAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                contentAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                logoOffsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            delay(1400L)

            launch {
                logoScale.animateTo(
                    targetValue = 1.08f,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                )
            }
            launch {
                fieldLinesAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            }
            contentAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            )

            onNavigateNext()
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        val backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF43A047),
                Color(0xFF388E3C),
                Color(0xFF2E7D32)
            ),
            start = Offset(0f, 0f),
            end = Offset(widthPx, heightPx)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.dp.toPx()
                val lineColor = Color.White.copy(alpha = 0.14f * fieldLinesAlpha.value)
                val stroke = Stroke(width = strokeWidth)

                val w = size.width
                val h = size.height

                // Pojok Kanan Atas
                val trCenterX = w * 0.75f
                val trCenterY = h * 0.10f
                val trInnerRadius = w * 0.22f
                val trOuterRadius = w * 0.44f

                drawCircle(
                    color = lineColor,
                    radius = trInnerRadius,
                    center = Offset(trCenterX, trCenterY),
                    style = stroke
                )
                drawCircle(
                    color = lineColor,
                    radius = trOuterRadius,
                    center = Offset(trCenterX, trCenterY),
                    style = stroke
                )
                drawLine(
                    color = lineColor,
                    start = Offset(trCenterX, 0f),
                    end = Offset(trCenterX, h * 0.32f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = lineColor,
                    start = Offset(w * 0.35f, trCenterY),
                    end = Offset(w * 1.05f, trCenterY),
                    strokeWidth = strokeWidth
                )

                // Pojok Kiri Bawah
                val boxRight = w * 0.68f
                val boxTop = h * 0.76f
                val cornerRadius = 40.dp.toPx()

                val outerPenaltyPath = Path().apply {
                    moveTo(0f, boxTop)
                    lineTo(boxRight - cornerRadius, boxTop)
                    quadraticTo(boxRight, boxTop, boxRight, boxTop + cornerRadius)
                    lineTo(boxRight, h)
                }
                drawPath(
                    path = outerPenaltyPath,
                    color = lineColor,
                    style = stroke
                )

                val innerBoxRight = w * 0.28f
                val innerBoxTop = h * 0.76f
                drawLine(
                    color = lineColor,
                    start = Offset(innerBoxRight, innerBoxTop),
                    end = Offset(innerBoxRight, h * 0.86f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = lineColor,
                    start = Offset(0f, h * 0.79f),
                    end = Offset(w * 0.28f, h * 0.86f),
                    strokeWidth = strokeWidth
                )

                val arcRect = Rect(
                    center = Offset(w * 0.28f, h * 0.91f),
                    radius = w * 0.24f
                )
                drawArc(
                    color = lineColor,
                    startAngle = -90f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = arcRect.topLeft,
                    size = arcRect.size,
                    style = stroke
                )

                val dashCircleRadius = w * 0.14f
                val dashCenter = Offset(w * 0.28f, h * 0.91f)
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                drawCircle(
                    color = lineColor,
                    radius = dashCircleRadius,
                    center = dashCenter,
                    style = Stroke(width = strokeWidth, pathEffect = dashEffect)
                )

                drawLine(
                    color = lineColor,
                    start = Offset(innerBoxRight, h * 0.86f),
                    end = Offset(w * 0.52f, h * 0.98f),
                    strokeWidth = strokeWidth
                )
            }

            // Logo Huruf "A"
            Canvas(
                modifier = Modifier
                    .size(140.dp, 160.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                        translationY = logoOffsetY.value
                        alpha = contentAlpha.value
                    }
            ) {
                val w = size.width
                val h = size.height

                val letterAPath = Path().apply {
                    fillType = PathFillType.EvenOdd
                    moveTo(w * 0.50f, 0f)
                    lineTo(w * 0.98f, h)
                    lineTo(w * 0.70f, h)
                    lineTo(w * 0.60f, h * 0.67f)
                    lineTo(w * 0.40f, h * 0.67f)
                    lineTo(w * 0.30f, h)
                    lineTo(w * 0.02f, h)
                    close()

                    moveTo(w * 0.50f, h * 0.26f)
                    lineTo(w * 0.61f, h * 0.54f)
                    lineTo(w * 0.39f, h * 0.54f)
                    close()
                }

                drawPath(
                    path = letterAPath,
                    color = Color.White
                )
            }

            // 3 Titik Indikator Horizontal
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 68.dp)
                    .graphicsLayer {
                        alpha = contentAlpha.value
                    },
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedIndicatorDot(
                    color = Color.White,
                    delayMillis = 0,
                    infiniteTransition = infiniteTransition
                )
                AnimatedIndicatorDot(
                    color = Color(0xFFF97316),
                    delayMillis = 150,
                    infiniteTransition = infiniteTransition
                )
                AnimatedIndicatorDot(
                    color = Color.White,
                    delayMillis = 300,
                    infiniteTransition = infiniteTransition
                )
            }
        }
    }
}

@Composable
private fun AnimatedIndicatorDot(
    color: Color,
    delayMillis: Int,
    infiniteTransition: InfiniteTransition
) {
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(delayMillis)
        ),
        label = "DotAnim_$delayMillis"
    )

    val scale = 0.8f + (animProgress * 0.45f)
    val alpha = 0.45f + (animProgress * 0.55f)
    val offsetY = -animProgress * 4f

    Box(
        modifier = Modifier
            .size(8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                translationY = offsetY
            }
            .background(color = color, shape = CircleShape)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArenovaSplashScreenPreview() {
    ArenovaTheme {
        ArenovaSplashScreen(onNavigateNext = {})
    }
}