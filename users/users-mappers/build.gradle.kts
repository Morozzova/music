plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":users:users-api-jackson"))
    implementation(project(":users:users-common"))
    implementation(project(mapOf("path" to ":users:users-api-jackson")))

    testImplementation(kotlin("test-junit"))
}