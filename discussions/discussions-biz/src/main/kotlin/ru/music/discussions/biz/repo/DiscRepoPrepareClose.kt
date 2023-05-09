package ru.music.discussions.biz.repo

import ru.music.common.DiscContext
import ru.music.common.models.DiscState
import ru.music.common.models.DiscStatus
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.repoPrepareClose(title: String) = worker {
    this.title = title

    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == DiscState.RUNNING }
    handle {
        discussionRepoPrepare = discussionRepoRead.deepCopy().apply {
            this.title = discussionValidated.title
            soundUrl = discussionValidated.soundUrl
            status = DiscStatus.CLOSED
            answers = discussionValidated.answers
        }
    }
}
