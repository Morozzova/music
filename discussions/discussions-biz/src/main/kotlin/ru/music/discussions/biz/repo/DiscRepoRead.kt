package ru.music.discussions.biz.repo

import repo.DbDiscussionIdRequest
import repo.DbDiscussionRequest
import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == DiscState.RUNNING }
    handle {
        val request = DbDiscussionIdRequest(discussionValidated)
        val result = discussionRepo.readDiscussion(request)
        val resultDisc = result.data
        if (result.isSuccess && resultDisc != null) {
            discussionRepoRead = resultDisc
        } else {
            state = DiscState.FAILING
            errors.addAll(result.errors)
        }
    }
}
