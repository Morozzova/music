package ru.music.common.helpers

import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState

fun DiscContext.addError(vararg error: DiscError) = errors.addAll(error)

fun DiscContext.fail(error: DiscError) {
    addError(error)
    state = DiscState.FAILING
}