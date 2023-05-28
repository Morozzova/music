package ru.music.discussions.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.DbDiscussionRequest
import repo.IDiscussionRepository
import ru.music.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionCreateTest {
    abstract val repo: IDiscussionRepository

    protected open val lockNew: DiscLock = DiscLock("20000000-0000-0000-0000-000000000002")

    private val createObj = DiscDiscussion(
        title = "create object",
        soundUrl = "www.bebebe.com",
        ownerId = DiscUserId("owner-123"),
        status = DiscStatus.OPEN,
        answers = mutableListOf()
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createDiscussion(DbDiscussionRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: DiscId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.soundUrl, result.data?.soundUrl)
        assertEquals(expected.status, result.data?.status)
        assertNotEquals(DiscId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitDiscussions("create") {
        override val initObjects: List<DiscDiscussion> = emptyList()
    }
}
