package ru.music.discussions.ru.music.discussions

import io.ktor.server.routing.*
import ru.music.discussions.DiscAppSettings

val methodsStrategy: List<RouteStrategy> = listOf(
    RouteStrategyCreate(),
    RouteStrategyRead(),
    RouteStrategyUpdate(),
    RouteStrategyClose(),
    RouteStrategyDelete(),
    RouteStrategyAll(),
    RouteStrategyUsers()
)

fun Route.discussions(appSettings: DiscAppSettings) {
    methodsStrategy.forEach { route ->
        post(route.method) {
            route.handler(this, appSettings)
        }
    }
}