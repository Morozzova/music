package ru.music.discussions.biz.groups

import ru.music.common.DiscContext
import ru.music.common.models.DiscCommand
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.chain

fun ICorChainDsl<DiscContext>.operation(title: String, command: DiscCommand, block: ICorChainDsl<DiscContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == DiscState.RUNNING }
}
