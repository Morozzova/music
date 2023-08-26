package ru.music.auth

import permissions.MusicPrincipalRelations
import permissions.MusicUserPermissions
import ru.music.common.models.DiscCommand

fun checkPermitted(
    command: DiscCommand,
    relations: Iterable<MusicPrincipalRelations>,
    permissions: Iterable<MusicUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: DiscCommand,
    val permission: MusicUserPermissions,
    val relation: MusicPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = DiscCommand.CREATE,
        permission = MusicUserPermissions.CREATE_OWN,
        relation = MusicPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = DiscCommand.READ,
        permission = MusicUserPermissions.READ_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = DiscCommand.READ,
        permission = MusicUserPermissions.READ_PUBLIC,
        relation = MusicPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = DiscCommand.UPDATE,
        permission = MusicUserPermissions.UPDATE_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,

    // Close
    AccessTableConditions(
        command = DiscCommand.CLOSE,
        permission = MusicUserPermissions.CLOSE_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,

    // Delete
    AccessTableConditions(
        command = DiscCommand.DELETE,
        permission = MusicUserPermissions.DELETE_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,

    // All discussions
    AccessTableConditions(
        command = DiscCommand.ALL_DISCUSSIONS,
        permission = MusicUserPermissions.ALL_DISCUSSIONS_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,

    // Users discussions
    AccessTableConditions(
        command = DiscCommand.USERS_DISCUSSIONS,
        permission = MusicUserPermissions.USERS_DISCUSSIONS_OWN,
        relation = MusicPrincipalRelations.OWN,
    ) to true,
)
