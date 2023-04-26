package ru.music.discussions

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import ru.music.discussions.api.v1.apiV1Mapper
import ru.music.discussions.logging.jvm.MpLogWrapperLogback
import ru.music.discussions.plugins.initAppSettings
import ru.music.discussions.plugins.initPlugins
import ru.music.discussions.plugins.swagger
import ru.music.discussions.ru.music.discussions.discussions

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val clazz = Application::module::class.qualifiedName ?: "Application"
@Suppress("unused") // Referenced in application.conf_
fun Application.module(appSettings: DiscAppSettings = initAppSettings()) {
    initPlugins(appSettings)

    install(CallLogging) {
        level = Level.INFO
        val lgr = appSettings
            .corSettings
            .loggerProvider
            .logger(clazz) as? MpLogWrapperLogback
        lgr?.logger?.also { logger = it }
    }
    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }
    install(DefaultHeaders)

    routing {
        route("discussion") {
            discussions(appSettings)
        }
        swagger(appSettings)
        static("static") {
            resources("static")
        }
    }
}
