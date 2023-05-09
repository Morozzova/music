package ru.music.discussions.biz.repo

import repo.DbDiscussionIdRequest
import repo.DbDiscussionsResponse
import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.common.models.DiscUserId
import ru.music.common.repo.DbDiscussionUsersIdRequest
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoUsersDiscussions(title: String) = worker {
    this.title = title

    description = "Загрузка обсуждений пользователя"

    on { state == DiscState.RUNNING }
    handle {
        val request = DbDiscussionUsersIdRequest(multiDiscussionsRequest.id)
        val dbResponse = discussionRepo.usersDiscussions(request)
    println(dbResponse.data)
        val resultDiscussions = dbResponse.data
        when {
            !resultDiscussions.isNullOrEmpty() -> discussionsRepoDone = resultDiscussions.toMutableList()
            dbResponse.isSuccess -> return@handle
            else -> {
                state = DiscState.FAILING
                errors.addAll(dbResponse.errors)
            }
        }
        discussionsRepoDone = dbResponse.data?.toMutableList() ?: mutableListOf()
    }
}
