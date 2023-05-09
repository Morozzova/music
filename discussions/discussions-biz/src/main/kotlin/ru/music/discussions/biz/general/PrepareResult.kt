package ru.music.discussions.biz.general

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.common.models.DiscWorkMode
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != DiscWorkMode.STUB }
    handle {
        discussionResponse = discussionRepoDone
        multiDiscussionsResponse = discussionsRepoDone
        state = when (val st = state) {
            DiscState.RUNNING -> DiscState.FINISHING
            else -> st
        }
    }
}
