val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktorServer(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"

fun ktorClient(module: String, version: String? = this@Build_gradle.ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

plugins {
    id("application")
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
    kotlin("plugin.serialization")
    kotlin("jvm")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    mavenCentral()
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
//    mainClass.set("ru.music.discussions.ApplicationKt")
}

val webjars: Configuration by configurations.creating
dependencies {
    val swaggerUiVersion: String by project
    webjars("org.webjars:swagger-ui:$swaggerUiVersion")
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(ktorServer("core")) // "io.ktor:ktor-server-core:$ktorVersion"
    implementation(ktorServer("cio")) // "io.ktor:ktor-server-cio:$ktorVersion"
    implementation(ktorServer("auth")) // "io.ktor:ktor-server-auth:$ktorVersion"
    implementation(ktorServer("auto-head-response")) // "io.ktor:ktor-server-auto-head-response:$ktorVersion"
    implementation(ktorServer("caching-headers")) // "io.ktor:ktor-server-caching-headers:$ktorVersion"
    implementation(ktorServer("cors")) // "io.ktor:ktor-server-cors:$ktorVersion"
    implementation(ktorServer("websockets")) // "io.ktor:ktor-server-websockets:$ktorVersion"
    implementation(ktorServer("config-yaml")) // "io.ktor:ktor-server-config-yaml:$ktorVersion"
    implementation(ktorServer("content-negotiation")) // "io.ktor:ktor-server-content-negotiation:$ktorVersion"

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation(kotlin("stdlib-jdk8"))

    implementation(ktorServer("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

    // jackson
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

//                implementation(ktorServer("locations"))
    implementation(ktorServer("caching-headers"))
    implementation(ktorServer("call-logging"))
    implementation(ktorServer("auto-head-response"))
    implementation(ktorServer("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"
    implementation(ktorServer("auto-head-response"))

    implementation(ktorServer("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

    implementation("com.sndyuk:logback-more-appenders:1.8.8")
    implementation("org.fluentd:fluent-logger:0.3.4")

    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("test-junit"))

    implementation(ktorServer("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
    implementation(ktorClient("content-negotiation"))

    implementation(project(":discussions:discussions-lib-logging-kermit"))
    implementation(project(":discussions:discussions-lib-logging-logback"))
    implementation(project(":discussions:discussions-lib-logging-common"))
    implementation(project(":discussions:discussions-biz"))

    // transport models
    implementation(project(":discussions:discussions-common"))
    implementation(project(":discussions:discussions-api-jackson"))
    implementation(project(":discussions:discussions-mappers"))
    implementation(project(":discussions:discussions-api-log"))
    implementation(project(":discussions:discussions-mappers-log"))

    // Stubs
    implementation(project(":discussions:discussions-stubs"))


    implementation(project(":discussions:discussions-repo-in-memory"))
    implementation(project(":discussions:discussions-repo-stubs"))
    implementation(project(":discussions:discussions-repo-tests"))
    implementation(project(":discussions:discussions-repo-postgresql"))
}

tasks {
//    val dockerJvmDockerfile by creating(Dockerfile::class) {
//        group = "docker"
//        from("openjdk:17")
//        copyFile("app.jar", "app.jar")
//        entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "/app.jar")
//    }
//    create("dockerBuildJvmImage", DockerBuildImage::class) {
//        group = "docker"
//        doFirst {
//            copy {
//                into("${project.buildDir}/docker/app.jar")
//            }
//        }
//        images.add("${project.name}:${project.version}")
//    }


    @Suppress("UnstableApiUsage")
    withType<ProcessResources>().configureEach {
        println("RESOURCES: ${this.name} ${this::class}")
        from("$rootDir/specs") {
            into("specs")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }
        }
        webjars.forEach { jar ->
//        emptyList<File>().forEach { jar ->
            val conf = webjars.resolvedConfiguration
            println("JarAbsPa: ${jar.absolutePath}")
            val artifact = conf.resolvedArtifacts.find { it.file.toString() == jar.absolutePath } ?: return@forEach
            val upStreamVersion = artifact.moduleVersion.id.version.replace("(-[\\d.-]+)", "")
            copy {
                from(zipTree(jar))
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                into(file("${buildDir}/webjars-content/${artifact.name}"))
            }
            with(this@configureEach) {
                this.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                from(
                    "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${upStreamVersion}"
                ) { into(artifact.name) }
                from(
                    "${buildDir}/webjars-content/${artifact.name}/META-INF/resources/webjars/${artifact.name}/${artifact.moduleVersion.id.version}"
                ) { into(artifact.name) }
            }
        }
    }
}