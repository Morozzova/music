package ru.music.auth

import permissions.MusicUserGroups
import permissions.MusicUserPermissions

fun resolveChainPermissions(
    groups: Iterable<MusicUserGroups>,
) = mutableSetOf<MusicUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    MusicUserGroups.USER to setOf(
        MusicUserPermissions.READ_OWN,
        MusicUserPermissions.READ_PUBLIC,
        MusicUserPermissions.CREATE_OWN,
        MusicUserPermissions.UPDATE_OWN,
        MusicUserPermissions.CLOSE_OWN,
        MusicUserPermissions.DELETE_OWN,
        MusicUserPermissions.ALL_DISCUSSIONS_OWN,
        MusicUserPermissions.USERS_DISCUSSIONS_OWN,
    ),
    MusicUserGroups.MODERATOR_MUSIC to setOf(),
    MusicUserGroups.ADMIN_DISC to setOf(),
    MusicUserGroups.TEST to setOf(),
    MusicUserGroups.BAN_DISC to setOf(),
)

private val groupPermissionsDenys = mapOf(
    MusicUserGroups.USER to setOf(),
    MusicUserGroups.MODERATOR_MUSIC to setOf(),
    MusicUserGroups.ADMIN_DISC to setOf(),
    MusicUserGroups.TEST to setOf(),
    MusicUserGroups.BAN_DISC to setOf(
        MusicUserPermissions.UPDATE_OWN,
        MusicUserPermissions.CLOSE_OWN,
        MusicUserPermissions.CREATE_OWN,
    ),
)
