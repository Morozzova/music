package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker
import ru.music.discussions.stubs.DiscStub

fun ICorChainDsl<DiscContext>.stubDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.SUCCESS && state == DiscState.RUNNING }
    handle {
        state = DiscState.FINISHING
        val stub = DiscStub.prepareResult {
            discussionRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        discussionResponse = stub
    }
}
