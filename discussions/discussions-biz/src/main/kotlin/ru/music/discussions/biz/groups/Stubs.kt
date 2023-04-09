package ru.music.discussions.biz.groups

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.common.models.DiscWorkMode
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.chain

fun ICorChainDsl<DiscContext>.stubs(title: String, block: ICorChainDsl<DiscContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == DiscWorkMode.STUB && state == DiscState.RUNNING }
}
