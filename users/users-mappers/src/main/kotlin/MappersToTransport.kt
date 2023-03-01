import exceptions.UnknownUsersCommand
import musicBroker.api.v2.models.*
import ru.music.common.UsersContext
import ru.music.common.models.*

fun UsersContext.toTransport() : IResponse = when (command) {
    UsersCommand.ALL_DISCUSSIONS -> toTransportAllDisc()
    UsersCommand.USERS_DISCUSSIONS -> toTransportUsersDisc()
    else -> throw UnknownUsersCommand(command)
}

fun UsersContext.toTransportAllDisc() = AllDiscussionsResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == UsersState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = this.usersResponse.discussions.toTransportUsers()
)

fun UsersContext.toTransportUsersDisc() = UsersDiscussionsResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == UsersState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    discussions = this.usersResponse.discussions.toTransportUsers()
)
private fun UsersDiscussion.toTransportUsers(): DiscussionResponseObject = DiscussionResponseObject(
    id = id.takeIf { it != UsersDiscussionId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UsersUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportUsers(),
)

fun List<UsersDiscussion>.toTransportUsers(): List<DiscussionResponseObject>? = this
    .map { it.toTransportUsers() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<UsersPermissionsClient>.toTransportUsers(): Set<DiscussionPermissions>? = this
    .map { it.toTransportUsers() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun UsersPermissionsClient.toTransportUsers() = when (this) {
    UsersPermissionsClient.READ -> DiscussionPermissions.READ
    UsersPermissionsClient.UPDATE -> DiscussionPermissions.UPDATE
    UsersPermissionsClient.DELETE -> DiscussionPermissions.DELETE
}

private fun List<UsersError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportUsers() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun UsersError.toTransportUsers() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)