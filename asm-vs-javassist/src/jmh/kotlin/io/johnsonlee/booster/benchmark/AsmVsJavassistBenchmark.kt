package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.build.AndroidSdk
import com.didiglobal.booster.transform.AbstractTransformContext
import com.didiglobal.booster.transform.asm.AsmTransformer
import com.didiglobal.booster.transform.javassist.JavassistTransformer
import com.didiglobal.booster.transform.util.transform
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import java.io.File
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
        this.file = File.createTempFile("guava-", "-28.2-jre.jar")
        javaClass.classLoader.getResourceAsStream("guava-28.2-jre.jar").use { input ->
            file.outputStream().use { output ->
                input!!.copyTo(output)
            }
        }
    }

    @Benchmark
    fun transformJarUsingAsm() {
        val target = File.createTempFile("guava-", "-28.2-jre.jar")
        val context = object : AbstractTransformContext("transform-jar-using-asm", listOf(AndroidSdk.getAndroidJar(28)), emptyList(), emptyList()) {}

        AsmTransformer(AsmThreadTransformer()).let { asm ->
            asm.onPreTransform(context)

            JarFile(this.file).transform(target) { bytecode ->
                asm.transform(context, bytecode)
            }

            // asm.onPostTransform(context)
        }
    }

    @Benchmark
    fun transformJarUsingJavassist() {
        val target = File.createTempFile("guava-", "-28.2-jre.jar")
        val context = object : AbstractTransformContext("transform-jar-using-javassist", listOf(AndroidSdk.getAndroidJar(28)), emptyList(), emptyList()) {}

        JavassistTransformer(JavassistThreadTransformer()).let { javassist ->
            javassist.onPreTransform(context)

            JarFile(this.file).transform(target) { bytecode ->
                javassist.transform(context, bytecode)
            }

            // javassist.onPostTransform(context)
        }
    }

    @TearDown
    fun teardown() {
        this.file.delete()
    }

}