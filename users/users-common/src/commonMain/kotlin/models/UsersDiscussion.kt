package ru.music.common.models

data class UsersDiscussion (
    var id: UsersDiscussionId = UsersDiscussionId.NONE,
    var title: String = "",
    var ownerId: UsersUserId = UsersUserId.NONE,
    var soundUrl: String = "",
    var isOpen: Boolean = true,
    var permissionsClient: MutableList<UsersPermissionsClient> = mutableListOf()
)