package com.example.architecture.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.core.model.DefenseQuestion
import com.example.architecture.theme.*
import com.example.architecture.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val isQuizMode by viewModel.isQuizMode.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val score by viewModel.score.collectAsState()

    if (questions.isEmpty()) return
    val currentQuestion: DefenseQuestion = questions[currentIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Defense Q&A Practice", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.shuffleQuestions() }) {
                        Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceColor)
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(ControlFlowColor.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = currentQuestion.category,
                                color = ControlFlowColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isQuizMode) "Quiz Mode" else "Flashcard",
                            color = ActiveComponent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isQuizMode,
                            onCheckedChange = { viewModel.toggleMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AppBackground,
                                checkedTrackColor = ActiveComponent
                            )
                        )
                    }
                }

                if (isQuizMode) {
                    val accuracy = if (currentIndex > 0) (score * 100) / (currentIndex + 1) else 100
                    Text(
                        text = "Score: $score / ${questions.size} (Accuracy: $accuracy%)",
                        color = DataFlowColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isQuizMode) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clickable { viewModel.flipCard() }
                            .border(1.5.dp, if (isFlipped) DataFlowColor else ActiveComponent, RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = if (isFlipped) "ANSWER:" else "QUESTION:",
                                    color = if (isFlipped) DataFlowColor else ActiveComponent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = if (isFlipped) currentQuestion.answer else currentQuestion.question,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "(Tap card to flip)",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                } else {
                    Column {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, ActiveComponent, RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = currentQuestion.question,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        currentQuestion.options.forEachIndexed { optIdx, optionText ->
                            val isSelected = selectedOption == optIdx
                            val isCorrect = optIdx == currentQuestion.correctIndex
                            val optionBg = when {
                                selectedOption == null -> SurfaceColor
                                isSelected && isCorrect -> ActiveComponent.copy(alpha = 0.3f)
                                isSelected && !isCorrect -> PowerFlowColor.copy(alpha = 0.3f)
                                isCorrect -> ActiveComponent.copy(alpha = 0.3f)
                                else -> SurfaceColor
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(optionBg)
                                    .border(1.dp, if (isSelected) ActiveComponent else InactiveComponent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.selectOption(optIdx) }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = optionText,
                                    color = TextPrimary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.previousQuestion() },
                    enabled = currentIndex > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceColor, disabledContainerColor = AppBackground)
                ) {
                    Text("Previous", color = TextPrimary)
                }

                Button(
                    onClick = { viewModel.nextQuestion() },
                    enabled = currentIndex < questions.size - 1,
                    colors = ButtonDefaults.buttonColors(containerColor = ActiveComponent)
                ) {
                    Text("Next", color = AppBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
