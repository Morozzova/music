rootProject.name = "musicBroker"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val ktorPluginVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorPluginVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("answers")
//include("answers:answers-common")
//include("answers:answers-mappers")
//include("answers:answers-api-jackson")
include("discussions")
include("discussions:discussions-common")
include("discussions:discussions-mappers")
include("discussions:discussions-api-jackson")
//include("users")
//include("users:users-common")
//include("users:users-mappers")
//include("users:users-api-jackson")
include("discussions-app-ktor")
include("discussions-stubs")
