package ru.music.common.models

data class DiscDiscussion(
    var id: DiscId = DiscId.NONE,
    var title: String = "",
    var ownerId: DiscUserId = DiscUserId.NONE,
    var soundUrl: String = "",
    var status: DiscStatus = DiscStatus.NONE,
    var answers: MutableList<DiscAnswer> = mutableListOf(),
    var lock: DiscLock = DiscLock.NONE,
    var permissionsClient: MutableSet<DiscPermissionsClient> = mutableSetOf()
) {
    fun deepCopy(): DiscDiscussion = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )
    fun isEmpty() = this == NONE

    companion object {
        val NONE = DiscDiscussion()
    }
}