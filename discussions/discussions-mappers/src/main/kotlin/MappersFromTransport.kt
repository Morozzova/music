import exceptions.UnknownRequestClass
import musicBroker.api.v1.models.*
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs

fun DiscContext.fromTransport(request: IRequest) = when (request) {
    is DiscussionCreateRequest -> fromTransport(request)
    is DiscussionReadRequest -> fromTransport(request)
    is DiscussionUpdateRequest -> fromTransport(request)
    is DiscussionCloseRequest -> fromTransport(request)
    is DiscussionDeleteRequest -> fromTransport(request)
    is AllDiscussionsRequest -> fromTransport(request)
    is UsersDiscussionsRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IRequest?.requestId() = this?.requestId?.let { DiscRequestId(it) } ?: DiscRequestId.NONE
private fun String?.toDiscId() = this?.let { DiscId(it) } ?: DiscId.NONE
private fun String?.toDiscWithId() = DiscDiscussion(id = this.toDiscId())
private fun String?.toUserId() = this?.let { DiscUserId(it) } ?: DiscUserId.NONE
private fun String?.toAnswer() = this?.let { DiscAnswer(it) } ?: DiscAnswer.NONE

private fun DiscussionCreateObject.toInternal(): DiscDiscussion = DiscDiscussion(
    title = this.title ?: "",
    soundUrl = this.soundUrl ?: "",
    answers = this.answers.fromTransport() ?: mutableListOf(),
    status = this.status.fromTransportStatus()
)

private fun DiscussionUpdateObject.toInternal(): DiscDiscussion = DiscDiscussion(
    id = this.id.toDiscId(),
    title = this.title ?: "",
    soundUrl = this.soundUrl ?: "",
    answers = this.answers.fromTransport() ?: mutableListOf(),
    status = this.status.fromTransportStatus()
)

private fun DiscussionCloseObject.toInternal(): DiscDiscussion = DiscDiscussion(
    id = this.id.toDiscId(),
    title = this.title ?: "",
    soundUrl = this.soundUrl ?: "",
    answers = this.answers.fromTransport() ?: mutableListOf(),
    status = this.status.fromTransportStatus()
)

private fun AllDiscussionsReadObject.toInternal(): DiscMulti = DiscMulti()
private fun String.toDiscMulti(): DiscMulti = DiscMulti(
    id = this.toUserId()
)

private fun DiscussionStatus?.fromTransportStatus(): DiscStatus = when (this) {
    DiscussionStatus.OPEN -> DiscStatus.OPEN
    DiscussionStatus.CLOSED -> DiscStatus.CLOSED
    else -> DiscStatus.OPEN
}

private fun List<String>?.fromTransport(): MutableList<DiscAnswer>? = this
    ?.map { it.toAnswer() }
    ?.toMutableList()
    .takeIf { !it.isNullOrEmpty() }

fun DiscussionDebug?.transportToWorkMode() = when (this?.mode) {
    DiscussionRequestDebugMode.PROD -> DiscWorkMode.PROD
    DiscussionRequestDebugMode.TEST -> DiscWorkMode.TEST
    DiscussionRequestDebugMode.STUB -> DiscWorkMode.STUB
    null -> DiscWorkMode.PROD
}

fun DiscussionDebug?.transportToStubCase() = when (this?.stub) {
    DiscussionRequestDebugStubs.SUCCESS -> DiscStubs.SUCCESS
    DiscussionRequestDebugStubs.NOT_FOUND -> DiscStubs.NOT_FOUND
    DiscussionRequestDebugStubs.BAD_ID -> DiscStubs.BAD_ID
    DiscussionRequestDebugStubs.BAD_TITLE -> DiscStubs.BAD_TITLE
    DiscussionRequestDebugStubs.BAD_FILE -> DiscStubs.BAD_FILE
    DiscussionRequestDebugStubs.CANNOT_DELETE -> DiscStubs.CANNOT_DELETE
    null -> DiscStubs.NONE
}

fun DiscContext.fromTransport(request: DiscussionCreateRequest) {
    command = DiscCommand.CREATE
    requestId = request.requestId()
    discussionRequest = request.discussion?.toInternal() ?: DiscDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: DiscussionReadRequest) {
    command = DiscCommand.READ
    requestId = request.requestId()
    discussionRequest = request.discussion?.id.toDiscWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: DiscussionUpdateRequest) {
    command = DiscCommand.UPDATE
    requestId = request.requestId()
    discussionRequest = request.discussion?.toInternal() ?: DiscDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: DiscussionCloseRequest) {
    command = DiscCommand.CLOSE
    requestId = request.requestId()
    discussionRequest = request.discussion?.toInternal() ?: DiscDiscussion()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: DiscussionDeleteRequest) {
    command = DiscCommand.DELETE
    requestId = request.requestId()
    discussionRequest = request.discussion?.id.toDiscWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: AllDiscussionsRequest) {
    command = DiscCommand.ALL_DISCUSSIONS
    requestId = request.requestId()
    multiDiscussionsRequest = request.allDiscussions?.toInternal() ?: DiscMulti()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DiscContext.fromTransport(request: UsersDiscussionsRequest) {
    command = DiscCommand.USERS_DISCUSSIONS
    requestId = request.requestId()
    multiDiscussionsRequest = request.usersId?.toDiscMulti() ?: DiscMulti()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}