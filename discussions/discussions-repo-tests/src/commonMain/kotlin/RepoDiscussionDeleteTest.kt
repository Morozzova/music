import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionIdRequest
import repo.IDiscussionRepository
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionDeleteTest {
    abstract val repo: IDiscussionRepository
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(deleteSucc.id, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
        assertEquals(lockOld, result.data?.lock)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readDiscussion(DbDiscussionIdRequest(notFoundId, lock = lockOld))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(deleteConc.id, lock = lockBad))

        assertEquals(false, result.isSuccess)
        val error = result.errors.find { it.code == "concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(lockOld, result.data?.lock)
    }

    companion object : BaseInitDiscussions("delete") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )

        val notFoundId = DiscId("discussions-repo-delete-notFound")
    }
}