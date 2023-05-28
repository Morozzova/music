package ru.music.discussions.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import kotlin.test.assertEquals

private val userId = DiscUserId("6789")
private val initDisc = listOf(
    DiscDiscussion(
        id = DiscId("123"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("111")),
        ownerId = userId
    ),
    DiscDiscussion(
        id = DiscId("124"),
        title = "abc",
        soundUrl = "abc",
        status = DiscStatus.OPEN,
        answers = mutableListOf(DiscAnswer("111")),
        ownerId = userId
    )
)

private val repo by lazy {
    DiscussionsRepositoryMock(
        invokeUsersDiscussions = {
            if (initDisc.map { it.ownerId }.contains(it.usersId)) {
                DbDiscussionsResponse(
                    isSuccess = true,
                    data = initDisc
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
        multiDiscussionsRequest = DiscMulti(DiscUserId(""))
    )
    processor.exec(ctx)
    assertEquals(DiscState.FAILING, ctx.state)
    assertEquals(0, ctx.multiDiscussionsResponse.size)
    assertEquals(1, ctx.errors.size)
    assertEquals("ownerId", ctx.errors.first().field)
}
