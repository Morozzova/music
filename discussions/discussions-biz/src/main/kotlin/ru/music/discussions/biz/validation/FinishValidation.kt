package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.finishDiscValidation(title: String) = worker {
    this.title = title
    on { state == DiscState.RUNNING }
    handle {
        discussionValidated = discussionValidating
    }
}
