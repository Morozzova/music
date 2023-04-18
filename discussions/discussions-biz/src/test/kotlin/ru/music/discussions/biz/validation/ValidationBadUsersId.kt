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
fun validationUsersIdCorrect(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        multiDiscussionsRequest = DiscMulti(DiscUserId("555")),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationUsersIdTrim(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        multiDiscussionsRequest = DiscMulti(DiscUserId(" \n\t 555 \n\t "))
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(DiscState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationUsersIdEmpty(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        multiDiscussionsRequest = DiscMulti(DiscUserId(""))
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ownerId", error?.field)
    assertContains(error?.message ?: "", "ownerId")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationUsersIdFormat(command: DiscCommand, processor: DiscussionsProcessor) = runTest {
    val ctx = DiscContext(
        command = command,
        state = DiscState.NONE,
        workMode = DiscWorkMode.TEST,
        multiDiscussionsRequest = DiscMulti(DiscUserId("!@#\$%^&*(),.{}"))
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(DiscState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("ownerId", error?.field)
    assertContains(error?.message ?: "", "id")
}
