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

@BenchmarkMode
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@State(Scope.Benchmark)
open class LdcVsGetFieldBenchmark {


    @Benchmark
    fun ldc() {
        f(10)
    }

    @Benchmark
    fun getfield() {
        f(value)
    }

    companion object {

    }

}

const val value = 10

internal fun f(value: Int) {
}