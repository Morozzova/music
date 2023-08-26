package ru.music.discussions.biz.permissions

import models.DiscMultiPermissions
import permissions.MusicUserPermissions
import ru.music.auth.resolveRelationsTo
import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.chain
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.accessValidationMulti(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == DiscState.RUNNING }
    worker("Вычисление отношения обсуждения к принципалу") {
        discussionRepoRead.principalRelations = discussionRepoRead.resolveRelationsTo(principal)
        discussionValidatedMulti.multiPermissions = setOfNotNull(
            DiscMultiPermissions.OWN.takeIf { permissionsChain.contains(MusicUserPermissions.USERS_DISCUSSIONS_OWN) },
            DiscMultiPermissions.OWN.takeIf { permissionsChain.contains(MusicUserPermissions.ALL_DISCUSSIONS_OWN) },
        ).toMutableSet()
    }
}
