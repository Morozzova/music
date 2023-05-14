import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionRequest
import repo.IDiscussionRepository
import ru.music.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionCloseTest {
    abstract val repo: IDiscussionRepository
    protected open val closeSucc = initObjects[0]
    protected open val closeConc = initObjects[1]
    protected val closeIdNotFound = DiscId("discussion-repo-close-not-found")
    protected val lockBad = DiscLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = DiscLock("20000000-0000-0000-0000-000000000002")

    private val reqCloseSucc by lazy {
        DiscDiscussion(
            id = closeSucc.id,
            title = closeSucc.title,
            soundUrl = closeSucc.soundUrl,
            ownerId = closeSucc.ownerId,
            status = DiscStatus.CLOSED,
            answers = mutableListOf(),
            lock = initObjects.first().lock,
        )
    }
    private val reqCloseNotFound = DiscDiscussion(
        id = closeIdNotFound,
        title = "close object not found",
        soundUrl = "close object not found url",
        ownerId = DiscUserId("owner-123"),
        status = DiscStatus.NONE,
        answers = mutableListOf(),
        lock = initObjects.first().lock,
    )

    private val reqCloseConc by lazy {
        DiscDiscussion(
            id = closeConc.id,
            title = "update object not found",
            soundUrl = "update object not found url",
            ownerId = DiscUserId("owner-123"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = lockBad,
        )
    }
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
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun closeNotFound() = runRepoTest {
        val result = repo.closeDiscussion(DbDiscussionRequest(reqCloseNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateDiscussion(DbDiscussionRequest(reqCloseConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(closeConc, result.data)
    }
    companion object : BaseInitDiscussions("close") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("close"),
            createInitTestModel("closeConc"),
        )
    }
}