package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.helpers.fail
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == DiscState.RUNNING }
    handle {
        fail(
            DiscError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
