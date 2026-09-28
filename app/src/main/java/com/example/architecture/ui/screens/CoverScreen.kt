package com.example.architecture.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.theme.*
import com.example.architecture.viewmodel.UserPreferencesViewModel

@Composable
fun CoverScreen(
    userViewModel: UserPreferencesViewModel,
    onContinue: () -> Unit
) {
    val studentName by userViewModel.studentName.collectAsState()
    val group by userViewModel.group.collectAsState()
    val submissionDate by userViewModel.submissionDate.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "ICT / Computer Science",
                    color = ControlFlowColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Computer Architecture",
                    color = TextSecondary,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Practical Assignment 1 — Visualizing the Startup Process and Program Execution",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Student Details", color = ActiveComponent, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { userViewModel.setStudentName(it) },
                            label = { Text("Student Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ActiveComponent,
                                unfocusedBorderColor = InactiveComponent,
                                focusedLabelColor = ActiveComponent,
                                unfocusedLabelColor = TextSecondary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = group,
                            onValueChange = { userViewModel.setGroup(it) },
                            label = { Text("Group") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ActiveComponent,
                                unfocusedBorderColor = InactiveComponent,
                                focusedLabelColor = ActiveComponent,
                                unfocusedLabelColor = TextSecondary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = submissionDate,
                            onValueChange = { userViewModel.setSubmissionDate(it) },
                            label = { Text("Submission Date") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ActiveComponent,
                                unfocusedBorderColor = InactiveComponent,
                                focusedLabelColor = ActiveComponent,
                                unfocusedLabelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActiveComponent),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Continue",
                    color = AppBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
