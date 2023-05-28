package ru.music.discussions.biz.permissions

import ru.music.auth.checkPermitted
import ru.music.auth.resolveRelationsTo
import ru.music.common.DiscContext
import ru.music.common.helpers.fail
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.chain
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == DiscState.RUNNING }
    worker("Вычисление отношения обсуждения к принципалу") {
        discussionRepoRead.principalRelations = discussionRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к обсуждению") {
        permitted = checkPermitted(command, discussionRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(DiscError(message = "User is not allowed to perform this operation"))
        }
    }
}

