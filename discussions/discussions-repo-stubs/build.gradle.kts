plugins {
    kotlin("jvm")
}
val coroutinesVersion: String by project
dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(project(":discussions:discussions-common"))
    implementation(project(":discussions:discussions-stubs"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(project(":discussions:discussions-repo-tests"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))

}
