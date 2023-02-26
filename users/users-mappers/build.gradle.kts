plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":users:users-api-jackson"))
    implementation(project(":users:users-common"))

    testImplementation(kotlin("test-junit"))
}