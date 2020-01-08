package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.transform.util.transform
import org.openjdk.jmh.annotations.*
import java.io.File
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@State(Scope.Benchmark)
open class JarFileTransformBenchmark {

    private lateinit var file: File

    @Setup
    fun setup() {
        this.file = File.createTempFile("sdklib-", "-25.3.0.jar")
        javaClass.classLoader.getResourceAsStream("sdklib-25.3.0.jar").use { input ->
            file.outputStream().use { output ->
                input!!.copyTo(output)
            }
        }
    }

    @Benchmark
    fun transformJarFileParallelly() {
        val target = File.createTempFile("parallel-sdklib-", "-25.3.0.jar")
        JarFile(this.file).transform(target)
        target.delete()
    }

    @Benchmark
    fun transformJarFileSequentially() {
        val target = Files.createTempFile("sequential-sdklib-", "-25.3.0.jar").toFile()
        target.outputStream().buffered().use { out ->
            JarOutputStream(out).use { outJar ->
                JarFile(file).use { inJar ->
                    inJar.entries().asSequence().forEach { entry ->
                        inJar.getInputStream(entry).buffered().use { input ->
                            outJar.putNextEntry(JarEntry(entry.name).apply {
                                method = entry.method
                            })
                            input.copyTo(outJar)
                            outJar.closeEntry()
                        }
                    }
                }
            }
        }
        target.delete()
    }


    @TearDown
    fun teardown() {
        this.file.delete()
    }

}