import de.twhx.knative.vm.StackVM
import kotlinx.cinterop.*
import platform.posix.*

actual fun main(args: Array<String>) {

    if (args.size != 1) {
        println("Usage: knative.exe <program.txt>")
        return
    }

    val lines = readLinesFromFile(path = args[0])
    val instructions = lines.flatMap { it.split(regex = "\\s+".toRegex()) }
    val program = instructions.map { convertToLong(it) }.toLongArray()

    val vm = StackVM(true)
    vm.loadProgram(program)
    vm.run()
}

fun convertToLong(s: String): Long = if (s.startsWith("0x")) {
    s.removePrefix("0x").toLong(16)
} else {
    s.toLong()
}

fun readLinesFromFile(path: String): List<String> {
    val file = fopen64(path, "r")
    if (file == null) {
        perror("Cannot open input file $path")
        return emptyList()
    }
    return readLinesFromFile(file = file)
}

fun readLinesFromFile(file: CPointer<FILE>): List<String> {
    val lines = ArrayList<String>()
    try {
        memScoped {
            val bufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(bufferLength)

            do {
                val nextLine = fgets(buffer, bufferLength, file)?.toKString()
                if (nextLine == null || nextLine.isEmpty()) break
                lines += nextLine
            } while (true)
        }
    } finally {
        fclose(file)
    }
    return lines
}
