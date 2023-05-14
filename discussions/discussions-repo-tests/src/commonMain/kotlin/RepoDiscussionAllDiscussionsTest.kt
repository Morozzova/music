package ru.music.discussions.repo.tests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import repo.IDiscussionRepository
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscStatus
import ru.music.common.models.DiscUserId
import ru.music.common.repo.DbDiscussionUsersIdRequest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoDiscussionAllDiscussionsTest {
    abstract val repo: IDiscussionRepository
    protected open val initializedObjects: List<DiscDiscussion> = initObjects

    @Test
    fun allDiscussions() = runRepoTest {
        val result = repo.allDiscussions(DbDiscussionUsersIdRequest(usersId = null))
        assertEquals(true, result.isSuccess)
        val expected =
            listOf(initializedObjects[0], initializedObjects[1], initializedObjects[2], initializedObjects[3], initializedObjects[4])
        assertEquals(expected, result.data)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitDiscussions("allDiscussions") {

        val ownerId = DiscUserId("owner-333")
        override val initObjects: List<DiscDiscussion> = listOf(
            createInitTestModel("disc1"),
            createInitTestModel("disc2", ownerId = ownerId),
            createInitTestModel("disc3", status = DiscStatus.CLOSED),
            createInitTestModel("disc4", ownerId = ownerId),
            createInitTestModel("disc5", soundUrl = "www.bububu.com"),
        )
    }
}
