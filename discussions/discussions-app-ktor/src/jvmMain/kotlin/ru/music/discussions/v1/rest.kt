package ru.music.discussions.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.discussions() {
    route("ad") {
        post("create") {
            call.createDiscussion()
        }
        post("read") {
            call.readDiscussion()
        }
        post("update") {
            call.updateDiscussion()
        }
        post("close") {
            call.closeDiscussion()
        }
        post("delete") {
            call.deleteDiscussion()
        }
        post("all") {
            call.allDiscussions()
        }
        post("users") {
            call.usersDiscussions()
        }
    }
}
