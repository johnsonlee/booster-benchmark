plugins {
    kotlin("jvm") version "1.3.41"
    id("me.champeau.gradle.jmh") version "0.5.0"
}

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("me.champeau.gradle.jmh")

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        jcenter()
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

        jmh(platform("org.jetbrains.kotlin:kotlin-bom"))
        jmh("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        jmh("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    }
}
