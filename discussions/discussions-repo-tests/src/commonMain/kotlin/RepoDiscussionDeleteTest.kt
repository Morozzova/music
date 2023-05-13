import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionIdRequest
import repo.IDiscussionRepository
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionDeleteTest {
    abstract val repo: IDiscussionRepository

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(successId, lock = lockOld))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readDiscussion(DbDiscussionIdRequest(notFoundId))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun deleteConcurrency() = runTest {
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(concurrencyId, lock = lockBad))

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
        val successId = DiscId(initObjects[0].id.asString())
        val notFoundId = DiscId("discussions-repo-delete-notFound")
        val concurrencyId = DiscId(initObjects[1].id.asString())
    }
}