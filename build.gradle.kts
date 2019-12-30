plugins {
    kotlin("jvm") version "1.3.41"
    id("me.champeau.gradle.jmh") version "0.5.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    jmh(platform("org.jetbrains.kotlin:kotlin-bom"))
    jmh("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    jmh("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    jmh("org.apache.commons:commons-compress:1.19")
}
