package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.common.models.DiscId
import ru.music.common.models.DiscUserId
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateUsersIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { discussionValidatingMulti.id != DiscUserId.NONE && ! (discussionValidatingMulti.id?.asString() ?: "").matches(regExp) }
    handle {
        val encodedId = (discussionValidatingMulti.id?.asString() ?: "")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
            field = "ownerId",
            violationCode = "badFormat",
            description = "value $encodedId must contain only letters and numbers"
        )
        )
    }
}
