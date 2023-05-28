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
abstract class RepoDiscussionUsersDiscussionsTest {
    abstract val repo: IDiscussionRepository
    protected open val initializedObjects: List<DiscDiscussion> = initObjects

    @Test
    fun usersDiscussions() = runRepoTest {
        val result = repo.usersDiscussions(DbDiscussionUsersIdRequest(usersId = ownerId))
        assertEquals(true, result.isSuccess)
        val expected =
            listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
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