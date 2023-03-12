import exceptions.UnknownRequestClass
import musicBroker.api.v1.models.*
import ru.music.common.AnswersContext
import ru.music.common.models.*
import ru.music.common.stubs.AnswersStubs

fun AnswersContext.fromTransport(request: IRequest) = when (request) {
    is AnswerCreateRequest -> fromTransport(request)
    is AnswerReadRequest -> fromTransport(request)
    is AnswerUpdateRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun IRequest?.requestId() = this?.requestId?.let { AnswersRequestId(it) } ?: AnswersRequestId.NONE
private fun String?.toAnswersId() = this?.let { AnswersId(it) } ?: AnswersId.NONE
private fun String?.toAnswersDiscId() = this?.let { AnswersDiscId(it) } ?: AnswersDiscId.NONE
private fun String?.toAnswersUserId() = this?.let { AnswersUserId(it) } ?: AnswersUserId.NONE
private fun String?.toAnswerWithId() = AnswersAnswer(id = this.toAnswersId())

private fun AnswerCreateObject.toInternal(): AnswersAnswer = AnswersAnswer(
    discId = this.discussionId.toAnswersDiscId(),
    ownerId = this.usersId.toAnswersUserId(),
    text = this.text ?: "",
    isRight = this.isRight ?: false
)

private fun AnswerUpdateObject.toInternal(): AnswersAnswer = AnswersAnswer(
    id = this.id.toAnswersId(),
    discId = this.discussionId.toAnswersDiscId(),
    ownerId = this.usersId.toAnswersUserId(),
    text = this.text ?: "",
    isRight = this.isRight ?: false
)


fun AnswerDebug?.transportToWorkMode() = when(this?.mode) {
    AnswerRequestDebugMode.PROD -> AnswersWorkMode.PROD
    AnswerRequestDebugMode.TEST -> AnswersWorkMode.TEST
    AnswerRequestDebugMode.STUB -> AnswersWorkMode.STUB
    null -> AnswersWorkMode.PROD
}

fun AnswerDebug?.transportToStubCase() = when(this?.stub) {
    AnswerRequestDebugStubs.SUCCESS -> AnswersStubs.SUCCESS
    AnswerRequestDebugStubs.NOT_FOUND -> AnswersStubs.NOT_FOUND
    AnswerRequestDebugStubs.BAD_DISCUSSION -> AnswersStubs.BAD_DISCUSSION
    AnswerRequestDebugStubs.BAD_TEXT -> AnswersStubs.BAD_TEXT
    null -> AnswersStubs.NONE
}
fun AnswersContext.fromTransport(request: AnswerCreateRequest) {
    command = AnswersCommand.CREATE
    requestId = request.requestId()
    answersRequest = request.answer?.toInternal() ?: AnswersAnswer()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AnswersContext.fromTransport(request: AnswerReadRequest) {
    command = AnswersCommand.READ
    requestId = request.requestId()
    answersRequest = request.answer?.id?.toAnswerWithId() ?: AnswersAnswer()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AnswersContext.fromTransport(request: AnswerUpdateRequest) {
    command = AnswersCommand.UPDATE
    requestId = request.requestId()
    answersRequest = request.answer?.toInternal() ?: AnswersAnswer()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}