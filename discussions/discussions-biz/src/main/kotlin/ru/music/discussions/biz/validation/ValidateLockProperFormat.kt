package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.common.models.DiscLock
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { discussionValidating.lock != DiscLock.NONE && !discussionValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = discussionValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = "value $encodedId must contain only"
            )
        )
    }
}
