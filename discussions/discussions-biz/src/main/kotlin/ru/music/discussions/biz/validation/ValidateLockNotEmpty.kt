package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { discussionValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
