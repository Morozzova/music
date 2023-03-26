plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(project(":discussions:discussions-common"))

    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("test-junit"))

}
