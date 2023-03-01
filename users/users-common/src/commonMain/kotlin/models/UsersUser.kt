package ru.music.common.models

data class UsersUser(
    var id: UsersUserId = UsersUserId.NONE,
    var login: String = "",
    var password: String = "",
    var name: String = "",
    var discussions: MutableList<UsersDiscussion> = mutableListOf(),
    var permissionsClient: MutableList<UsersPermissionsClient> = mutableListOf()
)
