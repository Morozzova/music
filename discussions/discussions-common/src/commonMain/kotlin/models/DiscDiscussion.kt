package ru.music.common.models

data class DiscDiscussion(
    var id: DiscId = DiscId.NONE,
    var title: String = "",
    var ownerId: DiscUserId = DiscUserId.NONE,
    var soundUrl: String = "",
    var status: DiscStatus = DiscStatus.OPEN,
    var permissionsClient: MutableList<DiscPermissionsClient> = mutableListOf()
)
