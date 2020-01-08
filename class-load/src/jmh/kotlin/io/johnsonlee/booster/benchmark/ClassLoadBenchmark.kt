package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.kotlinx.file
import org.openjdk.jmh.annotations.*
import java.io.File
import java.net.URLClassLoader
import java.util.*
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
        this.file = File.createTempFile("annotations", "-18.0.0.jar")
        javaClass.classLoader.getResourceAsStream("annotations-18.0.0.jar").use { input ->
            file.outputStream().use { output ->
                input!!.copyTo(output)
            }
        }
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

    @TearDown
    fun teardown() {
        this.file.delete()
    }

}