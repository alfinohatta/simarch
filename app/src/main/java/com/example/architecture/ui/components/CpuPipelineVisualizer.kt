package com.example.architecture.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.theme.*

enum class PipelineStage(val title: String, val code: String) {
    FETCH("Instruction Fetch", "IF"),
    DECODE("Instruction Decode", "ID"),
    EXECUTE("ALU Execute", "EX"),
    MEMORY("Memory Access", "MEM"),
    WRITEBACK("Register Writeback", "WB")
}

@Composable
fun CpuPipelineVisualizer(
    activeStage: PipelineStage,
    currentInstruction: String,
    registerPc: String,
    registerAcc: String,
    hasHazard: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ActiveComponent.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CPU 5-Stage Instruction Pipeline",
                    color = ActiveComponent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                if (hasHazard) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PowerFlowColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("DATA HAZARD / STALL", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5-Stage Pipeline Progress Boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PipelineStage.values().forEach { stage ->
                    val isActive = stage == activeStage
                    val boxBg = if (isActive) ActiveComponent.copy(alpha = 0.3f) else AppBackground
                    val boxBorder = if (isActive) ActiveComponent else InactiveComponent.copy(alpha = 0.4f)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(boxBg)
                            .border(1.dp, boxBorder, RoundedCornerShape(6.dp))
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stage.code,
                                color = if (isActive) ActiveComponent else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = stage.name.lowercase().capitalize(),
                                color = TextSecondary,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Instruction & Register Registers Monitor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(AppBackground)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("CURRENT INSTRUCTION (IR):", color = ControlFlowColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = currentInstruction,
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("PC REG", color = TextSecondary, fontSize = 9.sp)
                        Text(registerPc, color = ActiveComponent, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text("ACC REG", color = TextSecondary, fontSize = 9.sp)
                        Text(registerAcc, color = DataFlowColor, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}
