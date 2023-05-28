package ru.music.discussions.biz.repo

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoPrepareUsersDiscussions(title: String) = worker {
    this.title = title
    description = "Готовим данные для загрузки обсуждений пользователя"
    on { state == DiscState.RUNNING }
    handle {
        discussionRepoPrepare = discussionValidated.deepCopy()
        discussionRepoDone = discussionRepoRead.deepCopy()
    }
}
