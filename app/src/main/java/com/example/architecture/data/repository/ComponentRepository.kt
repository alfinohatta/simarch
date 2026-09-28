package com.example.architecture.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.architecture.core.model.Characteristic
import com.example.architecture.core.model.ComponentCategory
import com.example.architecture.core.model.ComponentData
import com.example.architecture.core.model.StorageComparisonRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ComponentRepository {
    fun getAllComponents(): Flow<List<ComponentData>>
    fun getComponentById(id: String): Flow<ComponentData?>
    fun searchComponents(query: String, category: ComponentCategory): Flow<List<ComponentData>>
}

class ComponentRepositoryImpl : ComponentRepository {

    private val storageComparisonData = listOf(
        StorageComparisonRow("HDD (Hard Disk Drive)", "2TB", "SATA III", "~150 MB/s"),
        StorageComparisonRow("SSD (Solid State Drive)", "1TB", "SATA III", "~550 MB/s"),
        StorageComparisonRow("NVMe SSD", "1TB", "PCIe 4.0", "~7000 MB/s")
    )

    private val storageExplanationText = "An HDD stores data on spinning magnetic platters read by a moving mechanical arm, which is inherently slower than the flash memory chips used in an SSD, and an NVMe SSD is faster still because it connects directly over the high-bandwidth PCIe bus instead of the older SATA interface."

    private val components = listOf(
        ComponentData(
            id = "psu",
            name = "Power Supply Unit (PSU)",
            icon = Icons.Default.Power,
            category = ComponentCategory.CORE,
            mainFunction = "Converts household AC power into stable DC voltage rails (+12V, +5V, +3.3V) required by hardware components.",
            architectureRole = "Energy Source — Powers motherboard power planes, VRMs, CPU, RAM, and storage drives.",
            simpleAnalogy = "The Computer's Juice Box — Sends electric energy down the roads so everyone can wake up and play!",
            characteristics = listOf(
                Characteristic("Capacity", "650 Watts"),
                Characteristic("Voltage Rails", "+12V (54A), +5V (20A), +3.3V (20A)"),
                Characteristic("Efficiency", "80 PLUS Gold (≥90% efficiency)")
            )
        ),
        ComponentData(
            id = "motherboard",
            name = "Motherboard",
            icon = Icons.Default.DeveloperBoard,
            category = ComponentCategory.CORE,
            mainFunction = "Central printed circuit board (PCB) housing system buses (Control, Address, Data), CPU socket, RAM DIMM slots, and expansion slots.",
            architectureRole = "Interconnect Highway — Transmits clock signals, bus requests, and power across chips.",
            simpleAnalogy = "The Big Town Map — A giant green board with copper roads so all computer friends can hold hands and talk!",
            characteristics = listOf(
                Characteristic("Chipset", "Intel Z690 Express"),
                Characteristic("Socket / Bus", "LGA1700 / PCIe 4.0 & 5.0"),
                Characteristic("Memory Traces", "4x DDR4 Dual-Channel"),
                Characteristic("Power VRM", "16+1 Phase Digital VRM")
            )
        ),
        ComponentData(
            id = "cpu",
            name = "Central Processing Unit (CPU)",
            icon = Icons.Default.Memory,
            category = ComponentCategory.CORE,
            mainFunction = "Executes instructions via Fetch-Decode-Execute cycles; contains Control Unit (CU), Arithmetic Logic Unit (ALU), and Registers.",
            architectureRole = "System Brain — Manages program counter, ALU execution, and register writebacks.",
            simpleAnalogy = "The Chief Chef / Little Brain — Super fast! Solves 3.5 billion math puzzles every single second!",
            characteristics = listOf(
                Characteristic("Clock Frequency", "3.5 GHz (3.5 billion cycles/sec)"),
                Characteristic("Cores / Threads", "8 Cores / 16 Threads"),
                Characteristic("Registers", "PC, MAR, MBR, IR, ACC, General Purpose"),
                Characteristic("Cache Hierarchy", "L1 (64KB), L2 (512KB), L3 (16MB Shared)")
            )
        ),
        ComponentData(
            id = "ram",
            name = "Random Access Memory (RAM)",
            icon = Icons.Default.ViewArray,
            category = ComponentCategory.MEMORY,
            mainFunction = "Volatile primary storage holding active instructions and data currently needed by the CPU.",
            architectureRole = "Primary Memory — Provides nanosecond read/write access to instructions loaded from storage.",
            simpleAnalogy = "The Fast Workbench — A super fast desk where the CPU keeps toys it is playing with RIGHT NOW!",
            characteristics = listOf(
                Characteristic("Capacity", "16 GB"),
                Characteristic("Type", "DDR4 SDRAM"),
                Characteristic("Data Bus Width", "64-bit per channel"),
                Characteristic("Bandwidth", "25,600 MB/s (PC4-25600)")
            )
        ),
        ComponentData(
            id = "storage",
            name = "Storage Device",
            icon = Icons.Default.Storage,
            category = ComponentCategory.MEMORY,
            mainFunction = "Non-volatile secondary storage retaining operating system files, programs, and user data permanently.",
            architectureRole = "Secondary Memory — Holds OS kernel, bootloader binary, and application files.",
            simpleAnalogy = "The Big Toy Chest — A huge toy box where all your games and photos stay safe, even when the computer turns off!",
            characteristics = emptyList(),
            isStorageDevice = true,
            storageComparison = storageComparisonData,
            storageExplanation = storageExplanationText
        ),
        ComponentData(
            id = "gpu",
            name = "Graphics Processing Unit (GPU)",
            icon = Icons.Default.Tv,
            category = ComponentCategory.CORE,
            mainFunction = "Massively parallel processor optimized for rendering frame buffers, 3D graphics, and matrix mathematical execution.",
            architectureRole = "Co-Processor — Receives draw commands from CPU and outputs pixel color matrices to display.",
            simpleAnalogy = "The Fast Artist / Painter — Paints millions of colorful pictures every second and puts them on your screen!",
            characteristics = listOf(
                Characteristic("VRAM", "8 GB GDDR6"),
                Characteristic("Memory Bus", "256-bit interface"),
                Characteristic("Pipeline Cores", "2560 CUDA / Stream Cores"),
                Characteristic("Display Bus", "PCIe 4.0 x16")
            )
        ),
        ComponentData(
            id = "bios",
            name = "BIOS / UEFI Firmware",
            icon = Icons.Default.DeveloperMode,
            category = ComponentCategory.FIRMWARE_OS,
            mainFunction = "Non-volatile firmware chip containing system initialization code, POST diagnostic, and bootloader launcher.",
            architectureRole = "Bootstrapper — Executes at reset vector 0xFFFFFFF0 to initialize hardware before OS.",
            simpleAnalogy = "The Morning Alarm Clock — The very first friend to wake up and check if everyone is healthy!",
            characteristics = listOf(
                Characteristic("ROM Type", "SPI Flash ROM (128 Mb)"),
                Characteristic("Firmware Standard", "UEFI 2.8 / Legacy BIOS Compatible"),
                Characteristic("Function", "Hardware POST, ACPI, NVRAM Settings")
            )
        ),
        ComponentData(
            id = "input",
            name = "Input Devices",
            icon = Icons.Default.Keyboard,
            category = ComponentCategory.PERIPHERALS,
            mainFunction = "Peripheral hardware converting user physical actions into electrical interrupt signals (IRQ) and data packets.",
            architectureRole = "Signal Ingestion — Generates hardware interrupts processed by OS device drivers.",
            simpleAnalogy = "Your Hands & Fingers — How you tap buttons, move the mouse, or whisper into the microphone!",
            characteristics = listOf(
                Characteristic("Devices", "Keyboard, Mouse, Scanner, Touchpad"),
                Characteristic("Bus Controller", "USB 3.0 / HID Host Controller"),
                Characteristic("Interrupt Type", "MSI / Hardware IRQ Line")
            )
        ),
        ComponentData(
            id = "output",
            name = "Output Devices",
            icon = Icons.Default.Monitor,
            category = ComponentCategory.PERIPHERALS,
            mainFunction = "Peripheral hardware presenting processed digital data into human-perceivable visual, audio, or physical form.",
            architectureRole = "Information Display — Converts frame buffers into video refresh scans.",
            simpleAnalogy = "The Magic Window / Screen — Shows you the games, cartoons, and pictures the computer paints!",
            characteristics = listOf(
                Characteristic("Primary Output", "1080p 60Hz HDMI/DisplayPort Monitor"),
                Characteristic("Secondary", "Audio DAC / Speakers, Printers"),
                Characteristic("Signal Interface", "HDMI 2.0 / DisplayPort 1.4")
            )
        ),
        ComponentData(
            id = "network",
            name = "Network Interface (NIC)",
            icon = Icons.Default.Wifi,
            category = ComponentCategory.PERIPHERALS,
            mainFunction = "Hardware interface managing packet serialization/deserialization over local LAN or wireless radio frequencies.",
            architectureRole = "Data Transmission — Encapsulates TCP/IP packets between network stack and physical medium.",
            simpleAnalogy = "The Postman / Letter Carrier — Sends and receives letters across the internet to your friends!",
            characteristics = listOf(
                Characteristic("Wireless", "Wi-Fi 6 (802.11ax 2.4/5GHz)"),
                Characteristic("Wired", "Gigabit Ethernet (1000BASE-T)"),
                Characteristic("Protocol Stack", "Hardware MAC & PHY Layer")
            )
        ),
        ComponentData(
            id = "os",
            name = "Operating System (OS Kernel)",
            icon = Icons.Default.DesktopWindows,
            category = ComponentCategory.FIRMWARE_OS,
            mainFunction = "System software managing CPU process scheduling, virtual memory paging, file system, and device drivers.",
            architectureRole = "Hardware Mediator — Provides abstraction layer so apps access CPU and RAM safely.",
            simpleAnalogy = "The Helpful Teacher / Boss — Guides all computer friends so nobody bumps into each other!",
            characteristics = listOf(
                Characteristic("Core Subsystems", "Kernel, Process Scheduler, VMM, VFS, Drivers"),
                Characteristic("Execution Mode", "Kernel Mode (Ring 0) vs User Mode (Ring 3)"),
                Characteristic("Examples", "Linux Kernel, Windows NT, macOS Darwin")
            )
        )
    )

    override fun getAllComponents(): Flow<List<ComponentData>> = flow {
        emit(components)
    }

    override fun getComponentById(id: String): Flow<ComponentData?> = flow {
        emit(components.find { it.id == id })
    }

    override fun searchComponents(query: String, category: ComponentCategory): Flow<List<ComponentData>> = flow {
        val filtered = components.filter { comp ->
            val matchesCategory = (category == ComponentCategory.ALL || comp.category == category)
            val matchesQuery = query.isBlank() || comp.name.contains(query, ignoreCase = true) || comp.mainFunction.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
        emit(filtered)
    }
}
