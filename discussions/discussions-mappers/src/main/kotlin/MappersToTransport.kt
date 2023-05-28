import exceptions.UnknownDiscCommand
import musicBroker.api.v1.models.*
import permissions.MusicPermissionClient
import ru.music.common.DiscContext
import ru.music.common.models.*

fun DiscContext.toTransport() : IResponse = when (command) {
    DiscCommand.CREATE -> toTransportCreate()
    DiscCommand.READ -> toTransportRead()
    DiscCommand.UPDATE -> toTransportUpdate()
    DiscCommand.CLOSE -> toTransportClose()
    DiscCommand.DELETE -> toTransportDelete()
    DiscCommand.ALL_DISCUSSIONS -> toTransportAllDisc()
    DiscCommand.USERS_DISCUSSIONS -> toTransportUsersDisc()
    else -> throw UnknownDiscCommand(command)
}

fun DiscContext.toTransportCreate() = DiscussionCreateResponse(
    responseType = "create",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportRead() = DiscussionReadResponse(
    responseType = "read",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)
fun DiscContext.toTransportUpdate() = DiscussionUpdateResponse(
    responseType = "update",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)
fun DiscContext.toTransportClose() = DiscussionCloseResponse(
    responseType = "close",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportDelete() = DiscussionDeleteResponse(
    responseType = "delete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportAllDisc() = AllDiscussionsResponse(
    responseType = "allDiscussions",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = multiDiscussionsResponse.toTransportDisc()
)

fun DiscContext.toTransportUsersDisc() = UsersDiscussionsResponse(
    responseType = "usersDiscussions",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = multiDiscussionsResponse.toTransportDisc()
)
fun List<DiscDiscussion>.toTransportDisc(): List<DiscussionResponseObject>? = this
    .map { it.toTransportDisc() }
    .toList()
    .takeIf { it.isNotEmpty() }
fun Set<MusicPermissionClient>.toTransportDisc(): Set<DiscussionPermissions>? = this
    .map { it.toTransportDisc() }
    .takeIf { it.isNotEmpty() }?.toSet()

private fun DiscDiscussion.toTransportDisc(): DiscussionResponseObject = DiscussionResponseObject(
    id = id.takeIf { it != DiscId.NONE }?.asString(),
    soundUrl = soundUrl.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    status = status.toTransport(),
    answers = answers.toTransportAnswers(),
    ownerId = ownerId.takeIf { it != DiscUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportDisc(),
    lock = lock.takeIf { it != DiscLock.NONE }?.asString(),
)

private fun MutableList<DiscAnswer>.toTransportAnswers(): List<String>? = this
    .map { it.asString() }
    .takeIf { it.isNotEmpty() }

private fun DiscStatus.toTransport(): DiscussionStatus = when (this) {
    DiscStatus.NONE -> DiscussionStatus.NONE
    DiscStatus.OPEN -> DiscussionStatus.OPEN
    DiscStatus.CLOSED -> DiscussionStatus.CLOSED
}
private fun List<MusicPermissionClient>.toTransportDisc(): Set<DiscussionPermissions>? = this
    .map { it.toTransportDisc() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MusicPermissionClient.toTransportDisc() = when (this) {
    MusicPermissionClient.READ -> DiscussionPermissions.READ
    MusicPermissionClient.UPDATE -> DiscussionPermissions.UPDATE
    MusicPermissionClient.CLOSE -> DiscussionPermissions.CLOSE
    MusicPermissionClient.DELETE -> DiscussionPermissions.DELETE
    MusicPermissionClient.ALL_DISCUSSIONS -> DiscussionPermissions.READ
    MusicPermissionClient.USERS_DISCUSSIONS -> DiscussionPermissions.READ
}

private fun List<DiscError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportDisk() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun DiscError.toTransportDisk() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)