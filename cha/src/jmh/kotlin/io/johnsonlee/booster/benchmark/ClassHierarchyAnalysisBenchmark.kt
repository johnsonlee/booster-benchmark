package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.build.AndroidSdk
import com.didiglobal.booster.cha.ClassHierarchy
import com.didiglobal.booster.cha.ClassSet
import com.didiglobal.booster.cha.asm.AsmClassFileParser
import com.didiglobal.booster.cha.asm.AsmClassSet
import com.didiglobal.booster.cha.asm.from
import org.apache.commons.io.output.NullPrintStream
import org.objectweb.asm.tree.ClassNode
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
import java.net.URLClassLoader
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = ["-Xms2G", "-Xmx2G"])
@State(Scope.Benchmark)
open class ClassHierarchyAnalysisBenchmark {

    private lateinit var file: File
    private lateinit var classLoader: URLClassLoader
    private lateinit var classHierarchy: ClassHierarchy<ClassNode, AsmClassFileParser>

    @Setup
    fun setup() {
        System.setOut(NullPrintStream())
        this.file = AndroidSdk.getAndroidJar(28)
        this.classLoader = URLClassLoader(arrayOf(file.toURI().toURL()))
        this.classHierarchy = ClassHierarchy(ClassSet.from(file))
    }

    @TearDown
    fun teardown() {
        this.classLoader.close()
    }

    @Benchmark
    fun analyseClassHierarchyByClassLoaderWithoutCache() {
        URLClassLoader(arrayOf(file.toURI().toURL())).use {
            val classContext = Class.forName("android.content.Context", false, it)
            val classListActivity = Class.forName("android.app.ListActivity", false, it)
            println(classContext.isAssignableFrom(classListActivity))
        }
    }

    @Benchmark
    fun analyseClassHierarchyByGraphWithoutCache() {
        val hierarchy = ClassHierarchy(ClassSet.from(file))
        println(hierarchy.isInheritFrom("android/app/ListActivity", "android/content/Context"))
    }

    @Benchmark
    fun analyseClassHierarchyByClassLoaderWithCache() {
        val classContext = Class.forName("android.content.Context", false, classLoader)
        val classListActivity = Class.forName("android.app.ListActivity", false, classLoader)
        println(classContext.isAssignableFrom(classListActivity))
    }

    @Benchmark
    fun analyseClassHierarchyByGraphWithCache() {
        println(classHierarchy.isInheritFrom("android/app/ListActivity", "android/content/Context"))
    }

}