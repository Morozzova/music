package ru.music.discussions.ru.music.discussions

import io.ktor.server.routing.*

val methodsStrategy: List<RouteStrategy> = listOf(
    RouteStrategyCreate(),
    RouteStrategyRead(),
    RouteStrategyUpdate(),
    RouteStrategyClose(),
    RouteStrategyDelete(),
    RouteStrategyAll(),
    RouteStrategyUsers()
)

fun Route.discussions() {
    route("disc") {
        methodsStrategy.forEach { route ->
            post(route.method) {
                route.handler(this)
            }
        }
    }
}