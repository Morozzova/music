plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":discussions:discussions-api-jackson"))
    implementation(project(":discussions:discussions-common"))

    testImplementation(kotlin("test-junit"))
}