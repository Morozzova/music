package ru.music.discussions

import io.ktor.server.application.*
import musicBroker.api.v1.models.*
import ru.music.common.models.DiscCommand
import ru.music.discussions.ru.music.discussions.process

suspend fun ApplicationCall.createDiscussion(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<DiscussionCreateRequest, DiscussionCreateResponse>(appSettings, logger, "discussion-create", DiscCommand.CREATE)

suspend fun ApplicationCall.readDiscussion(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<DiscussionReadRequest, DiscussionReadResponse>(appSettings, logger, "discussion-read", DiscCommand.READ)

suspend fun ApplicationCall.updateDiscussion(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<DiscussionUpdateRequest, DiscussionUpdateResponse>(appSettings, logger, "discussion-update", DiscCommand.UPDATE)

suspend fun ApplicationCall.closeDiscussion(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<DiscussionCloseRequest, DiscussionCloseResponse>(appSettings, logger, "discussion-close", DiscCommand.CLOSE)

suspend fun ApplicationCall.deleteDiscussion(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<DiscussionDeleteRequest, DiscussionDeleteResponse>(appSettings, logger, "discussion-delete", DiscCommand.DELETE)

suspend fun ApplicationCall.allDiscussions(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<AllDiscussionsRequest, AllDiscussionsResponse>(appSettings, logger, "discussion-all_discussions", DiscCommand.ALL_DISCUSSIONS)

suspend fun ApplicationCall.usersDiscussions(appSettings: DiscAppSettings, logger: IMpLogWrapper) =
    process<UsersDiscussionsRequest, UsersDiscussionsResponse>(appSettings, logger, "discussion-users_discussions", DiscCommand.USERS_DISCUSSIONS)
