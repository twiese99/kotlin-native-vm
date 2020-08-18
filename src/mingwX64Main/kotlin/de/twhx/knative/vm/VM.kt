package de.twhx.knative.vm

abstract class VM {

    protected var programCounter: Int = 100
    protected var stackPointer: Int = 0
    protected var typeRegistrar: Long = 0
    protected var dataRegistrar: Long = 0
    protected var running: Boolean = true
    protected var memory: LongArray = LongArray(size = 1000000)

    protected abstract fun getType(instruction: Long): Long
    protected abstract fun getData(instruction: Long): Long
    protected abstract fun fetch()
    protected abstract fun decode()
    protected abstract fun execute()
    protected abstract fun doPrimitive()

    abstract fun run()
    abstract fun loadProgram(program: LongArray)

}
