package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateUsersIdNotEmpty(title: String) = worker {
    this.title = title
    on { (discussionValidatingMulti.id?.asString() ?: "").isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "ownerId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
