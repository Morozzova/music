package ru.music.discussions.biz.repo

import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoPrepareAllDiscussions(title: String) = worker {
    this.title = title
    description = "Готовим данные для загрузки всех обсуждений"
    on { state == DiscState.RUNNING }
    handle {
        discussionRepoPrepare = discussionRepoRead.deepCopy()
        discussionRepoDone = discussionRepoRead.deepCopy()
    }
}
