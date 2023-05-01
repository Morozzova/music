rootProject.name = "musicBroker"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val openapiVersion: String by settings
        val ktorPluginVersion: String by settings
        val bmuschkoVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorPluginVersion apply false
        id("org.openapi.generator") version openapiVersion apply false

        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}

//include("answers")
//include("answers:answers-common")
//include("answers:answers-mappers")
//include("answers:answers-api-jackson")

//include("users")
//include("users:users-common")
//include("users:users-mappers")
//include("users:users-api-jackson")

include("discussions")
include("discussions:discussions-common")
include("discussions:discussions-mappers")
include("discussions:discussions-mappers-log")
include("discussions:discussions-api-jackson")
include("discussions:discussions-api-log")

include("discussions:discussions-app-ktor")
//include("discussions:discussions-app-kafka")
include("discussions:discussions-stubs")
include("discussions:discussions-biz")
include("discussions:discussions-lib-logging-common")
include("discussions:discussions-lib-logging-kermit")
include("discussions:discussions-lib-logging-logback")
include("discussions:discussions-lib-cor")

include("discussions:discussions-repo-in-memory")
include("discussions:discussions-repo-stubs")
include("discussions:discussions-repo-tests")
