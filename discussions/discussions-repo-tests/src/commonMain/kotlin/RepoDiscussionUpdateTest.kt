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
abstract class RepoDiscussionUpdateTest {
    abstract val repo: IDiscussionRepository
    protected open val updateSucc = initObjects[0]
    private val updateIdNotFound = DiscId("discussion-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        DiscDiscussion(
            id = updateSucc.id,
            title = "create object",
            soundUrl = "www.bebebe.com",
            ownerId = DiscUserId("owner-123"),
            status = DiscStatus.OPEN,
            answers = mutableListOf()
        )
    }
    private val reqUpdateNotFound = DiscDiscussion(
        id = updateIdNotFound,
        title = "update object not found",
        soundUrl = "update object not found url",
        ownerId = DiscUserId("owner-123"),
        status = DiscStatus.OPEN,
        answers = mutableListOf()
    )

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
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateDiscussion(DbDiscussionRequest(reqUpdateNotFound))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDiscussions("update") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}