package ru.music.discussions.ru.music.discussions

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import ru.music.discussions.*

class RouteStrategyCreate : RouteStrategy {
    override val method: String = "create"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.createDiscussion(appSettings)
    }
}

class RouteStrategyRead : RouteStrategy {
    override val method: String = "read"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.readDiscussion(appSettings)
    }
}

class RouteStrategyUpdate : RouteStrategy {
    override val method: String = "update"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.updateDiscussion(appSettings)
    }
}

class RouteStrategyClose : RouteStrategy {
    override val method: String = "close"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.closeDiscussion(appSettings)
    }
}

class RouteStrategyDelete : RouteStrategy {
    override val method: String = "delete"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.deleteDiscussion(appSettings)
    }
}

class RouteStrategyAll : RouteStrategy {
    override val method: String = "all"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.allDiscussions(appSettings)
    }
}

class RouteStrategyUsers : RouteStrategy {
    override val method: String = "users"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings) {
        return context.call.usersDiscussions(appSettings)
    }
}