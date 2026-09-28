package com.example.architecture.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.theme.*

@Composable
fun RelationshipDiagram(modifier: Modifier = Modifier) {
    val chain = listOf(
        "CPU" to "Executes instructions & coordinates memory",
        "RAM" to "Primary memory holding active instructions",
        "Storage" to "Non-volatile secondary file & OS storage",
        "Motherboard" to "PCB hosting system buses & power traces",
        "BIOS/UEFI" to "Firmware executing POST & bootloader",
        "Operating System" to "Kernel managing processes & hardware",
        "Input/Output Devices" to "Peripherals generating IRQs & rendering output"
    )

    var selectedIndex by remember { mutableStateOf(0) }
    val currentRelation = chain[selectedIndex]

    val surfaceColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onBackground

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ControlFlowColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Logical Component Chain",
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap node to inspect",
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Interactive Chain Nodes Row
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                chain.forEachIndexed { index, (item, _) ->
                    val isSelected = index == selectedIndex
                    val nodeBg by animateColorAsState(
                        targetValue = if (isSelected) ActiveComponent.copy(alpha = 0.18f) else LightAppBackground,
                        label = "nodeBg"
                    )
                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) ActiveComponent else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        label = "borderColor"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(nodeBg)
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable { selectedIndex = index }
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(ActiveComponent, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = item,
                                color = if (isSelected) ActiveComponent else textColor,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }

                    if (index < chain.size - 1) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Arrow",
                            tint = if (isSelected || index == selectedIndex - 1) ActiveComponent else ControlFlowColor.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Live Selected Component Role Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(LightAppBackground)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${currentRelation.first}: ",
                        color = ActiveComponent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentRelation.second,
                        color = textColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
