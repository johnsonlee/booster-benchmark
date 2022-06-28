package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.build.AndroidSdk
import com.didiglobal.booster.kotlinx.file
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.io.File
import java.net.URLClassLoader
import java.util.concurrent.TimeUnit
import java.util.jar.JarFile

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@State(Scope.Benchmark)
open class ClassLoadBenchmark {

    private lateinit var file: File

    @Setup
    fun setup() {
        this.file = AndroidSdk.getAndroidJar(28)
    }

    @Benchmark
    fun loadClassViaClassLoaderLoad() {
        val classLoader = URLClassLoader(arrayOf(this.file.toURI().toURL()))
        JarFile(this.file).entries().asSequence().filter {
            it.name.endsWith(".class")
        }.forEach { entry ->
            classLoader.loadClass(entry.name.substringBeforeLast(".").replace('/', '.'))
        }
    }

    @Benchmark
    fun loadClassViaClassForName() {
        val classLoader = URLClassLoader(arrayOf(this.file.toURI().toURL()))
        JarFile(this.file).entries().asSequence().filter {
            it.name.endsWith(".class")
        }.forEach { entry ->
            Class.forName(entry.name.substringBeforeLast(".").replace('/', '.'), false, classLoader)
        }
    }

}