package com.example.architecture.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.core.model.ComponentData
import com.example.architecture.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentInfoBottomSheet(
    component: ComponentData,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = LightSurfaceColor,
        contentColor = LightTextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = component.icon,
                        contentDescription = component.name,
                        tint = ActiveComponent,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = component.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightTextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = LightTextSecondary)
                }
            }

            HorizontalDivider(color = InactiveComponent.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 12.dp))

            // Kid-Friendly Analogy Highlight Card
            if (component.simpleAnalogy.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ActiveComponent.copy(alpha = 0.15f))
                        .border(1.dp, ActiveComponent, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ChildCare, contentDescription = "Kid Analogy", tint = ActiveComponent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "EXPLAINED SIMPLY FOR KIDS:",
                                color = ActiveComponent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = component.simpleAnalogy,
                            color = LightTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            Text(
                text = "System Architecture Role",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ControlFlowColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = component.architectureRole,
                fontSize = 13.sp,
                color = DataFlowColor,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Main Function",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ControlFlowColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = component.mainFunction,
                fontSize = 13.sp,
                color = LightTextPrimary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Technical Characteristics",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ControlFlowColor
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (component.isStorageDevice && component.storageComparison != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DataFlowColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightAppBackground)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Type", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LightTextSecondary, modifier = Modifier.weight(1.5f))
                        Text("Cap.", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LightTextSecondary, modifier = Modifier.weight(0.8f))
                        Text("Bus", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LightTextSecondary, modifier = Modifier.weight(1f))
                        Text("Read Speed", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = LightTextSecondary, modifier = Modifier.weight(1.2f))
                    }

                    component.storageComparison.forEach { row ->
                        HorizontalDivider(color = InactiveComponent.copy(alpha = 0.2f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(row.type, fontSize = 11.sp, color = LightTextPrimary, modifier = Modifier.weight(1.5f))
                            Text(row.capacity, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = LightTextPrimary, modifier = Modifier.weight(0.8f))
                            Text(row.interfaceType, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = LightTextPrimary, modifier = Modifier.weight(1f))
                            Text(row.readSpeed, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = ActiveComponent, modifier = Modifier.weight(1.2f))
                        }
                    }
                }

                if (component.storageExplanation != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = component.storageExplanation,
                        fontSize = 12.sp,
                        color = LightTextSecondary,
                        lineHeight = 16.sp
                    )
                }
            } else {
                component.characteristics.forEach { spec ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "• ${spec.label}", fontSize = 13.sp, color = LightTextSecondary)
                        Text(
                            text = spec.value,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = LightTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
