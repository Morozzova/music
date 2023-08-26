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

private val stub = DiscStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "abc",
            soundUrl = "www.sound.ru",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
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
    assertEquals("abc", ctx.discussionValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = " \n\t abc \t\n ",
            soundUrl = "www.sound.ru",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
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
    assertEquals("abc", ctx.discussionValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "",
            soundUrl = "www.sound.ru",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "!@#\$%^&*(),.{}",
            soundUrl = "www.sound.ru",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
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
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
