plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":answers:answers-api-jackson"))
    implementation(project(":answers:answers-common"))

    testImplementation(kotlin("test-junit"))
}