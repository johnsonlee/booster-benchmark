extra["booster_version"] = "4.11.0-beta4"

plugins {
    kotlin("jvm") version embeddedKotlinVersion
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
        implementation(kotlin("bom"))
        implementation(kotlin("stdlib"))

        testImplementation(kotlin("test"))
        testImplementation(kotlin("test-junit"))

        jmh(kotlin("bom"))
        jmh(kotlin("stdlib"))
    }
}
