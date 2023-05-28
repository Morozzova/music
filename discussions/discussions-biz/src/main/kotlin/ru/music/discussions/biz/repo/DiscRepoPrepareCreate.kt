package ru.music.discussions.biz.repo

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == DiscState.RUNNING }
    handle {
        discussionRepoRead = discussionValidated.deepCopy()
        discussionRepoRead.ownerId = principal.id
        discussionRepoPrepare = discussionRepoRead
    }
}
