package ru.music.discussions.biz.validation

import ru.music.common.DiscContext
import ru.music.common.helpers.errorValidation
import ru.music.common.helpers.fail
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.validateSoundUrlHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { discussionValidating.soundUrl.isNotEmpty() && !discussionValidating.soundUrl.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "soundUrl",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
