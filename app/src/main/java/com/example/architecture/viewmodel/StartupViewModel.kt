package com.example.architecture.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StartupSceneInfo(
    val sceneNumber: Int,
    val title: String,
    val description: String,
    val activeBuses: Set<String>,      // "power", "control", "data", "address"
    val activeComponents: Set<String>,
    val activeRegisterText: String
)

data class CustomBusSignal(
    val busType: String,            // "Control Bus", "Address Bus", "Data Bus"
    val source: String,
    val destination: String,
    val packetData: String
)

class StartupViewModel : ViewModel() {
    private val _currentScene = MutableStateFlow(1)
    val currentScene: StateFlow<Int> = _currentScene

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _speedMultiplier = MutableStateFlow(1f)
    val speedMultiplier: StateFlow<Float> = _speedMultiplier

    private val _selectedComponent = MutableStateFlow<String?>(null)
    val selectedComponent: StateFlow<String?> = _selectedComponent

    private val _customSignal = MutableStateFlow<CustomBusSignal?>(null)
    val customSignal: StateFlow<CustomBusSignal?> = _customSignal

    private val _sceneInfo = MutableStateFlow(getSceneInfoForNumber(1))
    val sceneInfo: StateFlow<StartupSceneInfo> = _sceneInfo

    private var playbackJob: Job? = null

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
        _currentScene.value = 1
        _sceneInfo.value = getSceneInfoForNumber(1)
        _customSignal.value = null
    }

    fun nextScene() {
        if (_currentScene.value < 5) {
            _currentScene.value++
            _sceneInfo.value = getSceneInfoForNumber(_currentScene.value)
        }
    }

    fun previousScene() {
        if (_currentScene.value > 1) {
            _currentScene.value--
            _sceneInfo.value = getSceneInfoForNumber(_currentScene.value)
        }
    }

    fun setSpeed(multiplier: Float) {
        _speedMultiplier.value = multiplier
    }

    fun selectComponent(id: String?) {
        _selectedComponent.value = id
        if (id != null) {
            _customSignal.value = createSignalForComponent(id)
        }
    }

    fun dispatchCustomBusSignal(type: String) {
        _customSignal.value = when (type) {
            "Address Bus" -> CustomBusSignal("Address Bus", "CPU (MAR)", "RAM Memory Decoder", "ADDR: 0x00401000")
            "Data Bus" -> CustomBusSignal("Data Bus", "Storage Drive", "RAM Code Pages", "DATA: 0x7F454C46 (ELF Binary)")
            "Control Bus" -> CustomBusSignal("Control Bus", "CPU Control Unit", "GPU Pipeline", "SIGNAL: MEM_READ / DRAW_CALL")
            else -> null
        }
    }

    private fun createSignalForComponent(id: String): CustomBusSignal = when (id) {
        "psu" -> CustomBusSignal("Power Rail", "PSU 650W", "Motherboard VRM", "RAIL: +12V DC @ 54A")
        "cpu" -> CustomBusSignal("Address Bus", "CPU (PC)", "RAM Memory Controller", "ADDR: 0x00401000 [FETCH]")
        "ram" -> CustomBusSignal("Data Bus", "RAM SDRAM", "CPU Cache L3", "DATA: 64-bit Word Vector")
        "storage" -> CustomBusSignal("Data Bus", "NVMe PCIe 4.0", "RAM Code Pages", "TRANSFER: ~7000 MB/s DMA")
        "gpu" -> CustomBusSignal("Control Bus", "GPU Shader Core", "VRAM Frame Buffer", "DRAW: 1080p Frame Render")
        "bios" -> CustomBusSignal("Control Bus", "BIOS ROM", "System Bus", "INIT: POST Diagnostic Check")
        "input" -> CustomBusSignal("Control Bus", "USB HID Host", "CPU Interrupt Line", "IRQ: Hardware Interrupt Line 0x01")
        "output" -> CustomBusSignal("Data Bus", "GPU VRAM", "Monitor Display", "SCAN: HDMI 2.0 60Hz Pixel Stream")
        "network" -> CustomBusSignal("Data Bus", "Wi-Fi 6 MAC", "OS Network Stack", "PACKET: TCP/IP Port 443 RX/TX")
        "os" -> CustomBusSignal("Control Bus", "OS Kernel Ring 0", "CPU Process Scheduler", "SYSCALL: Allocating PID 4092")
        else -> CustomBusSignal("System Bus", id.uppercase(), "System Interconnect", "STATUS: Active Signal")
    }

    private fun startAutoPlayback() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (_isPlaying.value && _currentScene.value <= 5) {
                _sceneInfo.value = getSceneInfoForNumber(_currentScene.value)
                val sceneDuration = getSceneDuration(_currentScene.value)
                val adjustedDuration = (sceneDuration / _speedMultiplier.value).toLong()
                delay(adjustedDuration)
                if (_currentScene.value < 5) {
                    _currentScene.value++
                } else {
                    _isPlaying.value = false
                    break
                }
            }
        }
    }

    private fun getSceneDuration(scene: Int): Long = when (scene) {
        1 -> 3500L
        2 -> 5000L
        3 -> 6500L
        4 -> 5000L
        5 -> 3000L
        else -> 3500L
    }

    private fun getSceneInfoForNumber(scene: Int): StartupSceneInfo = when (scene) {
        1 -> StartupSceneInfo(
            sceneNumber = 1,
            title = "Scene 1/5: Power Distribution Phase",
            description = "PSU converts AC power into +12V, +5V, +3.3V DC rails. Motherboard VRMs stabilize power and distribute it across traces to CPU, RAM, and Storage.",
            activeBuses = setOf("power"),
            activeComponents = setOf("psu", "motherboard", "cpu", "ram", "storage", "gpu"),
            activeRegisterText = "Power Rails: +12V (CPU/GPU) | +5V (Storage) | +3.3V (Logic Chips)"
        )
        2 -> StartupSceneInfo(
            sceneNumber = 2,
            title = "Scene 2/5: Firmware Fetch & POST Diagnostic",
            description = "CPU resets to vector 0xFFFFFFF0 and fetches initial instructions from BIOS/UEFI ROM. BIOS executes Power-On Self-Test (POST) to verify RAM, Storage, and GPU.",
            activeBuses = setOf("power", "control", "address"),
            activeComponents = setOf("psu", "motherboard", "cpu", "bios", "ram", "storage", "gpu", "output"),
            activeRegisterText = "CPU Reset Vector: 0xFFFFFFF0 | Control Bus: READ_ROM | Status: POST_OK"
        )
        3 -> StartupSceneInfo(
            sceneNumber = 3,
            title = "Scene 3/5: Bootloader & OS Kernel Transfer",
            description = "BIOS locates the bootloader on Storage. OS Kernel binary is transferred from Storage to RAM over Data Bus (DMA/SATA/PCIe) for direct CPU execution.",
            activeBuses = setOf("power", "control", "data", "address"),
            activeComponents = setOf("psu", "motherboard", "cpu", "bios", "storage", "ram", "gpu", "output"),
            activeRegisterText = "Data Bus Transfer: Storage → RAM | CPU Mode: Real Mode → Long Mode"
        )
        4 -> StartupSceneInfo(
            sceneNumber = 4,
            title = "Scene 4/5: OS Kernel Takeover & Driver Load",
            description = "BIOS hands control over to OS Kernel in RAM. OS initializes Process Scheduler, Virtual Memory Manager, Device Drivers (GPU/Network), and Network Interface.",
            activeBuses = setOf("power", "control", "data"),
            activeComponents = setOf("psu", "motherboard", "cpu", "bios", "os", "ram", "storage", "gpu", "network", "output"),
            activeRegisterText = "Control Handover: BIOS → Kernel (Ring 0) | Drivers: GPU, NIC, USB, File System"
        )
        5 -> StartupSceneInfo(
            sceneNumber = 5,
            title = "Scene 5/5: System Execution Idle & Interrupt Loop",
            description = "Operating System renders Graphical Desktop to Monitor via GPU frame buffer. CPU enters idle loop, waiting for hardware interrupts (IRQ) from Keyboard or Mouse.",
            activeBuses = setOf("power", "control", "data"),
            activeComponents = setOf("psu", "motherboard", "cpu", "ram", "storage", "gpu", "bios", "input", "output", "network", "os"),
            activeRegisterText = "System Status: IDLE / READY | CPU Register: HLT / Awaiting IRQ Interrupts"
        )
        else -> StartupSceneInfo(1, "", "", emptySet(), emptySet(), "")
    }

    init {
        _sceneInfo.value = getSceneInfoForNumber(1)
    }
}
