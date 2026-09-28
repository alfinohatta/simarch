package com.example.architecture.data.repository

import com.example.architecture.core.model.DefenseQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface QuestionRepository {
    fun getQuestions(): Flow<List<DefenseQuestion>>
}

class QuestionRepositoryImpl : QuestionRepository {
    private val questions = listOf(
        DefenseQuestion(
            1,
            "Fundamentals",
            "What is computer architecture?",
            "Computer architecture is the conceptual design and fundamental operational structure of a computer system — how its hardware components (CPU, memory, storage, input/output devices) are organized and how they interact with software to execute instructions and process data.",
            listOf(
                "The mechanical casing and aesthetic design of a computer tower.",
                "The conceptual design and operational structure of hardware and software interaction.",
                "The protocol used for internet wireless communication.",
                "The arrangement of files inside an operating system's file system."
            ),
            1
        ),
        DefenseQuestion(
            2,
            "Fundamentals",
            "What are the main components of a computer?",
            "The main components are the Central Processing Unit (CPU), Random Access Memory (RAM), a storage device (HDD/SSD/NVMe SSD), the motherboard, the power supply unit (PSU), a graphics processing unit (GPU), the BIOS/UEFI firmware, input devices, output devices, a network interface, and the operating system that manages all of them.",
            listOf(
                "CPU, RAM, Storage, Motherboard, PSU, GPU, BIOS/UEFI, Input/Output, Network Interface, OS.",
                "Monitor, Keyboard, Mouse, and Printer only.",
                "Power Cable, HDMI Cable, Hard Drive, and Transistors.",
                "Kernel, Web Browser, Word Processor, and Antivirus."
            ),
            0
        ),
        DefenseQuestion(
            3,
            "CPU & Processors",
            "What is the main function of the CPU?",
            "The CPU executes program instructions, performs arithmetic and logic calculations, and coordinates and controls the operation of the other components in the system.",
            listOf(
                "Stores files permanently when power is turned off.",
                "Converts AC wall power into DC voltage for components.",
                "Executes program instructions, performs math/logic, and coordinates all components.",
                "Renders 3D graphics directly to the display output."
            ),
            2
        ),
        DefenseQuestion(
            4,
            "Memory Hierarchy",
            "What is the difference between RAM and permanent storage?",
            "RAM is volatile, very fast memory that temporarily holds data and instructions the CPU is actively using, and it loses its contents when power is removed. Permanent storage (HDD/SSD) is non-volatile, slower, and retains data even when the computer is turned off; it is used to store the operating system, applications, and files long-term.",
            listOf(
                "RAM stores files permanently, storage is volatile.",
                "RAM is volatile and fast for active CPU use; storage is non-volatile and slower for long-term files.",
                "RAM is only used for graphics, while storage is used for arithmetic operations.",
                "There is no difference; RAM and SSD are identical technology."
            ),
            1
        ),
        DefenseQuestion(
            5,
            "Boot Sequence",
            "What happens immediately after the Power button is pressed?",
            "The Power Supply Unit begins delivering electrical power to the motherboard, which distributes it to the CPU, RAM, storage, and GPU. The CPU resets and immediately begins fetching its very first instruction from the BIOS/UEFI firmware chip on the motherboard.",
            listOf(
                "The Operating System immediately renders the desktop.",
                "The PSU delivers power to motherboard, CPU resets and fetches instructions from BIOS/UEFI.",
                "The storage device copies all user files directly into GPU video memory.",
                "The monitor displays an application window."
            ),
            1
        ),
        DefenseQuestion(
            6,
            "Boot Sequence",
            "What is the function of BIOS/UEFI?",
            "BIOS/UEFI is firmware stored on the motherboard that initializes and tests the computer's hardware immediately after power-on, runs the Power-On Self-Test (POST), and locates and hands control over to the bootloader on the selected boot device.",
            listOf(
                "Compiles high-level C++ code into executable binaries.",
                "Firmware that initializes hardware, runs POST, and hands control to bootloader.",
                "Manages network IP addresses and routing protocols.",
                "Protects the system against internet malware infection."
            ),
            1
        ),
        DefenseQuestion(
            7,
            "Boot Sequence",
            "What is POST and why is it important?",
            "POST (Power-On Self-Test) is a diagnostic sequence run by the BIOS/UEFI that checks that essential hardware components — RAM, storage, GPU, and others — are present and functioning correctly before the system proceeds to boot the operating system. It is important because it catches hardware failures early, before an unreliable component could cause data corruption or a crash later in the boot process.",
            listOf(
                "An internet protocol test conducted by the Wi-Fi card.",
                "Diagnostic hardware check by BIOS/UEFI before OS boots to catch hardware failures early.",
                "A memory cleanup routine performed by the operating system every hour.",
                "A stress test executed by the graphics card to test maximum clock speed."
            ),
            1
        ),
        DefenseQuestion(
            8,
            "Boot Sequence",
            "How does the computer find and load the operating system?",
            "After POST completes successfully, the BIOS/UEFI searches the configured boot device (typically the primary storage drive) for a valid bootloader. Once found, control is handed to the bootloader, which locates the operating system's kernel files on storage and copies them into RAM, after which the CPU begins executing the OS kernel directly from RAM.",
            listOf(
                "The CPU downloads the OS from the cloud during every boot.",
                "BIOS/UEFI finds bootloader on storage -> bootloader copies OS kernel into RAM -> CPU executes OS from RAM.",
                "The RAM transmits the OS directly into the PSU.",
                "The GPU loads the OS directly from its internal VRAM."
            ),
            1
        ),
        DefenseQuestion(
            9,
            "Memory Hierarchy",
            "Why is RAM necessary for running programs?",
            "The CPU can read and write data in RAM enormously faster than it can access data directly from permanent storage. Because a program's instructions must be fed to the CPU continuously and quickly while it runs, those instructions (and the data the program is working with) are copied into RAM first, so the CPU is never waiting on the much slower storage device during normal execution.",
            listOf(
                "Because storage devices cannot store binary numbers.",
                "RAM is much faster than storage, allowing the CPU to fetch instructions continuously without delay.",
                "RAM is required to prevent the motherboard from overheating.",
                "Without RAM, input devices like keyboards cannot register key presses."
            ),
            1
        ),
        DefenseQuestion(
            10,
            "Operating System",
            "What is the role of the operating system?",
            "The operating system manages the computer's running processes, allocates and manages memory, manages the file system, manages device drivers, and provides the user interface. It also mediates every application's access to the underlying hardware, so applications never have to talk to hardware directly.",
            listOf(
                "Converts electrical current into digital clock cycles.",
                "Manages processes, memory, file system, drivers, UI, and mediates application access to hardware.",
                "Physically holds the CPU and RAM chips in place on the circuit board.",
                "Generates HDMI video signals directly from raw data."
            ),
            1
        ),
        DefenseQuestion(
            11,
            "CPU & Buses",
            "How does the CPU communicate with RAM?",
            "The CPU communicates with RAM over the memory bus on the motherboard, continuously sending memory addresses it wants to read from or write to, and RAM returns or stores the corresponding data; this is a very high-speed, bidirectional, and constant exchange while any program is running.",
            listOf(
                "Over high-speed bidirectional memory bus sending addresses and transferring data.",
                "Via wireless Bluetooth signals transmitted across the motherboard.",
                "Through HDMI cables connected to the back of the case.",
                "By writing temporary files to the SSD flash memory."
            ),
            0
        ),
        DefenseQuestion(
            12,
            "Program Execution",
            "What happens when an application is launched?",
            "When the user launches an application, the input is received by the operating system, which locates the application's executable files on the storage device, copies those files into RAM, and instructs the CPU to begin executing the program's instructions from RAM. If the application produces graphical output, the CPU sends rendering commands to the GPU, and the final image is displayed on the monitor.",
            listOf(
                "Input received by OS -> OS locates executable on storage -> files copied to RAM -> CPU executes from RAM -> GPU renders to monitor.",
                "The application executes directly inside the SSD storage without involving RAM.",
                "The BIOS/UEFI restarts the entire computer to load the app.",
                "The PSU increases voltage to maximum to force the monitor to display the app."
            ),
            0
        ),
        DefenseQuestion(
            13,
            "Memory Hierarchy",
            "Why are program files copied from storage into RAM?",
            "Storage devices, even fast NVMe SSDs, are still significantly slower to read from repeatedly than RAM is. Copying a program's files into RAM before execution means the CPU can fetch instructions and data at RAM speed rather than storage speed, which is essential for the program to run smoothly.",
            listOf(
                "To free up space on the storage device for new files.",
                "Because RAM speed is vastly higher than storage, allowing smooth instruction fetching by CPU.",
                "Because storage devices can only execute text files, not binary files.",
                "To allow the network card to inspect the application for viruses."
            ),
            1
        ),
        DefenseQuestion(
            14,
            "GPU & Peripherals",
            "What is the role of the GPU?",
            "The GPU (Graphics Processing Unit) processes graphical data and renders the visual output shown on the monitor. It is specialized for performing many calculations in parallel, which makes it especially efficient at rendering images, video, and 3D graphics, and it can also accelerate certain non-graphical parallel workloads.",
            listOf(
                "Processes parallel calculations, renders visual graphics to monitor, and accelerates parallel tasks.",
                "Manages file paths and directory structures on hard drives.",
                "Supplies electric current to CPU and RAM slots.",
                "Stores the computer's firmware boot order."
            ),
            0
        ),
        DefenseQuestion(
            15,
            "GPU & Peripherals",
            "How do input devices communicate with the system?",
            "Input devices such as a keyboard, mouse, scanner, or microphone connect to the system through interfaces such as USB or Bluetooth. When the user interacts with an input device, it sends a signal that the operating system's device drivers translate into an event the system and applications can respond to.",
            listOf(
                "They connect via USB/Bluetooth; OS device drivers translate signals into events for apps.",
                "They write data directly into the BIOS chip memory.",
                "They bypass the OS and directly control CPU registers without drivers.",
                "They communicate strictly using optical light pulses."
            ),
            0
        ),
        DefenseQuestion(
            16,
            "Operating System",
            "What happens when several programs are running at the same time?",
            "The operating system's process management divides the CPU's time among the running programs, rapidly switching between them (a process called multitasking or time-slicing) so that, to the user, they all appear to run simultaneously. The OS's memory management also allocates a separate portion of RAM to each running program so their data does not interfere with one another.",
            listOf(
                "The computer buys more physical CPU cores dynamically.",
                "OS divides CPU time via multitasking/time-slicing and allocates isolated RAM regions to each program.",
                "Only the active program stays in RAM; all background apps are saved back to SSD.",
                "Programs queue up and execute strictly one by one in 10-minute intervals."
            ),
            1
        ),
        DefenseQuestion(
            17,
            "CPU & Processors",
            "Which characteristics influence CPU performance?",
            "Clock speed (measured in GHz), the number of cores and threads, the size of the CPU's cache (L1/L2/L3), and the CPU's underlying architecture all influence how quickly and efficiently a CPU can execute instructions.",
            listOf(
                "Monitor resolution, HDMI cable length, and power plug shape.",
                "Clock speed (GHz), core/thread count, cache size (L1/L2/L3), and architecture.",
                "Storage capacity in Terabytes and SATA bus speed.",
                "Wi-Fi frequency and network bandwidth speed."
            ),
            1
        ),
        DefenseQuestion(
            18,
            "Memory Hierarchy",
            "Which characteristics influence RAM performance?",
            "RAM performance is influenced by its capacity (how much data it can hold at once), its type/generation (e.g., DDR4 vs. DDR5), its frequency (measured in MHz, how fast it can be accessed), and its resulting data transfer speed (measured in MB/s).",
            listOf(
                "Capacity, generation (DDR4/DDR5), frequency (MHz), and transfer speed (MB/s).",
                "Fan speed and heat sink color.",
                "Number of USB ports on the front panel.",
                "Resolution of the attached display."
            ),
            0
        ),
        DefenseQuestion(
            19,
            "Memory Hierarchy",
            "What is the difference between HDD, SSD, and NVMe SSD?",
            "An HDD (Hard Disk Drive) stores data on spinning magnetic platters read by a moving mechanical arm, making it the slowest and cheapest per gigabyte of the three. An SSD (Solid State Drive) stores data in flash memory chips with no moving parts, making it much faster than an HDD, typically connecting over the SATA interface. An NVMe SSD is also flash-based but connects directly over the much higher-bandwidth PCIe bus instead of SATA, making it significantly faster still than a standard SATA SSD.",
            listOf(
                "HDD is mechanical/slowest; SATA SSD is flash-based; NVMe SSD uses flash over PCIe bus for maximum speed.",
                "HDD is the fastest; NVMe SSD is the slowest; SATA SSD is volatile.",
                "NVMe SSDs use spinning magnetic disks; HDDs use flash memory chips.",
                "There is no performance difference between them."
            ),
            0
        ),
        DefenseQuestion(
            20,
            "System Integration",
            "How do computer components work together as one system?",
            "Every component depends on the others: the PSU powers the motherboard, which physically connects and enables communication between the CPU, RAM, storage, and GPU; the BIOS/UEFI initializes this hardware and hands control to the operating system; the operating system then manages all running software and mediates every application's access to the hardware, coordinating input from input devices and directing output to output devices and the network. No single component performs a useful task in isolation — they form one integrated system that continuously exchanges instructions and data.",
            listOf(
                "Components work entirely independently without communicating.",
                "They form an integrated system continuously exchanging instructions and data managed by the OS.",
                "The GPU controls all motherboard hardware directly without an OS.",
                "The storage device powers all components directly through SATA cables."
            ),
            1
        )
    )

    override fun getQuestions(): Flow<List<DefenseQuestion>> = flow {
        emit(questions)
    }
}
