import exceptions.UnknownRequestClass
import musicBroker.api.v2.models.*
import ru.music.common.UsersContext
import ru.music.common.models.*
import ru.music.common.stubs.UsersStubs

fun UsersContext.fromTransport(request: IRequest) = when (request) {
    is AllDiscussionsRequest -> fromTransport(request)
    is UsersDiscussionsRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IRequest?.requestId() = this?.requestId?.let { UsersRequestId(it) } ?: UsersRequestId.NONE
private fun String?.toUsersId() = this?.let { UsersUserId(it) } ?: UsersUserId.NONE
private fun AllDiscussionsReadObject.toInternal(): UsersUser = UsersUser()

private fun String?.toUserWithId() = UsersUser(id = this.toUsersId())

fun UserDebug?.transportToWorkMode() = when (this?.mode) {
    UserRequestDebugMode.PROD -> UsersWorkMode.PROD
    UserRequestDebugMode.TEST -> UsersWorkMode.TEST
    UserRequestDebugMode.STUB -> UsersWorkMode.STUB
    null -> UsersWorkMode.PROD
}

fun UserDebug?.transportToStubCase() = when (this?.stub) {
    UserRequestDebugStubs.SUCCESS -> UsersStubs.SUCCESS
    UserRequestDebugStubs.NOT_FOUND -> UsersStubs.NOT_FOUND
    UserRequestDebugStubs.BAD_ID -> UsersStubs.BAD_ID
    null -> UsersStubs.NONE
}

fun UsersContext.fromTransport(request: AllDiscussionsRequest) {
    command = UsersCommand.ALL_DISCUSSIONS
    requestId = request.requestId()
    usersRequest = request.allDiscussions?.toInternal() ?: UsersUser()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun UsersContext.fromTransport(request: UsersDiscussionsRequest) {
    command = UsersCommand.USERS_DISCUSSIONS
    requestId = request.requestId()
    usersRequest = request.usersId.toUserWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}