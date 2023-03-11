import musicBroker.api.v1.models.*
import ru.music.common.DiscContext
import ru.music.common.models.*

fun DiscContext.toTransport() : IResponse = when (command) {
    DiscCommand.CREATE -> toTransportCreate()
    DiscCommand.READ -> toTransportRead()
    DiscCommand.UPDATE -> toTransportUpdate()
    DiscCommand.DELETE -> toTransportDelete()
    DiscCommand.ALL_DISCUSSIONS -> toTransportAllDisc()
    DiscCommand.USERS_DISCUSSIONS -> toTransportUsersDisc()
    else -> throw Exception("Wrong command $command at mapping toTransport stage")
}

fun DiscContext.toTransportCreate() = DiscussionCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportRead() = DiscussionReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)
fun DiscContext.toTransportUpdate() = DiscussionUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportDelete() = DiscussionDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussion = discussionResponse.toTransportDisc()
)

fun DiscContext.toTransportAllDisc() = AllDiscussionsResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = multiDiscussionsResponse.toTransportDisc()
)

fun DiscContext.toTransportUsersDisc() = UsersDiscussionsResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DiscState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = multiDiscussionsResponse.toTransportDisc()
)
fun List<DiscDiscussion>.toTransportDisc(): List<DiscussionResponseObject>? = this
    .map { it.toTransportDisc() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun DiscDiscussion.toTransportDisc(): DiscussionResponseObject = DiscussionResponseObject(
    id = id.takeIf { it != DiscId.NONE }?.asString(),
    soundUrl = soundUrl.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != DiscUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportDisc(),
)

private fun List<DiscPermissionsClient>.toTransportDisc(): Set<DiscussionPermissions>? = this
    .map { it.toTransportDisc() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun DiscPermissionsClient.toTransportDisc() = when (this) {
    DiscPermissionsClient.READ -> DiscussionPermissions.READ
    DiscPermissionsClient.UPDATE -> DiscussionPermissions.UPDATE
    DiscPermissionsClient.DELETE -> DiscussionPermissions.DELETE
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