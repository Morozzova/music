import kotlinx.datetime.Clock
import musicBroker.api.logs.models.CommonLogModel
import musicBroker.api.logs.models.DiscussionLog
import musicBroker.api.logs.models.DiscussionLogModel
import musicBroker.api.logs.models.ErrorLogModel
import ru.music.common.DiscContext
import ru.music.common.models.*

fun DiscContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "music-broker",
    discussion = toDiscLog(),
    errors = errors.map { it.toLog() },
)

fun DiscContext.toDiscLog(): DiscussionLogModel? {
    val discussionNone = DiscDiscussion()
    return DiscussionLogModel(
        requestId = requestId.takeIf { it != DiscRequestId.NONE }?.asString(),
        requestDiscussion = discussionRequest.takeIf { it != discussionNone }?.toLog(),
        responseDiscussion = discussionResponse.takeIf { it != discussionNone }?.toLog(),
        responseDiscussions = multiDiscussionsResponse.takeIf { it.isNotEmpty() }?.filter { it != discussionNone }?.map { it.toLog() },
    ).takeIf { it != DiscussionLogModel() }
}

fun DiscError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)

fun DiscDiscussion.toLog() = DiscussionLog(
    id = id.takeIf { it != DiscId.NONE }?.asString(),
    soundUrl = soundUrl.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    status = status.takeIf { it != DiscStatus.NONE }?.name,
    answers = answers.map { it.asString() }.takeIf { it.isNotEmpty() },
    ownerId = ownerId.takeIf { it != DiscUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
