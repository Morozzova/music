import exceptions.UnknownAnswersCommand
import musicBroker.api.v1.models.*
import ru.music.common.AnswersContext
import ru.music.common.models.*

fun AnswersContext.toTransport() : IResponse = when (command) {
    AnswersCommand.CREATE -> toTransportCreate()
    AnswersCommand.READ -> toTransportRead()
    AnswersCommand.UPDATE -> toTransportUpdate()
    else -> throw UnknownAnswersCommand(command)
}

fun AnswersContext.toTransportCreate() = AnswerCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AnswersState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answer = answersResponse.toTransportAnswers()
)

fun AnswersContext.toTransportRead() = AnswerReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AnswersState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answer = answersResponse.toTransportAnswers()
)
fun AnswersContext.toTransportUpdate() = AnswerUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == AnswersState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    answer = answersResponse.toTransportAnswers()
)

fun List<AnswersAnswer>.toTransportAnswers(): List<AnswerResponseObject>? = this
    .map { it.toTransportAnswers() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AnswersAnswer.toTransportAnswers(): AnswerResponseObject = AnswerResponseObject(
    id = id.takeIf { it != AnswersId.NONE }?.asString(),
    discussionId = discId.takeIf { it != AnswersDiscId.NONE }?.asString(),
    usersId = ownerId.takeIf { it != AnswersUserId.NONE }?.asString(),
    text = text.takeIf { it.isNotBlank() },
    isRight = isRight,
    permissions = permissionsClient.toTransportAnswers(),
)

private fun List<AnswersPermissionsClient>.toTransportAnswers(): Set<AnswerPermissions>? = this
    .map { it.toTransportAnswers() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun AnswersPermissionsClient.toTransportAnswers() = when (this) {
    AnswersPermissionsClient.READ -> AnswerPermissions.READ
    AnswersPermissionsClient.UPDATE -> AnswerPermissions.UPDATE
}

private fun List<AnswersError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportDisk() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AnswersError.toTransportDisk() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)