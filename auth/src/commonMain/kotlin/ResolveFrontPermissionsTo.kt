package ru.music.auth

import permissions.MusicPermissionClient
import permissions.MusicPrincipalRelations
import permissions.MusicUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<MusicUserPermissions>,
    relations: Iterable<MusicPrincipalRelations>,
) = mutableSetOf<MusicPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    MusicUserPermissions.READ_OWN to mapOf(
        MusicPrincipalRelations.OWN to MusicPermissionClient.READ
    ),
    MusicUserPermissions.READ_GROUP to mapOf(
        MusicPrincipalRelations.GROUP to MusicPermissionClient.READ
    ),
    MusicUserPermissions.READ_PUBLIC to mapOf(
        MusicPrincipalRelations.PUBLIC to MusicPermissionClient.READ
    ),
    MusicUserPermissions.READ_CANDIDATE to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.READ
    ),

    // UPDATE
    MusicUserPermissions.UPDATE_OWN to mapOf(
        MusicPrincipalRelations.OWN to MusicPermissionClient.UPDATE
    ),
    MusicUserPermissions.UPDATE_PUBLIC to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.UPDATE
    ),
    MusicUserPermissions.UPDATE_CANDIDATE to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.UPDATE
    ),

    // CLOSE
    MusicUserPermissions.UPDATE_OWN to mapOf(
        MusicPrincipalRelations.OWN to MusicPermissionClient.CLOSE
    ),
    MusicUserPermissions.UPDATE_PUBLIC to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.CLOSE
    ),
    MusicUserPermissions.UPDATE_CANDIDATE to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.CLOSE
    ),

    // DELETE
    MusicUserPermissions.DELETE_OWN to mapOf(
        MusicPrincipalRelations.OWN to MusicPermissionClient.DELETE
    ),
    MusicUserPermissions.DELETE_PUBLIC to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.DELETE
    ),
    MusicUserPermissions.DELETE_CANDIDATE to mapOf(
        MusicPrincipalRelations.MODERATABLE to MusicPermissionClient.DELETE
    ),
)
