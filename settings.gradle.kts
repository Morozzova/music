rootProject.name = "musicBroker"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

include("discussions")
include("users")
include("answers")