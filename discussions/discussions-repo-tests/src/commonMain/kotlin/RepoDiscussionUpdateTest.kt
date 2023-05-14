import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionRequest
import repo.IDiscussionRepository
import ru.music.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionUpdateTest {
    abstract val repo: IDiscussionRepository
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = DiscId("discussion-repo-update-not-found")
    protected val lockBad = DiscLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = DiscLock("20000000-0000-0000-0000-000000000002")


    private val reqUpdateSucc by lazy {
        DiscDiscussion(
            id = updateSucc.id,
            title = "create object",
            soundUrl = "www.bebebe.com",
            ownerId = DiscUserId("owner-123"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = DiscDiscussion(
        id = updateIdNotFound,
        title = "update object not found",
        soundUrl = "update object not found url",
        ownerId = DiscUserId("owner-123"),
        status = DiscStatus.OPEN,
        answers = mutableListOf(),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        DiscDiscussion(
            id = updateConc.id,
            title = "update object not found",
            soundUrl = "update object not found url",
            ownerId = DiscUserId("owner-123"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = lockBad,
        )
    }
    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateDiscussion(DbDiscussionRequest(reqUpdateSucc))
        assertEquals(true, result.isSuccess)
        assertEquals(reqUpdateSucc.id, result.data?.id)
        assertEquals(reqUpdateSucc.title, result.data?.title)
        assertEquals(reqUpdateSucc.soundUrl, result.data?.soundUrl)
        assertEquals(reqUpdateSucc.status, result.data?.status)
        assertEquals(reqUpdateSucc.answers, result.data?.answers)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockNew, result.data?.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDiscussion(DbDiscussionRequest(reqUpdateNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateDiscussion(DbDiscussionRequest(reqUpdateConc))
        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }


    companion object : BaseInitDiscussions("update") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}