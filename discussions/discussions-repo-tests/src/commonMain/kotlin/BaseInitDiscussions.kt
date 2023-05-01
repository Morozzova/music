import ru.music.common.models.*

abstract class BaseInitDiscussions(val op: String): IInitObjects<DiscDiscussion> {

    fun createInitTestModel(
        suf: String,
        ownerId: DiscUserId = DiscUserId("owner-555"),
        soundUrl: String = "sound",
        status: DiscStatus = DiscStatus.CLOSED,
        answers: MutableList<DiscAnswer> = mutableListOf()
    ) = DiscDiscussion(
        id = DiscId("discussion-repo-$op-$suf"),
        title = "$suf stub",
        soundUrl = soundUrl,
        ownerId = ownerId,
        status = status,
        answers = answers
    )
}
