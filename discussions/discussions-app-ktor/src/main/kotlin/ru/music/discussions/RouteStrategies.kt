package ru.music.discussions.ru.music.discussions

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

class RouteStrategyCreate : RouteStrategy {
    override val method: String = "create"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.createDiscussion()
    }
}

class RouteStrategyRead : RouteStrategy {
    override val method: String = "read"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.readDiscussion()
    }
}

class RouteStrategyUpdate : RouteStrategy {
    override val method: String = "update"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.updateDiscussion()
    }
}

class RouteStrategyClose : RouteStrategy {
    override val method: String = "close"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.closeDiscussion()
    }
}

class RouteStrategyDelete : RouteStrategy {
    override val method: String = "delete"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.deleteDiscussion()
    }
}

class RouteStrategyAll : RouteStrategy {
    override val method: String = "all"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.allDiscussions()
    }
}

class RouteStrategyUsers : RouteStrategy {
    override val method: String = "users"
    override suspend fun handler(context: PipelineContext<Unit, ApplicationCall>) {
        return context.call.usersDiscussions()
    }
}