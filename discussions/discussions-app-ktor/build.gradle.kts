import org.jetbrains.kotlin.util.suffixIfNot
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktor(module: String, prefix: String = "server-", version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
//    mainClass.set("io.ktor.server.netty.EngineMain")
    mainClass.set("ru.music.ApplicationKt")
}

kotlin {
    jvm {}

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion" implementation(project(":discussions:discussions-common"))
                implementation(project(":discussions:discussions-stubs"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

                // jackson
                implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
                implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation
                implementation(ktor("kotlinx-json", "serialization")) // io.ktor:ktor-serialization-kotlinx-json

                implementation(ktor("locations"))
                implementation(ktor("caching-headers"))
                implementation(ktor("call-logging"))
                implementation(ktor("auto-head-response"))
                implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("auto-head-response"))
                implementation(ktor("websockets"))

                implementation(ktor("websockets")) // "io.ktor:ktor-websockets:$ktorVersion"
                implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"
                implementation(ktor("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                // transport models
                implementation(project(":discussions:discussions-common"))
                implementation(project(":discussions:discussions-api-jackson"))
                implementation(project(":discussions:discussions-mappers"))

                // Stubs
                implementation(project(":discussions:discussions-stubs"))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktor("content-negotiation", prefix = "client-"))
                implementation(ktor("websockets", prefix = "client-"))
            }
        }
    }
}

tasks {
    val dockerJvmDockerfile by creating(Dockerfile::class) {
        group = "docker"
        from("openjdk:17")
        copyFile("app.jar", "app.jar")
        entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "/app.jar")
    }
    create("dockerBuildJvmImage", DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerJvmDockerfile, named("jvmJar"))
        doFirst {
            copy {
                from(named("jvmJar"))
                into("${project.buildDir}/docker/app.jar")
            }
        }
        images.add("${project.name}:${project.version}")
    }
}