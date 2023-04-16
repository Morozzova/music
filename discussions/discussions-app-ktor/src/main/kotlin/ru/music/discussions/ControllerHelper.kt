package ru.music.discussions.ru.music.discussions

import fromTransport
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import musicBroker.api.v1.models.IRequest
import musicBroker.api.v1.models.IResponse
import ru.music.common.DiscContext
import ru.music.common.helpers.asDiscError
import ru.music.common.models.DiscCommand
import ru.music.common.models.DiscState
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.IMpLogWrapper
import toLog
import toTransport

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.process(
    appSettings: DiscAppSettings,
    logger: IMpLogWrapper,
    logId: String,
    command: DiscCommand? = null,
) {
    val ctx = DiscContext(
        timeStart = Clock.System.now(),
    )
    val processor = appSettings.processor
    try {
        logger.doWithLogging(id = logId) {
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            processor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(ctx.toTransport())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
            )
            ctx.state = DiscState.FAILING
            ctx.errors.add(e.asDiscError())
            processor.exec(ctx)
            respond(ctx.toTransport())
        }
    }
}
