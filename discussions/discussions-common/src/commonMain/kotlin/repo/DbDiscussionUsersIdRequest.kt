package ru.music.common.repo

import ru.music.common.models.DiscUserId

data class DbDiscussionUsersIdRequest(
    val usersId: DiscUserId? = DiscUserId.NONE,
)