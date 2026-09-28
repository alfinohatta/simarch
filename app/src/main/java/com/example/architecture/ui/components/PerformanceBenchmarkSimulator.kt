package com.example.architecture.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

enum class StorageType(val displayName: String, val bootSec: Int, val appLaunchSec: Float, val speedMBs: Int) {
    HDD("HDD (Platter)", 45, 8.5f, 150),
    SATA_SSD("SATA III SSD", 14, 2.2f, 550),
    NVME_SSD("NVMe PCIe 4.0", 4, 0.4f, 7000)
}

@Composable
fun PerformanceBenchmarkSimulator(
    modifier: Modifier = Modifier
) {
    var selectedStorage by remember { mutableStateOf(StorageType.NVME_SSD) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, DataFlowColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Architecture Performance & Latency Simulator",
                color = DataFlowColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Simulate how storage bandwidth directly influences boot time and program loading speed:",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Storage Selection Segmented Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StorageType.values().forEach { storage ->
                    val isSelected = storage == selectedStorage
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) ActiveComponent else AppBackground)
                            .clickable { selectedStorage = storage }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = storage.displayName,
                            color = if (isSelected) AppBackground else TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Simulated Latency Bars
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Read Speed:", color = TextSecondary, fontSize = 11.sp)
                    Text("${selectedStorage.speedMBs} MB/s", color = ActiveComponent, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Estimated System Boot Time:", color = TextSecondary, fontSize = 11.sp)
                    Text("${selectedStorage.bootSec} seconds", color = DataFlowColor, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Chrome App Launch Latency:", color = TextSecondary, fontSize = 11.sp)
                    Text("${selectedStorage.appLaunchSec} seconds", color = PowerFlowColor, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
