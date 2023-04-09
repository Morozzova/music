package ru.music.discussions

import fromTransport
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import musicBroker.api.v1.models.*
import ru.music.common.DiscContext
import ru.music.discussions.stubs.DiscStub
import toLog
import toTransportAllDisc
import toTransportClose
import toTransportCreate
import toTransportDelete
import toTransportRead
import toTransportUpdate
import toTransportUsersDisc

private val clazzCreate = ApplicationCall::createDiscussion::class.qualifiedName ?: "create"
suspend fun ApplicationCall.createDiscussion(appSettings: DiscAppSettings) {
    val logId = "create"
    val logger = appSettings.corSettings.loggerProvider.logger(clazzCreate)

    logger.doWithLogging(logId) {
        val request = receive<DiscussionCreateRequest>()
        val context = DiscContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is got",
            data = context.toLog("${logId}-request"),
        )
        context.discussionResponse = DiscStub.get()
        respond(context.toTransportCreate())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.readDiscussion(appSettings: DiscAppSettings) {
    val request = receive<DiscussionReadRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportRead() )
}

suspend fun ApplicationCall.updateDiscussion(appSettings: DiscAppSettings) {
    val request = receive<DiscussionUpdateRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportUpdate() )
}

suspend fun ApplicationCall.closeDiscussion(appSettings: DiscAppSettings) {
    val request = receive<DiscussionCloseRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportClose() )
}

suspend fun ApplicationCall.deleteDiscussion(appSettings: DiscAppSettings) {
    val request = receive<DiscussionDeleteRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.discussionResponse = DiscStub.get()
    respond(context.toTransportDelete() )
}

suspend fun ApplicationCall.allDiscussions(appSettings: DiscAppSettings) {
    val request = receive<AllDiscussionsRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.multiDiscussionsResponse = DiscStub.getAll()
    respond(context.toTransportAllDisc() )
}
suspend fun ApplicationCall.usersDiscussions(appSettings: DiscAppSettings) {
    val request = receive<UsersDiscussionsRequest>()
    val context = DiscContext()
    context.fromTransport(request)
    context.multiDiscussionsResponse = DiscStub.getUsers()
    respond(context.toTransportUsersDisc() )
}