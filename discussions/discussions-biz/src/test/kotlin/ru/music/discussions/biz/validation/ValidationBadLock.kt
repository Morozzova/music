package ru.music.discussions.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockCorrect(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = DiscId("123-234-abc-ABC"),
            title = "abc",
            soundUrl = "abc",
            status = DiscStatus.OPEN,
            answers = mutableListOf(DiscAnswer("aaa")),
            lock = DiscLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockTrim(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = DiscId("123-234-abc-ABC"),
            title = "abc",
            soundUrl = "abc",
            status = DiscStatus.OPEN,
            answers = mutableListOf(DiscAnswer("aaa")),
            lock = DiscLock(" \n\t 123-234-abc-ABC \n\t "),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockEmpty(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = DiscId("123-234-abc-ABC"),
            title = "abc",
            soundUrl = "abc",
            status = DiscStatus.OPEN,
            answers = mutableListOf(DiscAnswer("aaa")),
            lock = DiscLock(""),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationLockFormat(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = DiscId("123-234-abc-ABC"),
            title = "abc",
            soundUrl = "abc",
            status = DiscStatus.OPEN,
            answers = mutableListOf(DiscAnswer("aaa")),
            lock = DiscLock("!@#\$%^&*(),.{}"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
