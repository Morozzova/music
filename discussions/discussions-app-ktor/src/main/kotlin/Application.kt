package ru.music.discussions

import com.auth0.jwt.JWT
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import ru.music.discussions.api.v1.apiV1Mapper
import ru.music.discussions.base.resolveAlgorithm
import ru.music.discussions.logging.jvm.MpLogWrapperLogback
import ru.music.discussions.plugins.initAppSettings
import ru.music.discussions.plugins.initPlugins
import ru.music.discussions.plugins.swagger
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.music.discussions.ru.music.discussions.discussions

fun main(args: Array<String>) = io.ktor.server.cio.EngineMain.main(args)

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

    install(Authentication) {
        jwt("auth-jwt") {
            val authConfig = appSettings.auth
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        this@module.log.error("Groups claim must not be empty in JWT token")
                        null
                    }

                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }

    routing {
        route("discussion") {
            authenticate("auth-jwt") {
                discussions(appSettings)
            }
        }
        swagger(appSettings)
        staticResources("static", "ru.music.static")
    }
}