package ru.music.discussions.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import repo.DbDiscussionResponse
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.tests.DiscussionsRepositoryMock
import kotlin.test.assertEquals

private val initDisc = DiscDiscussion(
    id = DiscId("123"),
    title = "abc",
    soundUrl = "abc",
    status = DiscStatus.OPEN,
    answers = mutableListOf(DiscAnswer("111"))
)

private val repo by lazy {
    DiscussionsRepositoryMock(
        invokeReadDiscussion = {
            if (it.id == initDisc.id) {
                DbDiscussionResponse(
                    isSuccess = true,
                    data = initDisc
                )
            } else {
                DbDiscussionResponse(
                    isSuccess = false,
                    data = null,
                    errors = listOf(DiscError(message = "Not found", field = "id"))
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
fun repoNotFoundTest(command: DiscCommand) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = DiscId("12345"),
            title = "xyz",
            soundUrl = "xyz",
            status = DiscStatus.CLOSED,
            answers = mutableListOf(DiscAnswer("000"), DiscAnswer("111")),
            lock = DiscLock("123-234-abc-ABC")
        ),
    )
    processor.exec(ctx)
    assertEquals(DiscState.FAILING, ctx.state)
    assertEquals(DiscDiscussion(), ctx.discussionResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
