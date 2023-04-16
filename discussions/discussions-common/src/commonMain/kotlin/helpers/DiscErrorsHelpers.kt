package ru.music.common.helpers

import ru.music.common.DiscContext
import ru.music.common.models.DiscError
import ru.music.common.models.DiscState

fun Throwable.asDiscError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = DiscError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
fun DiscContext.addError(vararg error: DiscError) = errors.addAll(error)

fun DiscContext.fail(error: DiscError) {
    addError(error)
    state = DiscState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: DiscError.Level = DiscError.Level.ERROR,
) = DiscError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)