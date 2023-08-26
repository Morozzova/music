package ru.music.discussions.base

import io.ktor.server.auth.jwt.*
import permissions.MusicPrincipalModel
import permissions.MusicUserGroups
import ru.music.common.models.DiscUserId
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.F_NAME_CLAIM
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.GROUPS_CLAIM
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.ID_CLAIM
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.L_NAME_CLAIM
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig.Companion.M_NAME_CLAIM

fun JWTPrincipal?.toModel() = this?.run {
    MusicPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { DiscUserId(it) } ?: DiscUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> MusicUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MusicPrincipalModel.NONE
