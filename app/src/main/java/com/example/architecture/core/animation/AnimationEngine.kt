package com.example.architecture.core.animation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.architecture.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SignalType(val color: Color, val label: String) {
    POWER(PowerFlowColor, "POWER RAIL (+12V/+5V/+3.3V)"),
    DATA(DataFlowColor, "DATA BUS (DMA/SDRAM)"),
    CONTROL(ControlFlowColor, "CONTROL BUS (IRQ/SYSCALL)"),
    ADDRESS(ActiveComponent, "ADDRESS BUS (MAR/PC)")
}

enum class HardwareState {
    OFF,
    POWERING_UP,
    POST_DIAGNOSTIC,
    FETCHING_INSTRUCTION,
    DECODING_OPCODE,
    ALU_EXECUTING,
    MEMORY_WRITEBACK,
    SYSTEM_IDLE
}

@Immutable
data class CameraState(
    val scale: Float = 1.0f,       // Zoom level (1.0x to 2.5x)
    val offsetX: Float = 0.0f,     // Pan Translation X in pixels
    val offsetY: Float = 0.0f,     // Pan Translation Y in pixels
    val isAutoFocused: Boolean = false
)

@Immutable
data class BusParticle(
    val id: String,
    val signalType: SignalType,
    val startPos: Offset,
    val endPos: Offset,
    val controlPos: Offset? = null,
    val progress: Float,
    val speed: Float = 1.0f
)

@Immutable
data class AnimationEngineState(
    val isPlaying: Boolean = false,
    val currentFrameMs: Long = 0L,
    val speedMultiplier: Float = 1.0f,
    val clockPulseHigh: Boolean = false,
    val activeHardwareState: HardwareState = HardwareState.OFF,
    val cameraState: CameraState = CameraState(),
    val activeParticles: List<BusParticle> = emptyList()
)

class SystemAnimationEngine(
    private val scope: CoroutineScope
) {
    private val _engineState = MutableStateFlow(AnimationEngineState())
    val engineState: StateFlow<AnimationEngineState> = _engineState.asStateFlow()

    private var animationJob: Job? = null
    private val frameIntervalMs = 30L

    fun play() {
        if (_engineState.value.isPlaying) return
        _engineState.value = _engineState.value.copy(isPlaying = true)
        startEngineLoop()
    }

    fun pause() {
        _engineState.value = _engineState.value.copy(isPlaying = false)
        animationJob?.cancel()
    }

    fun reset() {
        pause()
        _engineState.value = AnimationEngineState()
    }

    fun setSpeed(multiplier: Float) {
        _engineState.value = _engineState.value.copy(speedMultiplier = multiplier)
    }

    fun setHardwareState(state: HardwareState) {
        _engineState.value = _engineState.value.copy(activeHardwareState = state)
        autoFocusCameraForState(state)
    }

    fun updateCameraPanAndZoom(scaleDelta: Float, panX: Float, panY: Float) {
        val currentCam = _engineState.value.cameraState
        val newScale = (currentCam.scale * scaleDelta).coerceIn(1.0f, 2.5f)
        val newX = (currentCam.offsetX + panX).coerceIn(-200f, 200f)
        val newY = (currentCam.offsetY + panY).coerceIn(-200f, 200f)

        _engineState.value = _engineState.value.copy(
            cameraState = CameraState(scale = newScale, offsetX = newX, offsetY = newY, isAutoFocused = false)
        )
    }

    fun resetCamera() {
        _engineState.value = _engineState.value.copy(
            cameraState = CameraState(scale = 1.0f, offsetX = 0.0f, offsetY = 0.0f, isAutoFocused = false)
        )
    }

    fun focusCameraOnTarget(scale: Float, offsetX: Float, offsetY: Float) {
        _engineState.value = _engineState.value.copy(
            cameraState = CameraState(scale = scale, offsetX = offsetX, offsetY = offsetY, isAutoFocused = true)
        )
    }

    private fun autoFocusCameraForState(state: HardwareState) {
        val targetCam = when (state) {
            HardwareState.POWERING_UP -> CameraState(scale = 1.15f, offsetX = 60f, offsetY = 20f, isAutoFocused = true)
            HardwareState.POST_DIAGNOSTIC -> CameraState(scale = 1.25f, offsetX = 0f, offsetY = -10f, isAutoFocused = true)
            HardwareState.FETCHING_INSTRUCTION -> CameraState(scale = 1.30f, offsetX = -30f, offsetY = -30f, isAutoFocused = true)
            HardwareState.DECODING_OPCODE -> CameraState(scale = 1.35f, offsetX = -40f, offsetY = -30f, isAutoFocused = true)
            HardwareState.ALU_EXECUTING -> CameraState(scale = 1.25f, offsetX = -20f, offsetY = -10f, isAutoFocused = true)
            HardwareState.MEMORY_WRITEBACK -> CameraState(scale = 1.20f, offsetX = -60f, offsetY = 20f, isAutoFocused = true)
            HardwareState.SYSTEM_IDLE -> CameraState(scale = 1.0f, offsetX = 0f, offsetY = 0f, isAutoFocused = false)
            else -> CameraState(scale = 1.0f, offsetX = 0f, offsetY = 0f, isAutoFocused = false)
        }
        _engineState.value = _engineState.value.copy(cameraState = targetCam)
    }

    private fun startEngineLoop() {
        animationJob?.cancel()
        animationJob = scope.launch {
            while (_engineState.value.isPlaying) {
                val speed = _engineState.value.speedMultiplier
                val currentMs = _engineState.value.currentFrameMs + (frameIntervalMs * speed).toLong()
                val clockHigh = (currentMs / 500) % 2 == 0L

                val updatedParticles = _engineState.value.activeParticles.map { p ->
                    val newProgress = p.progress + (0.02f * speed)
                    if (newProgress >= 1f) p.copy(progress = 0f) else p.copy(progress = newProgress)
                }

                _engineState.value = _engineState.value.copy(
                    currentFrameMs = currentMs,
                    clockPulseHigh = clockHigh,
                    activeParticles = updatedParticles
                )

                delay(frameIntervalMs)
            }
        }
    }
}
