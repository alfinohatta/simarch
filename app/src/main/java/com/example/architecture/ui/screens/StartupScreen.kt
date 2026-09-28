package com.example.architecture.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.architecture.viewmodel.StartupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartupScreen(
    viewModel: StartupViewModel,
    onBack: () -> Unit
) {
    val currentScene by viewModel.currentScene.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val speedMultiplier by viewModel.speedMultiplier.collectAsState()
    val sceneInfo by viewModel.sceneInfo.collectAsState()
    val customSignal by viewModel.customSignal.collectAsState()
    val selectedComponentId by viewModel.selectedComponent.collectAsState()

    var isKidMode by remember { mutableStateOf(true) }
    var isCameraCentered by remember { mutableStateOf(false) }
    var activeInspectorTab by remember { mutableStateOf(0) } // 0: State & Buses, 1: Simulator, 2: Signal Dispatcher

    val appBg = LightAppBackground
    val surfaceBg = LightSurfaceColor
    val textPrimary = LightTextPrimary
    val textSecondary = LightTextSecondary

    val kidDescriptions = mapOf(
        1 to "Waking Up! The Juice Box (PSU) sends electric energy down the roads to wake up the Chief Chef (CPU), Workbench (RAM), and Toy Chest (Storage)!",
        2 to "Morning Checkup! The Alarm Clock (BIOS) asks the Chief Chef (CPU) to check if the Workbench (RAM) and Painter (GPU) are healthy and ready to play!",
        3 to "Opening the Toy Chest! The computer copies the Helpful Teacher (OS Kernel) from the Big Toy Chest (Storage) onto the fast Workbench (RAM)!",
        4 to "Teacher Takes Charge! The OS Teacher wakes up the network postman and graphic painter so you can connect to the internet!",
        5 to "Ready to Play! The magic screen lights up with the desktop, waiting for your click!"
    )

    // Calibrated Camera Scale & Pan to guarantee ZERO clipping
    val (targetScale, targetX, targetY) = if (isCameraCentered) {
        Triple(1.0f, 0f, 0f)
    } else when (currentScene) {
        1 -> Triple(1.04f, 8f, 0f)
        2 -> Triple(1.05f, 0f, 0f)
        3 -> Triple(1.05f, -8f, 0f)
        4 -> Triple(1.04f, -4f, 0f)
        5 -> Triple(1.0f, 0f, 0f)
        else -> Triple(1.0f, 0f, 0f)
    }

    val animScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "camScale"
    )
    val animX by animateFloatAsState(
        targetValue = targetX,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "camX"
    )
    val animY by animateFloatAsState(
        targetValue = targetY,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "camY"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Startup Process (Part 1)", color = textPrimary) },
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
                RelationshipDiagram()

                Spacer(modifier = Modifier.height(10.dp))

                // Scene Title & Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surfaceBg),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sceneInfo.title,
                                color = ActiveComponent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
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
                            text = if (isKidMode) (kidDescriptions[currentScene] ?: "") else sceneInfo.description,
                            color = textPrimary,
                            fontSize = 12.sp,
                            fontStyle = if (isKidMode) FontStyle.Italic else FontStyle.Normal,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive Animation Canvas Container (height 380dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceBg)
                        .border(1.dp, ControlFlowColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .graphicsLayer {
                            scaleX = animScale
                            scaleY = animScale
                            translationX = animX
                            translationY = animY
                        }
                        .padding(8.dp)
                ) {
                    // Overlay bus animations when buses are active
                    if (sceneInfo.activeBuses.contains("power")) {
                        AnimatedPowerLine(
                            start = Offset(50f, 150f),
                            end = Offset(180f, 150f),
                            isActive = true
                        )
                    }

                    if (sceneInfo.activeBuses.contains("data")) {
                        AnimatedBusTransfer(
                            isActive = true,
                            start = Offset(260f, 100f),
                            end = Offset(140f, 100f),
                            color = DataFlowColor
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left Column: PSU
                            ComponentCanvasCard(
                                id = "psu",
                                label = if (isKidMode) "Juice Box" else "PSU (650W)",
                                isActive = sceneInfo.activeComponents.contains("psu"),
                                onClick = { viewModel.selectComponent("psu") },
                                modifier = Modifier
                                    .width(68.dp)
                                    .fillMaxHeight()
                            )

                            // Center: Motherboard PCB
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp)
                                    .fillMaxHeight()
                                    .border(1.5.dp, ActiveComponent.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                    .background(LightAppBackground)
                                    .padding(6.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(if (isKidMode) "Town PCB Map" else "Motherboard PCB", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        ClockPulseWaveform(
                                            isActive = sceneInfo.activeComponents.contains("cpu"),
                                            modifier = Modifier
                                                .width(55.dp)
                                                .height(14.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ComponentCanvasCard(
                                            id = "cpu",
                                            label = if (isKidMode) "Chief Chef" else "CPU",
                                            isActive = sceneInfo.activeComponents.contains("cpu"),
                                            onClick = { viewModel.selectComponent("cpu") },
                                            modifier = Modifier.size(60.dp)
                                        )

                                        ComponentCanvasCard(
                                            id = "ram",
                                            label = if (isKidMode) "Workbench" else "RAM (16GB)",
                                            isActive = sceneInfo.activeComponents.contains("ram"),
                                            onClick = { viewModel.selectComponent("ram") },
                                            modifier = Modifier.size(60.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        ComponentCanvasCard(
                                            id = "bios",
                                            label = if (isKidMode) "Alarm Clock" else "BIOS/UEFI",
                                            isActive = sceneInfo.activeComponents.contains("bios"),
                                            onClick = { viewModel.selectComponent("bios") },
                                            modifier = Modifier.height(44.dp)
                                        )

                                        if (currentScene >= 4) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            ComponentCanvasCard(
                                                id = "os",
                                                label = if (isKidMode) "Teacher OS" else "OS Kernel",
                                                isActive = sceneInfo.activeComponents.contains("os"),
                                                onClick = { viewModel.selectComponent("os") },
                                                modifier = Modifier.height(44.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Right Column: Storage, GPU, Network
                            Column(
                                modifier = Modifier
                                    .width(72.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                ComponentCanvasCard(
                                    id = "storage",
                                    label = if (isKidMode) "Toy Chest" else "Storage",
                                    isActive = sceneInfo.activeComponents.contains("storage"),
                                    onClick = { viewModel.selectComponent("storage") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                ComponentCanvasCard(
                                    id = "gpu",
                                    label = if (isKidMode) "Painter GPU" else "GPU (8GB)",
                                    isActive = sceneInfo.activeComponents.contains("gpu"),
                                    onClick = { viewModel.selectComponent("gpu") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                ComponentCanvasCard(
                                    id = "network",
                                    label = if (isKidMode) "Postman" else "NIC (Wi-Fi)",
                                    isActive = sceneInfo.activeComponents.contains("network"),
                                    onClick = { viewModel.selectComponent("network") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Bottom Row: Input & Output Peripherals
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ComponentCanvasCard(
                                id = "input",
                                label = if (isKidMode) "Your Hands" else "Input Device",
                                isActive = sceneInfo.activeComponents.contains("input"),
                                onClick = { viewModel.selectComponent("input") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            ComponentCanvasCard(
                                id = "output",
                                label = if (isKidMode) "Magic Window" else "Monitor Display",
                                isActive = sceneInfo.activeComponents.contains("output"),
                                onClick = { viewModel.selectComponent("output") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                ColorLegendBar()

                Spacer(modifier = Modifier.height(10.dp))

                // Clean Architecture Inspector Segmented Tab Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Buses & State", "Latency Simulator", "Bus Signal Dispatcher").forEachIndexed { idx, tabName ->
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

                // Clean Tab Content Panel
                when (activeInspectorTab) {
                    0 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = LightAppBackground),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, ControlFlowColor.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ARCH BUS & REGISTER STATE:", color = ControlFlowColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = sceneInfo.activeRegisterText,
                                        color = textPrimary,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                    1 -> PerformanceBenchmarkSimulator()
                    2 -> CustomBusSignalDispatcher(
                        activeSignal = customSignal,
                        onDispatchSignal = { busType -> viewModel.dispatchCustomBusSignal(busType) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OnboardingGuideCard()

            Spacer(modifier = Modifier.height(10.dp))

            // Fixed Controls Bar at the bottom
            AnimationControlsBar(
                isPlaying = isPlaying,
                speedMultiplier = speedMultiplier,
                onPlay = { viewModel.play() },
                onPause = { viewModel.pause() },
                onReset = { viewModel.reset() },
                onStepBack = { viewModel.previousScene() },
                onStepForward = { viewModel.nextScene() },
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
private fun ComponentCanvasCard(
    id: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val compData = SystemComponents.getById(id)
    val borderColor = if (isActive) ActiveComponent else InactiveComponent
    val cardBg = if (isActive) ActiveComponent.copy(alpha = 0.25f) else LightSurfaceColor
    val textCol = LightTextPrimary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(cardBg)
            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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

        if (isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(5.dp)
                    .background(ActiveComponent, CircleShape)
            )
        }
    }
}
