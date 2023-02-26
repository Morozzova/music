import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "musicBroker"
version = "1.0-SNAPSHOT"

val kotestVersion: String by project
val coroutinesVersion: String by project
val jUnitJupiterVersion: String by project

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    implementation(kotlin("test-junit5"))
    implementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    implementation("org.junit.jupiter:junit-jupiter-params:$jUnitJupiterVersion")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.test {
    useJUnitPlatform()
}