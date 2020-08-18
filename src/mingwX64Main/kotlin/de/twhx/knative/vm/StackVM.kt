package de.twhx.knative.vm

/**
 * Instruction format:
 * header: 2 bits
 * data: 30 bits
 *
 * header format:
 * 0 => positive integer
 * 1 => primitive instruction
 * 2 => negative integer
 * 3 => undefined
 */
class StackVM(private val printDebug: Boolean = false) : VM() {
    override fun getType(instruction: Long): Long {
        val result = (0xc0000000 and instruction) shr 30
        debug("[debug] getType in=$instruction out=$result")
        return result
    }

    override fun getData(instruction: Long): Long {
        val result = (0x3fffffff).toLong() and instruction
        debug("[debug] getData in=$instruction out=$result")
        return result
    }

    override fun fetch() {
        programCounter += 1
    }

    override fun decode() {
        debug("[debug] decode programCounter=$programCounter")
        typeRegistrar = getType(memory[programCounter])
        dataRegistrar = getData(memory[programCounter])
    }

    override fun execute() {
        if (typeRegistrar.equals(0) || typeRegistrar.equals(2)) {
            stackPointer += 1
            memory[stackPointer] = dataRegistrar
        } else {
            doPrimitive()
        }
    }

    override fun doPrimitive() {
        when (dataRegistrar) {
            0L -> {
                println("halt")
                running = false
            }
            1L -> {
                println("add ${memory[stackPointer - 1]} ${memory[stackPointer]}")
                memory[stackPointer - 1] = memory[stackPointer - 1] + memory[stackPointer]
                stackPointer -= 1
            }
            2L -> {
                println("sub ${memory[stackPointer - 1]} ${memory[stackPointer]}")
                memory[stackPointer - 1] = memory[stackPointer - 1] - memory[stackPointer]
                stackPointer -= 1
            }
            3L -> {
                println("mul ${memory[stackPointer - 1]} ${memory[stackPointer]}")
                memory[stackPointer - 1] = memory[stackPointer - 1] * memory[stackPointer]
                stackPointer -= 1
            }
            4L -> {
                println("div ${memory[stackPointer - 1]} ${memory[stackPointer]}")
                memory[stackPointer - 1] = memory[stackPointer - 1] / memory[stackPointer]
                stackPointer -= 1
            }
        }
    }

    override fun run() {
        programCounter -= 1
        while (running) {
            fetch()
            decode()
            execute()
            debugLine()
            println("tos: ${memory[stackPointer]}")
            debugLine()
        }
    }

    override fun loadProgram(program: LongArray) {
        for (i in program.indices) {
            debug("[debug] loadProgram memory[${programCounter + i}] = ${program[i]}")
            memory[programCounter + i] = program[i]
        }
    }

    private fun debugLine() = debug("")

    private fun debug(message: String) {
        if (printDebug) println(message)
    }

}
