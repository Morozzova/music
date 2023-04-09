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

fun ICorChainDsl<DiscContext>.stubUpdateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.SUCCESS && state == DiscState.RUNNING }
    handle {
        state = DiscState.FINISHING
        val stub = DiscStub.prepareResult {
            discussionRequest.id.takeIf { it != DiscId.NONE }?.also { this.id = it }
            discussionRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
            discussionRequest.soundUrl.takeIf { it.isNotBlank() }?.also { this.soundUrl = it }
            discussionRequest.status.takeIf { it != DiscStatus.NONE }?.name
            discussionRequest.answers.map { it.asString() }.takeIf { it.isNotEmpty() }
            discussionRequest.ownerId.takeIf { it != DiscUserId.NONE }?.asString()
        }
        discussionResponse = stub
    }
}
