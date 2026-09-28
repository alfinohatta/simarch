package com.example.architecture.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.model.SystemComponents
import com.example.architecture.theme.*
import com.example.architecture.ui.components.*
import com.example.architecture.viewmodel.AppOption
import com.example.architecture.viewmodel.ExecutionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExecutionScreen(
    viewModel: ExecutionViewModel,
    onBack: () -> Unit
) {
    val selectedApp by viewModel.selectedApp.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val speedMultiplier by viewModel.speedMultiplier.collectAsState()
    val stepInfo by viewModel.stepInfo.collectAsState()
    val selectedComponentId by viewModel.selectedComponent.collectAsState()

    var isKidMode by remember { mutableStateOf(true) }
    var isCameraCentered by remember { mutableStateOf(false) }
    var activeInspectorTab by remember { mutableStateOf(0) } // 0: State & Buses, 1: CPU Pipeline, 2: Assembly Code

    val appBg = LightAppBackground
    val surfaceBg = LightSurfaceColor
    val textPrimary = LightTextPrimary
    val textSecondary = LightTextSecondary

    val kidStepDescriptions = mapOf(
        1 to "Your Hand Clicks! You tap the ${selectedApp.name} toy icon on the screen with your finger (Input Device).",
        2 to "Teacher Hears You! The OS Teacher catches your tap and gets ready to launch ${selectedApp.name}.",
        3 to "Searching the Toy Chest! The Teacher opens the Big Toy Chest (Storage) to find ${selectedApp.name}'s files.",
        4 to "Bringing Toys to Workbench! The computer copies ${selectedApp.name} from the Toy Chest onto the fast Workbench (RAM).",
        5 to "Chef Starts Cooking! The Chief Chef (CPU) fetches instructions from the Workbench and solves math puzzles.",
        6 to "Chef Asks the Painter! The Chief Chef sends painting instructions to the Artist (GPU).",
        7 to "Painter Paints Frames! The Artist GPU paints millions of pixels into the frame buffer.",
        8 to "Postman Delivers Letters! The Network Postman fetches web pages and pictures across the internet.",
        9 to "Magic Window Displays App! The ${selectedApp.name} window opens on the Magic Screen for you to play!"
    )

    val (targetScale, targetX, targetY) = if (isCameraCentered) {
        Triple(1.0f, 0f, 0f)
    } else when (currentStep) {
        1 -> Triple(1.05f, 10f, 0f)
        2 -> Triple(1.06f, 0f, 0f)
        3 -> Triple(1.06f, -10f, 0f)
        4 -> Triple(1.05f, 5f, 0f)
        5 -> Triple(1.06f, 0f, 0f)
        6 -> Triple(1.05f, -5f, 0f)
        7 -> Triple(1.05f, -10f, 0f)
        8 -> Triple(1.05f, 10f, 10f)
        9 -> Triple(1.04f, -10f, 10f)
        else -> Triple(1.0f, 0f, 0f)
    }

    val animScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "execCamScale"
    )
    val animX by animateFloatAsState(
        targetValue = targetX,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "execCamX"
    )
    val animY by animateFloatAsState(
        targetValue = targetY,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "execCamY"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Program Execution (Part 2)", color = textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textPrimary)
                    }
                },
                actions = {
                    FilterChip(
                        selected = isKidMode,
                        onClick = { isKidMode = !isKidMode },
                        label = { Text(if (isKidMode) "Kid Story" else "Tech Mode", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.ChildCare, contentDescription = "Kid Mode", modifier = Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ActiveComponent,
                            selectedLabelColor = LightSurfaceColor,
                            containerColor = LightAppBackground,
                            labelColor = textPrimary
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceBg)
            )
        },
        containerColor = appBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Application Picker
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surfaceBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Select Application to Execute:", color = ControlFlowColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            viewModel.apps.forEach { app ->
                                AppChoiceChip(
                                    app = app,
                                    isSelected = app.id == selectedApp.id,
                                    onClick = { viewModel.selectApp(app) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mandatory Execution Chain Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceBg)
                        .border(1.dp, DataFlowColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = if (isKidMode) "Hands (Input) → Teacher (OS) → Toy Chest (Storage) → Workbench (RAM) → Chef (CPU) → Painter (GPU) → Magic Screen" else "User Input → Operating System → Storage → RAM → CPU → GPU / I/O → Output on Monitor",
                        color = DataFlowColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Phase & Title Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surfaceBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ActiveComponent)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = stepInfo.phaseName,
                                        color = LightSurfaceColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stepInfo.title,
                                    color = ActiveComponent,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(LightAppBackground)
                                    .clickable { isCameraCentered = !isCameraCentered }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.CenterFocusWeak, contentDescription = "Camera Focus", tint = ActiveComponent, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isCameraCentered) "1.0x Full" else "Cam ${"%.2f".format(animScale)}x",
                                    color = textSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isKidMode) (kidStepDescriptions[currentStep] ?: "") else stepInfo.description,
                            color = textPrimary,
                            fontSize = 12.sp,
                            fontStyle = if (isKidMode) FontStyle.Italic else FontStyle.Normal,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hardware Interactive Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceBg)
                        .border(1.dp, ControlFlowColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .graphicsLayer {
                            scaleX = animScale
                            scaleY = animScale
                            translationX = animX
                            translationY = animY
                        }
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ExecCompCard("input", if (isKidMode) "Your Hands" else "Input Device", isActive = stepInfo.activeComponents.contains("input"), onClick = { viewModel.selectComponent("input") })
                            ExecCompCard("os", if (isKidMode) "Teacher OS" else "OS Kernel", isActive = stepInfo.activeComponents.contains("os"), onClick = { viewModel.selectComponent("os") })
                            ExecCompCard("storage", if (isKidMode) "Toy Chest" else "Storage Drive", isActive = stepInfo.activeComponents.contains("storage"), onClick = { viewModel.selectComponent("storage") })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ExecCompCard("ram", if (isKidMode) "Workbench" else "RAM Memory", isActive = stepInfo.activeComponents.contains("ram"), onClick = { viewModel.selectComponent("ram") })
                            ExecCompCard("cpu", if (isKidMode) "Chief Chef" else "CPU Core / ALU", isActive = stepInfo.activeComponents.contains("cpu"), onClick = { viewModel.selectComponent("cpu") })
                            ExecCompCard("gpu", if (isKidMode) "Painter GPU" else "GPU Cores", isActive = stepInfo.activeComponents.contains("gpu"), onClick = { viewModel.selectComponent("gpu") })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ExecCompCard("network", if (isKidMode) "Postman" else "NIC Network", isActive = stepInfo.activeComponents.contains("network"), onClick = { viewModel.selectComponent("network") })
                            ExecCompCard("output", if (isKidMode) "Magic Window" else "Monitor Output", isActive = stepInfo.activeComponents.contains("output"), onClick = { viewModel.selectComponent("output") })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                ColorLegendBar()

                Spacer(modifier = Modifier.height(10.dp))

                // Clean Tabbed Inspector Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Buses & State", "5-Stage Pipeline", "Assembly Code").forEachIndexed { idx, tabName ->
                        val isSelected = activeInspectorTab == idx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ActiveComponent else LightSurfaceColor)
                                .border(1.dp, if (isSelected) ActiveComponent else MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                .clickable { activeInspectorTab = idx }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabName,
                                color = if (isSelected) LightSurfaceColor else textPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (activeInspectorTab) {
                    0 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = LightAppBackground),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, ControlFlowColor.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("CPU REGISTERS & BUS PIPELINE STATE:", color = ControlFlowColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = stepInfo.registerState,
                                    color = textPrimary,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                    1 -> {
                        val activePipelineStage = when (currentStep) {
                            1 -> PipelineStage.FETCH
                            2 -> PipelineStage.FETCH
                            3 -> PipelineStage.FETCH
                            4 -> PipelineStage.FETCH
                            5 -> PipelineStage.DECODE
                            6 -> PipelineStage.EXECUTE
                            7 -> PipelineStage.EXECUTE
                            8 -> PipelineStage.MEMORY
                            9 -> PipelineStage.WRITEBACK
                            else -> PipelineStage.FETCH
                        }

                        CpuPipelineVisualizer(
                            activeStage = activePipelineStage,
                            currentInstruction = "EXECUTE ${selectedApp.name.uppercase()} (0x00401000)",
                            registerPc = "0x0040100${currentStep * 4}",
                            registerAcc = "0x0000000${currentStep}",
                            hasHazard = false
                        )
                    }
                    2 -> {
                        AssemblyInstructionVisualizer(
                            stepIndex = currentStep,
                            appName = selectedApp.name
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fixed Controls Bar
            AnimationControlsBar(
                isPlaying = isPlaying,
                speedMultiplier = speedMultiplier,
                onPlay = { viewModel.play() },
                onPause = { viewModel.pause() },
                onReset = { viewModel.reset() },
                onStepBack = { viewModel.previousStep() },
                onStepForward = { viewModel.nextStep() },
                onSpeedChange = { viewModel.setSpeed(it) }
            )
        }
    }

    selectedComponentId?.let { id ->
        SystemComponents.getById(id)?.let { data ->
            ComponentInfoBottomSheet(
                component = data,
                onDismiss = { viewModel.selectComponent(null) }
            )
        }
    }
}

@Composable
private fun AppChoiceChip(
    app: AppOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) ActiveComponent else LightAppBackground)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = app.iconName,
            color = if (isSelected) LightSurfaceColor else LightTextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ExecCompCard(
    id: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val compData = SystemComponents.getById(id)
    val borderColor = if (isActive) ActiveComponent else InactiveComponent
    val cardBg = if (isActive) ActiveComponent.copy(alpha = 0.25f) else LightSurfaceColor
    val textCol = LightTextPrimary

    Box(
        modifier = Modifier
            .width(92.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(cardBg)
            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            compData?.icon?.let { iconVector ->
                Icon(
                    imageVector = iconVector,
                    contentDescription = label,
                    tint = if (isActive) ActiveComponent else InactiveComponent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = label,
                color = if (isActive) ActiveComponent else textCol,
                fontSize = 9.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
