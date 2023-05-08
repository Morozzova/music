import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionIdRequest
import repo.IDiscussionRepository
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionReadTest {
    abstract val repo: IDiscussionRepository
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readDiscussion(DbDiscussionIdRequest(readSucc.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSucc, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readDiscussion(DbDiscussionIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }
    companion object : BaseInitDiscussions("read") {
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("read")
        )
        val notFoundId = DiscId("discussions-repo-read-notFound")
    }
}