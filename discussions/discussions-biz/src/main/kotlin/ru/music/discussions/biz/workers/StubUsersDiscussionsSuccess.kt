package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.common.models.DiscUserId
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker
import ru.music.discussions.stubs.DiscStub

fun ICorChainDsl<DiscContext>.stubUsersDiscussionsSuccess(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.SUCCESS && state == DiscState.RUNNING }
    handle {
        state = DiscState.FINISHING
        discussionResponse = DiscStub.prepareResult {
            discussionRequest.ownerId.takeIf { it != DiscUserId.NONE }?.also { this.ownerId = it }
        }
        multiDiscussionsResponse.addAll(DiscStub.prepareDiscussionsList(discussionResponse.ownerId))
    }
}