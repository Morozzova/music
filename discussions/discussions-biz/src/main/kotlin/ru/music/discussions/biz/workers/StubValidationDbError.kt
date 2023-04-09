package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.DB_ERROR && state == DiscState.RUNNING }
    handle {
        state = DiscState.FAILING
        this.errors.add(
            DiscError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}