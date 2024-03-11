plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.philippschuetz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation("com.sksamuel.hoplite:hoplite-core:2.8.0.RC3")
    implementation("com.sksamuel.hoplite:hoplite-json:2.8.0.RC3")
    implementation("info.picocli:picocli:4.7.5")
    implementation("com.jcraft:jsch:0.1.55")
    implementation("org.ktorm:ktorm-core:3.6.0")
    implementation("org.ktorm:ktorm-support-sqlite:3.6.0")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}