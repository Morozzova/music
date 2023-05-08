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

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(deleteSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(deleteSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.deleteDiscussion(DbDiscussionIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitDiscussions("delete") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val notFoundId = DiscId("discussions-repo-delete-notFound")
    }
}