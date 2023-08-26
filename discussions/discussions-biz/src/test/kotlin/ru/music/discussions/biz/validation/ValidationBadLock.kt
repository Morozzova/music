package ru.music.discussions.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import permissions.MusicPrincipalModel
import permissions.MusicUserGroups
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.stubs.DiscStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DiscStub.prepareResult { id = DiscId("123-234-abc-ABC") }

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
        principal = MusicPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                MusicUserGroups.USER,
                MusicUserGroups.TEST,
            )
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
        principal = MusicPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                MusicUserGroups.USER,
                MusicUserGroups.TEST,
            )
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
        principal = MusicPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                MusicUserGroups.USER,
                MusicUserGroups.TEST,
            )
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
        principal = MusicPrincipalModel(
            id = stub.ownerId,
            groups = setOf(
                MusicUserGroups.USER,
                MusicUserGroups.TEST,
            )
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("lock", error?.field)
    assertContains(error?.message ?: "", "id")
}
