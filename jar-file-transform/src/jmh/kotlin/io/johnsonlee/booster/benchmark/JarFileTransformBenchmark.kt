package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.build.AndroidSdk
import com.didiglobal.booster.kotlinx.NCPU
import com.didiglobal.booster.transform.util.transform
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.util.NullOutputStream
import java.nio.file.Files
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@State(Scope.Benchmark)
open class JarFileTransformBenchmark {

    @Benchmark
    fun transformJarFileSequentially() {
        val input = AndroidSdk.getAndroidJar()
        val output = Files.createTempFile("android-", "-28.jar").toFile()
        output.outputStream().buffered().use { out ->
            JarOutputStream(out).use { outJar ->
                JarFile(input).use { inJar ->
                    inJar.entries().asSequence().forEach { entry ->
                        inJar.getInputStream(entry).buffered().use { input ->
                            outJar.putNextEntry(JarEntry(entry))
                            input.copyTo(outJar)
                            outJar.closeEntry()
                        }
                    }
                }
            }
        }
        output.delete()
    }

    @Benchmark
    fun transformJarFileWithFixedThreadPool() {
        val input = AndroidSdk.getAndroidJar(28)
        val output = Files.createTempFile("android-", "-28.jar").toFile()
        JarFile(input).transform(NullOutputStream(), ::ZipArchiveEntry)
        output.delete()
    }

}