package io.johnsonlee.booster.benchmark

import org.apache.commons.compress.archivers.zip.*
import org.apache.commons.compress.parallel.InputStreamSupplier
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
    fun parallelTransformJarFile() {
        val target = File.createTempFile("parallel-sdklib-", "-25.3.0.jar")
        JarFile(file).use { inJar ->
            val creator = ParallelScatterZipCreator()

            inJar.entries().asSequence().forEach { entry ->
                val zae = ZipArchiveEntry(entry.name).apply {
                    method = entry.method
                }
                val stream = InputStreamSupplier {
                    inJar.getInputStream(entry)
                }

                if (zae.isDirectory && !zae.isUnixSymlink) {
                    creator.addArchiveEntry(zae, stream)
                }
            }

            ZipArchiveOutputStream(target).use {
                creator.writeTo(it)
            }
        }
        target.delete()
    }

    @Benchmark
    fun sequentialTransformJarFile() {
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
    }

}