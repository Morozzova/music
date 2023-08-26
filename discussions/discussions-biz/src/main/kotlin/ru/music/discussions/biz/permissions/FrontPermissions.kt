package ru.music.discussions.biz.permissions

import ru.music.auth.resolveFrontPermissions
import ru.music.auth.resolveRelationsTo
import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == DiscState.RUNNING }

    handle {
        discussionRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                discussionRepoDone.resolveRelationsTo(principal)
            )
        )

        for (disc in discussionsRepoDone) {
            disc.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    disc.resolveRelationsTo(principal)
                )
            )
        }
    }
}
