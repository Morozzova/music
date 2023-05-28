package ru.music.discussions.biz.repo

import repo.DbDiscussionRequest
import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление обсуждения в БД"
    on { state == DiscState.RUNNING }
    handle {
        val request = DbDiscussionRequest(discussionRepoPrepare)
        val result = discussionRepo.createDiscussion(request)
        val resultDisc = result.data
        if (result.isSuccess && resultDisc != null) {
            discussionRepoDone = resultDisc
        } else {
            state = DiscState.FAILING
            errors.addAll(result.errors)
        }
    }
}
