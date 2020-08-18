plugins {
    kotlin("multiplatform") version "1.4.0"
}
group = "de.twhx"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("macosX64")
        hostOs == "Linux" -> linuxX64("linuxX64")
        isMingwX64 -> mingwX64("mingwX64")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val mingwX64Main by getting
        val mingwX64Test by getting
        val linuxX64Main by getting
        val linuxX64Test by getting
        val macosX64Main by getting
        val macosX64Test by getting
    }
}