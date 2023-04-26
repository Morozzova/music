package ru.music.discussions.ru.music.discussions

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.IMpLogWrapper

interface RouteStrategy {
    val method: String
    suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper)
}