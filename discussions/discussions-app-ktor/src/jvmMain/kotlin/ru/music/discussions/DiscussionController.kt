package ru.music.discussions

import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import musicBroker.api.v1.models.*
import ru.music.common.DiscContext
import ru.music.discussions.stubs.DiscStub
import toTransportAllDisc
import toTransportClose
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportUpdate
import toTransportUsersDisc

suspend fun ApplicationCall.createDiscussion() {
    val request = receive<DiscussionCreateRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportCreate() )
}

suspend fun ApplicationCall.readDiscussion() {
    val request = receive<DiscussionReadRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportRead() )
}

suspend fun ApplicationCall.updateDiscussion() {
    val request = receive<DiscussionUpdateRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportUpdate() )
}

suspend fun ApplicationCall.closeDiscussion() {
    val request = receive<DiscussionCloseRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportClose() )
}

suspend fun ApplicationCall.deleteDiscussion() {
    val request = receive<DiscussionDeleteRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportDelete() )
}

suspend fun ApplicationCall.allDiscussions() {
    val request = receive<AllDiscussionsRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.multiDiscussionsResponse = DiscStub.getAll()
    respond(context.toTransportAllDisc() )
}
suspend fun ApplicationCall.usersDiscussions() {
    val request = receive<UsersDiscussionsRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.multiDiscussionsResponse = DiscStub.getUsers()
    respond(context.toTransportUsersDisc() )
}