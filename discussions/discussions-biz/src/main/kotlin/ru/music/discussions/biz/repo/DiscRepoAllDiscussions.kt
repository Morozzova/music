package ru.music.discussions.biz.repo

import repo.DbDiscussionIdRequest
import repo.DbDiscussionRequest
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.common.repo.DbDiscussionUsersIdRequest
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoAllDiscussions(title: String) = worker {
    this.title = title

    description = "Загрузка всех обсуждений"
    on { state == DiscState.RUNNING }
    handle {
        val dbResponse = discussionRepo.allDiscussions(DbDiscussionUsersIdRequest(null))

        val resultDiscussions = dbResponse.data
        when {
            !resultDiscussions.isNullOrEmpty() -> discussionsRepoDone = resultDiscussions.toMutableList()
            dbResponse.isSuccess -> return@handle
            else -> {
                state = DiscState.FAILING
                errors.addAll(dbResponse.errors)
            }
        }
    }
}
