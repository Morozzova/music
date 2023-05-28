plugins {
    kotlin("jvm")
}

dependencies {
    val exposedVersion: String by project
    val postgresDriverVersion: String by project
    val kmpUUIDVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))

    implementation(project(":discussions:discussions-common"))

    implementation("org.postgresql:postgresql:$postgresDriverVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.benasher44:uuid:$kmpUUIDVersion")


    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation(project(":discussions:discussions-repo-tests"))
}
