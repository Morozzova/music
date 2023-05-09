package ru.music.discussions.biz.repo

import DiscussionsRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.assertEquals

private val userId = DiscUserId("6789")
private val initDisc = DiscDiscussion(
    id = DiscId("123"),
    title = "abc",
    soundUrl = "abc",
    status = DiscStatus.OPEN,
    answers = mutableListOf(DiscAnswer("111")),
    ownerId = userId
)

private val repo by lazy {
    DiscussionsRepositoryMock(
        invokeUsersDiscussions = {
            if (it.usersId == initDisc.ownerId) {
                DbDiscussionsResponse(
                    isSuccess = true,
                    data = listOf(initDisc)
                )
            } else {
                DbDiscussionsResponse(
                    isSuccess = false,
                    data = null,
                    errors = listOf(DiscError(message = "Not found", field = "ownerId"))
                )
            }
        }
    )
}

private val settings = DiscCorSettings(
    repoTest = repo
)
private val processor = DiscussionsProcessor(settings)

@OptIn(ExperimentalCoroutinesApi::class)
fun repoUsersIdNotFoundTest(command: DiscCommand) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        multiDiscussionsRequest = DiscMulti(userId)
    )
    processor.exec(ctx)
    assertEquals(DiscState.FAILING, ctx.state)
    assertEquals(2, ctx.multiDiscussionsResponse.size)
    assertEquals(1, ctx.errors.size)
    assertEquals("ownerId", ctx.errors.first().field)
}
