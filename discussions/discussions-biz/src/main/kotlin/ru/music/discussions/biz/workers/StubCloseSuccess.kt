package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscId
import ru.music.common.models.DiscState
import ru.music.common.models.DiscStatus
import ru.music.common.models.DiscUserId
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker
import ru.music.discussions.stubs.DiscStub

fun ICorChainDsl<DiscContext>.stubCloseSuccess(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.SUCCESS && state == DiscState.RUNNING }
    handle {
        state = DiscState.FINISHING
        val stub = DiscStub.prepareResult {
            discussionRequest.id.takeIf { it != DiscId.NONE }?.also { this.id = it }
            discussionRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            discussionRequest.soundUrl.takeIf { it.isNotBlank() }?.also { this.soundUrl = it }
            discussionRequest.status.takeIf { it != DiscStatus.NONE }?.also { this.status = it }
            discussionRequest.answers.takeIf { it.isNotEmpty() }.also { this.answers = it ?: mutableListOf() }
            discussionRequest.ownerId.takeIf { it != DiscUserId.NONE }?.also { this.ownerId = it }
        }
        discussionResponse = stub
    }
}
