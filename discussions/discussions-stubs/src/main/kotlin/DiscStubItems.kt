package ru.music.discussions.stubs

import permissions.MusicPermissionClient
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import ru.music.common.models.DiscStatus
import ru.music.common.models.DiscUserId

object DiscStubItems {
    val DISCUSSION_FIRST: DiscDiscussion
        get() = DiscDiscussion(
            id = DiscId("678"),
            title = "Helpmeplz",
            ownerId = DiscUserId("user-01"),
            soundUrl = "www.lorem.ipsum",
            status = DiscStatus.OPEN,
            permissionsClient = mutableSetOf(
                MusicPermissionClient.READ,
                MusicPermissionClient.UPDATE,
                MusicPermissionClient.CLOSE,
                MusicPermissionClient.DELETE
            )
        )
    val ALL_DISCUSSIONS = listOf(DISCUSSION_FIRST, DISCUSSION_FIRST, DISCUSSION_FIRST)

    val USERS_DISCUSSIONS = listOf(DISCUSSION_FIRST, DISCUSSION_FIRST)
}
