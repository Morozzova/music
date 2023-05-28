package ru.music.auth

import permissions.MusicPrincipalModel
import permissions.MusicPrincipalRelations
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId

fun DiscDiscussion.resolveRelationsTo(principal: MusicPrincipalModel): Set<MusicPrincipalRelations> = setOfNotNull(
    MusicPrincipalRelations.NONE,
    MusicPrincipalRelations.NEW.takeIf { id == DiscId.NONE },
    MusicPrincipalRelations.OWN.takeIf { principal.id == ownerId }
)
