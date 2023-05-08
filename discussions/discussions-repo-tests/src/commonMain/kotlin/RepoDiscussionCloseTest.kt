import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionRequest
import repo.IDiscussionRepository
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import ru.music.common.models.DiscStatus
import ru.music.common.models.DiscUserId
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionCloseTest {
    abstract val repo: IDiscussionRepository
    protected open val closeSucc = initObjects[0]
    private val closeIdNotFound = DiscId("discussion-repo-close-not-found")

    private val reqCloseSucc by lazy {
        DiscDiscussion(
            id = closeSucc.id,
            title = closeSucc.title,
            soundUrl = closeSucc.soundUrl,
            ownerId = closeSucc.ownerId,
            status = DiscStatus.CLOSED,
            answers = mutableListOf()
        )
    }
    private val reqCloseNotFound = DiscDiscussion(
        id = closeIdNotFound,
        title = "close object not found",
        soundUrl = "close object not found url",
        ownerId = DiscUserId("owner-123"),
        status = DiscStatus.NONE,
        answers = mutableListOf()
    )

    @Test
    fun closeSuccess() = runRepoTest {
        val result = repo.closeDiscussion(DbDiscussionRequest(reqCloseSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqCloseSucc.id, result.data?.id)
        assertEquals(reqCloseSucc.title, result.data?.title)
        assertEquals(reqCloseSucc.soundUrl, result.data?.soundUrl)
        assertEquals(reqCloseSucc.status, result.data?.status)
        assertEquals(reqCloseSucc.answers, result.data?.answers)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun closeNotFound() = runRepoTest {
        val result = repo.closeDiscussion(DbDiscussionRequest(reqCloseNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDiscussions("close") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("close"),
            createInitTestModel("closeConc"),
        )
    }
}