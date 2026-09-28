package com.example.architecture.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.theme.*

data class AssemblyLine(
    val lineNum: Int,
    val opCode: String,
    val operand: String,
    val comment: String
)

@Composable
fun AssemblyInstructionVisualizer(
    stepIndex: Int,
    appName: String,
    modifier: Modifier = Modifier
) {
    val programAssembly = remember(appName) {
        listOf(
            AssemblyLine(1, "MOV", "EAX, [0x00401000]", "; Load $appName entry point into EAX"),
            AssemblyLine(2, "PUSH", "EBP", "; Save stack frame base pointer"),
            AssemblyLine(3, "MOV", "EBP, ESP", "; Initialize new stack frame"),
            AssemblyLine(4, "CALL", "0x00402500", "; Invoke OS kernel process init"),
            AssemblyLine(5, "MOV", "EBX, 0x00000001", "; Set register EBX return status"),
            AssemblyLine(6, "INT", "0x80", "; Trigger system call interrupt")
        )
    }

    val activeLineIdx = (stepIndex - 1) % programAssembly.size
    val activeLine = programAssembly[activeLineIdx]

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ControlFlowColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CPU x86-64 Machine Code Inspector",
                    color = ControlFlowColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ActiveComponent)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("FETCH & EXECUTE", color = AppBackground, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Assembly Program Listing Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppBackground)
                    .padding(8.dp)
            ) {
                programAssembly.forEachIndexed { idx, line ->
                    val isLineActive = idx == activeLineIdx
                    val bg = if (isLineActive) ActiveComponent.copy(alpha = 0.25f) else AppBackground
                    val textCol = if (isLineActive) ActiveComponent else TextPrimary

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(bg)
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "0x0${line.lineNum}  ",
                                color = TextSecondary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${line.opCode} ",
                                color = ActiveComponent,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = line.operand,
                                color = textCol,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = line.comment,
                            color = TextSecondary,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
