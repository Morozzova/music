package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.common.stubs.DiscStubs
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.stubValidationBadTitle(title: String) = worker {
    this.title = title
    on { stubCase == DiscStubs.BAD_TITLE && state == DiscState.RUNNING }
    handle {
        state = DiscState.FAILING
        this.errors.add(
            DiscError(
                group = "validation",
                code = "validation-title",
                field = "title",
                message = "Wrong title field"
            )
        )
    }
}