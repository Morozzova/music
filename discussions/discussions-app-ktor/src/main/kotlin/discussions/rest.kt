package discussions

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.discussions() {
    route("disc") {
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
