package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateSoundUrlNotEmpty(title: String) = worker {
    this.title = title
    on { discussionValidating.soundUrl.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "soundUrl",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
