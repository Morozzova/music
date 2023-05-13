package ru.music.discussions.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.music.common.DiscContext
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.stubs.DiscStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = DiscStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationSoundUrlCorrect(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
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
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
    assertEquals("www.sound.ru", ctx.discussionValidated.soundUrl)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationSoundUrlTrim(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "abc",
            soundUrl = " \n\twww.sound.ru \n\t",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
    assertEquals("www.sound.ru", ctx.discussionValidated.soundUrl)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationSoundUrlEmpty(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "abc",
            soundUrl = "",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("soundUrl", error?.field)
    assertContains(error?.message ?: "", "soundUrl")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationSoundUrlSymbols(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        discussionRequest = DiscDiscussion(
            id = stub.id,
            title = "abc",
            soundUrl = "!@#$%^&*(),.{}",
            ownerId = DiscUserId("555"),
            status = DiscStatus.OPEN,
            answers = mutableListOf(),
            lock = DiscLock("123-234-abc-ABC")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("soundUrl", error?.field)
    assertContains(error?.message ?: "", "soundUrl")
}
