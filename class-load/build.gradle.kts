dependencies {
    jmh(project(":logging"))
    jmh("org.apache.commons:commons-compress:1.19")
    jmh("com.didiglobal.booster:booster-transform-util:${rootProject.extra["booster_version"]}")
    jmh("com.didiglobal.booster:booster-build:${rootProject.extra["booster_version"]}")
    jmh("com.didiglobal.booster:booster-kotlinx:${rootProject.extra["booster_version"]}")
}
