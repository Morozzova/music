package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.chain

fun ICorChainDsl<DiscContext>.validation(block: ICorChainDsl<DiscContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == DiscState.RUNNING }
}
