package ru.music.discussions.biz.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import permissions.MusicPermissionClient
import permissions.MusicPrincipalModel
import permissions.MusicUserGroups
import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.inmemory.DiscussionsRepoInMemory
import ru.music.discussions.stubs.DiscStub
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DiscussionCrudAuthTest {
    @Test
    fun createSuccessTest() = runTest {
        val userId = DiscUserId("123")
        val repo = DiscussionsRepoInMemory()
        val processor = DiscussionsProcessor(
            settings = DiscCorSettings(
                repoTest = repo
            )
        )
        val context = DiscContext(
            workMode = DiscWorkMode.TEST,
            discussionRequest = DiscStub.prepareResult {
                permissionsClient.clear()
                id = DiscId.NONE
            },
            command = DiscCommand.CREATE,
            principal = MusicPrincipalModel(
                id = userId,
                groups = setOf(
                    MusicUserGroups.USER,
                    MusicUserGroups.TEST,
                )
            )
        )

        processor.exec(context)
        assertEquals(DiscState.FINISHING, context.state)
        with(context.discussionResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, permissions.MusicPermissionClient.READ)
            assertContains(permissionsClient, permissions.MusicPermissionClient.UPDATE)
            assertContains(permissionsClient, permissions.MusicPermissionClient.DELETE)
//            assertFalse { permissionsClient.contains(PermissionModel.CONTACT) }
        }
    }

    @Test
    fun readSuccessTest() = runTest {
        val adObj = DiscStub.get()
        val userId = adObj.ownerId
        val adId = adObj.id
        val repo = DiscussionsRepoInMemory(initObjects = listOf(adObj))
        val processor = DiscussionsProcessor(
            settings = DiscCorSettings(
                repoTest = repo
            )
        )
        val context = DiscContext(
            command = DiscCommand.READ,
            workMode = DiscWorkMode.TEST,
            discussionRequest = DiscDiscussion(id = adId),
            principal = MusicPrincipalModel(
                id = userId,
                groups = setOf(
                    MusicUserGroups.USER,
                    MusicUserGroups.TEST,
                )
            )
        )
        processor.exec(context)
        assertEquals(DiscState.FINISHING, context.state)
        with(context.discussionResponse) {
            assertTrue { id.asString().isNotBlank() }
            assertContains(permissionsClient, MusicPermissionClient.READ)
            assertContains(permissionsClient, MusicPermissionClient.UPDATE)
            assertContains(permissionsClient, MusicPermissionClient.DELETE)
//            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
        }
    }

}
