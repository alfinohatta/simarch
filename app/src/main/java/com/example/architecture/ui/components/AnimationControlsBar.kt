package com.example.architecture.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.theme.*

@Composable
fun AnimationControlsBar(
    isPlaying: Boolean,
    speedMultiplier: Float,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onStepBack: (() -> Unit)? = null,
    onStepForward: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onBackground

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = surfaceColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Playback Navigation Group
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onReset()
                    }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset Simulation", tint = textColor)
                }

                if (onStepBack != null) {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onStepBack()
                        }
                    ) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Step", tint = textColor)
                    }
                }

                // Primary Play / Pause FAB
                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (isPlaying) onPause() else onPlay()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .graphicsLayer { shadowElevation = 8.dp.toPx() },
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = ActiveComponent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause Simulation" else "Play Simulation",
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (onStepForward != null) {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onStepForward()
                        }
                    ) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next Step", tint = textColor)
                    }
                }
            }

            // Speed Multiplier Chips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(0.5f, 1f, 2f).forEach { speed ->
                    val isSelected = speedMultiplier == speed
                    val chipBg by animateColorAsState(
                        targetValue = if (isSelected) ActiveComponent else MaterialTheme.colorScheme.surfaceVariant,
                        label = "chipBg"
                    )
                    val chipTextCol = if (isSelected) Color.White else textColor

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(chipBg)
                            .border(1.dp, if (isSelected) ActiveComponent else MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onSpeedChange(speed)
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${speed}x",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = chipTextCol
                        )
                    }
                }
            }
        }
    }
}
