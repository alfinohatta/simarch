package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AppOption(val id: String, val name: String, val iconName: String)

data class ExecutionStepInfo(
    val stepNumber: Int,
    val phaseName: String,             // "Fetch", "Decode", "Execute", "Memory", "Writeback"
    val title: String,
    val description: String,
    val activeBuses: Set<String>,      // "control", "address", "data"
    val activeComponents: Set<String>,
    val registerState: String
)

class ExecutionViewModel : ViewModel() {
    val apps = listOf(
        AppOption("chrome", "Google Chrome", "Chrome"),
        AppOption("word", "Microsoft Word", "Word"),
        AppOption("calculator", "Calculator", "Calc"),
        AppOption("python", "Python IDE", "Python")
    )

    private val _selectedApp = MutableStateFlow(apps[0])
    val selectedApp: StateFlow<AppOption> = _selectedApp

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _speedMultiplier = MutableStateFlow(1f)
    val speedMultiplier: StateFlow<Float> = _speedMultiplier

    private val _selectedComponent = MutableStateFlow<String?>(null)
    val selectedComponent: StateFlow<String?> = _selectedComponent

    private val _stepInfo = MutableStateFlow(getStepInfoForStep(1, apps[0].name))
    val stepInfo: StateFlow<ExecutionStepInfo> = _stepInfo

    private var playbackJob: Job? = null

    fun selectApp(app: AppOption) {
        _selectedApp.value = app
        reset()
    }

    fun play() {
        if (_isPlaying.value) return
        _isPlaying.value = true
        startAutoPlayback()
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
    }

    fun reset() {
        pause()
        _currentStep.value = 1
        _stepInfo.value = getStepInfoForStep(1, _selectedApp.value.name)
    }

    fun nextStep() {
        if (_currentStep.value < 9) {
            _currentStep.value++
            _stepInfo.value = getStepInfoForStep(_currentStep.value, _selectedApp.value.name)
        }
    }

    fun previousStep() {
        if (_currentStep.value > 1) {
            _currentStep.value--
            _stepInfo.value = getStepInfoForStep(_currentStep.value, _selectedApp.value.name)
        }
    }

    fun setSpeed(multiplier: Float) {
        _speedMultiplier.value = multiplier
    }

    fun selectComponent(id: String?) {
        _selectedComponent.value = id
    }

    private fun startAutoPlayback() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (_isPlaying.value && _currentStep.value <= 9) {
                _stepInfo.value = getStepInfoForStep(_currentStep.value, _selectedApp.value.name)
                val stepDuration = (1400L / _speedMultiplier.value).toLong()
                delay(stepDuration)
                if (_currentStep.value < 9) {
                    _currentStep.value++
                } else {
                    _isPlaying.value = false
                    break
                }
            }
        }
    }

    private fun getStepInfoForStep(step: Int, appName: String): ExecutionStepInfo = when (step) {
        1 -> ExecutionStepInfo(
            stepNumber = 1,
            phaseName = "User Input Signal",
            title = "Step 1: Physical User Input Event",
            description = "User clicks $appName icon. Mouse/Keyboard generates Hardware Interrupt (IRQ) signal sent to USB Host Controller.",
            activeBuses = setOf("control"),
            activeComponents = setOf("input"),
            registerState = "IRQ Line: 0x01 | USB Host: Event Packet Captured"
        )
        2 -> ExecutionStepInfo(
            stepNumber = 2,
            phaseName = "OS Request Ingestion",
            title = "Step 2: Operating System Interruption & Dispatch",
            description = "OS Kernel interrupt handler catches IRQ event, identifies launch command for $appName, and issues virtual file system read.",
            activeBuses = setOf("control", "address"),
            activeComponents = setOf("input", "os"),
            registerState = "OS Kernel: Interrupt Vector 0x21 | Process: Allocating PID 4092"
        )
        3 -> ExecutionStepInfo(
            stepNumber = 3,
            phaseName = "Storage File Lookup",
            title = "Step 3: Storage Drive Binary File Location",
            description = "OS File System navigates partition table on Storage drive to locate executable binary '$appName.exe' and dependencies.",
            activeBuses = setOf("control", "address"),
            activeComponents = setOf("os", "storage"),
            registerState = "Storage Bus: SATA/NVMe PCIe Read Command | LBA Offset: 0x004A8100"
        )
        4 -> ExecutionStepInfo(
            stepNumber = 4,
            phaseName = "RAM DMA Page Loading",
            title = "Step 4: Direct Memory Access (Storage → RAM)",
            description = "Program executable binary and code sections are copied from Storage into RAM pages for high-speed CPU execution.",
            activeBuses = setOf("control", "data", "address"),
            activeComponents = setOf("storage", "ram"),
            registerState = "RAM Paging: Allocation 256MB Code Pages | Bus Transfer Speed: ~7000 MB/s"
        )
        5 -> ExecutionStepInfo(
            stepNumber = 5,
            phaseName = "Instruction Fetch (IF) & Decode",
            title = "Step 5: CPU Instruction Fetch (PC → MAR → MBR → IR)",
            description = "CPU Control Unit sets Program Counter (PC) to $appName entry point address in RAM, fetches instruction into Instruction Register (IR), and decodes opcodes.",
            activeBuses = setOf("control", "address", "data"),
            activeComponents = setOf("ram", "cpu"),
            registerState = "PC: 0x00401000 | MAR: 0x00401000 | IR: MOV EAX, [0x00402000]"
        )
        6 -> ExecutionStepInfo(
            stepNumber = 6,
            phaseName = "ALU Execution & GPU Commands",
            title = "Step 6: CPU ALU Execution & GPU Render Draw Call",
            description = "CPU Arithmetic Logic Unit executes instructions, processes program data, and transmits DirectX/Vulkan graphics commands to GPU.",
            activeBuses = setOf("control", "data"),
            activeComponents = setOf("cpu", "gpu"),
            registerState = "EAX: 0x00000001 | ALU OP: ADD/DRAW | PCIe x16 Bus: Dispatching DrawCalls"
        )
        7 -> ExecutionStepInfo(
            stepNumber = 7,
            phaseName = "GPU Rasterization",
            title = "Step 7: GPU Shader Execution & Frame Buffer Render",
            description = "GPU parallel cores process geometry, render UI elements, and write final pixel matrix into Video RAM (VRAM) Frame Buffer.",
            activeBuses = setOf("control", "data"),
            activeComponents = setOf("gpu"),
            registerState = "GPU Pipeline: Vertex & Fragment Shaders | VRAM Framebuffer: 1920x1080 ARGB"
        )
        8 -> ExecutionStepInfo(
            stepNumber = 8,
            phaseName = "Network Socket Fetch",
            title = "Step 8: Network Socket Transmission (If Required)",
            description = "Network Interface sends TCP/IP HTTP requests over Wi-Fi/Ethernet to fetch remote web pages, cloud data, or update feeds.",
            activeBuses = setOf("control", "data"),
            activeComponents = setOf("network", "cpu"),
            registerState = "NIC: Socket ESTABLISHED | TCP Packet RX/TX | IP: 192.168.1.100"
        )
        9 -> ExecutionStepInfo(
            stepNumber = 9,
            phaseName = "Display Output Writeback",
            title = "Step 9: Monitor Frame Display (V-Sync)",
            description = "GPU outputs HDMI/DisplayPort video signal from VRAM to Monitor. $appName window is rendered on screen for user interaction.",
            activeBuses = setOf("data"),
            activeComponents = setOf("output", "gpu"),
            registerState = "Monitor Output: 60Hz Refresh | Status: App Active & Interactive"
        )
        else -> ExecutionStepInfo(1, "", "", "", emptySet(), emptySet(), "")
    }

    init {
        _stepInfo.value = getStepInfoForStep(1, apps[0].name)
    }
}
