package ru.music.discussions.ru.music.discussions

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import ru.music.discussions.*

class RouteStrategyCreate : RouteStrategy {
    override val method: String = "create"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.createDiscussion(appSettings, logger)
    }
}

class RouteStrategyRead : RouteStrategy {
    override val method: String = "read"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.readDiscussion(appSettings, logger)
    }
}

class RouteStrategyUpdate : RouteStrategy {
    override val method: String = "update"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.updateDiscussion(appSettings, logger)
    }
}

class RouteStrategyClose : RouteStrategy {
    override val method: String = "close"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.closeDiscussion(appSettings, logger)
    }
}

class RouteStrategyDelete : RouteStrategy {
    override val method: String = "delete"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.deleteDiscussion(appSettings, logger)
    }
}

class RouteStrategyAll : RouteStrategy {
    override val method: String = "all"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.allDiscussions(appSettings, logger)
    }
}

class RouteStrategyUsers : RouteStrategy {
    override val method: String = "users"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>, appSettings: DiscAppSettings, logger: IMpLogWrapper) {
        return context.call.usersDiscussions(appSettings, logger)
    }
}