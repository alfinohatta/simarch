package com.example.architecture.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.architecture.theme.*
import kotlin.math.pow

/**
 * Animated Dashed Line representing Power flow along electrical traces.
 */
@Composable
fun AnimatedPowerLine(
    start: Offset,
    end: Offset,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "powerLine")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing)
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        if (isActive) {
            drawLine(
                color = PowerFlowColor,
                start = start,
                end = end,
                strokeWidth = 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), phase)
            )
        }
    }
}

/**
 * Animated Bus Packet transferring binary data / control signals between hardware modules.
 */
@Composable
fun AnimatedBusTransfer(
    isActive: Boolean,
    start: Offset,
    end: Offset,
    color: Color = DataFlowColor,
    packetLabel: String = "DATA",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "busTransfer")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing)
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        if (isActive) {
            // Draw bus trace line
            drawLine(
                color = color.copy(alpha = 0.5f),
                start = start,
                end = end,
                strokeWidth = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            // Draw traveling packet nodes
            repeat(4) { index ->
                val p = (progress + index * 0.25f) % 1f
                val currentX = start.x + (end.x - start.x) * p
                val currentY = start.y + (end.y - start.y) * p
                val center = Offset(currentX, currentY)

                drawCircle(
                    color = color,
                    radius = 7f,
                    center = center
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = center
                )
            }
        }
    }
}

/**
 * Interactive Radial Pulse Ripple triggered when a component is tapped.
 */
@Composable
fun ComponentTapRipple(
    center: Offset,
    trigger: Boolean,
    color: Color = ActiveComponent,
    modifier: Modifier = Modifier
) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger) {
            animProgress.snapTo(0f)
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
    }

    if (animProgress.value > 0f && animProgress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val radius = 20f + animProgress.value * 80f
            val alpha = (1f - animProgress.value).coerceIn(0f, 1f)

            drawCircle(
                color = color.copy(alpha = alpha),
                radius = radius,
                center = center,
                style = Stroke(width = 4f)
            )
        }
    }
}

/**
 * Clock Pulse Oscilloscope Waveform showing CPU clock cycles.
 */
@Composable
fun ClockPulseWaveform(
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "clockWave")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "offset"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val midY = height / 2f

        if (isActive) {
            val step = 20f
            var x = 0f
            val points = mutableListOf<Offset>()

            while (x < width + step) {
                val waveX = x + offset
                val high = (waveX / step).toInt() % 2 == 0
                val y = if (high) midY - 12f else midY + 12f
                points.add(Offset(x, y))
                x += step
            }

            for (i in 0 until points.size - 1) {
                drawLine(
                    color = ActiveComponent,
                    start = points[i],
                    end = Offset(points[i+1].x, points[i].y),
                    strokeWidth = 3f
                )
                drawLine(
                    color = ActiveComponent,
                    start = Offset(points[i+1].x, points[i].y),
                    end = points[i+1],
                    strokeWidth = 3f
                )
            }
        } else {
            drawLine(
                color = InactiveComponent.copy(alpha = 0.5f),
                start = Offset(0f, midY),
                end = Offset(width, midY),
                strokeWidth = 2f
            )
        }
    }
}

/**
 * Quadratic Bezier interpolation helper.
 */
fun quadraticBezier(p0: Offset, p1: Offset, p2: Offset, t: Float): Offset {
    val x = (1 - t).pow(2) * p0.x + 2 * (1 - t) * t * p1.x + t.pow(2) * p2.x
    val y = (1 - t).pow(2) * p0.y + 2 * (1 - t) * t * p1.y + t.pow(2) * p2.y
    return Offset(x, y)
}
