package ru.music.discussions.stubs

import ru.music.common.models.*

object DiscStubItems {
    val DISCUSSION_FIRST: DiscDiscussion
        get() = DiscDiscussion(
            id = DiscId("678"),
            title = "Helpmeplz",
            ownerId = DiscUserId("user-01"),
            soundUrl = "www.lorem.ipsum",
            status = DiscStatus.OPEN,
            permissionsClient = mutableListOf(
                DiscPermissionsClient.READ,
                DiscPermissionsClient.UPDATE,
                DiscPermissionsClient.DELETE
            )
        )
    val ALL_DISCUSSIONS = listOf(DiscDiscussion(), DiscDiscussion(), DiscDiscussion())

    val USERS_DISCUSSIONS = listOf(DiscDiscussion(), DiscDiscussion())
}
