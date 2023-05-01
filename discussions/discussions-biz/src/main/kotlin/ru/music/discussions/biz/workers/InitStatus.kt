package ru.music.discussions.biz.workers

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == DiscState.NONE }
    handle { state = DiscState.RUNNING }
}
