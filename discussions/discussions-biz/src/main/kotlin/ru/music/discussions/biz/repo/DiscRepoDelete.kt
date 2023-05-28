package ru.music.discussions.biz.repo

import repo.DbDiscussionIdRequest
import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление обсуждения из БД по ID"
    on { state == DiscState.RUNNING }
    handle {
        val request = DbDiscussionIdRequest(discussionRepoPrepare)
        val result = discussionRepo.deleteDiscussion(request)
        if (!result.isSuccess) {
            state = DiscState.FAILING
            errors.addAll(result.errors)
        }
        discussionRepoDone = discussionRepoRead
    }
}
