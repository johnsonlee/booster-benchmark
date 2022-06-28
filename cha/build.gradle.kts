dependencies {
    jmh(project(":logging"))
    jmh("com.didiglobal.booster:booster-cha-asm:${rootProject.extra["booster_version"]}")
    jmh("com.didiglobal.booster:booster-transform-util:${rootProject.extra["booster_version"]}")
    jmh("com.didiglobal.booster:booster-build:${rootProject.extra["booster_version"]}")
    jmh("com.didiglobal.booster:booster-kotlinx:${rootProject.extra["booster_version"]}")
}
