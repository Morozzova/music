package ru.music.discussions.ru.music.discussions

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

interface RouteStrategy {
    val method: String
    suspend fun handler(context: PipelineContext<Unit, ApplicationCall>)
}