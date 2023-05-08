package ru.music.discussions.biz

import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.DiscCommand
import ru.music.common.models.DiscId
import ru.music.common.models.DiscUserId
import ru.music.discussions.biz.groups.operation
import ru.music.discussions.biz.groups.stubs
import ru.music.discussions.biz.validation.*
import ru.music.discussions.biz.workers.*
import ru.music.discussions.cor.rootChain
import ru.music.discussions.cor.worker

class DiscussionsProcessor(private val settings: DiscCorSettings = DiscCorSettings()) {
    suspend fun exec(ctx: DiscContext) = BusinessChain.exec(ctx)
    companion object {
        private val BusinessChain = rootChain<DiscContext> {
            initStatus("Инициализация статуса")

            operation("Создание обсуждения", DiscCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadFile("Имитация ошибки загруженного файла")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidating = discussionRequest.deepCopy() }
                    worker("Очистка id") { discussionValidating.id = DiscId.NONE }
                    worker("Очистка заголовка") { discussionValidating.title = discussionValidating.title.trim() }
                    worker("Очистка url звукозаписи") { discussionValidating.soundUrl = discussionValidating.soundUrl.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateSoundUrlNotEmpty("Проверка, что url звукозаписи не пуст")
                    validateSoundUrlHasContent("Проверка символов")

                    finishDiscValidation("Завершение проверок")
                }
            }
            operation("Получить обсуждение", DiscCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidating = discussionRequest.deepCopy() }
                    worker("Очистка id") { discussionValidating.id = DiscId(discussionValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishDiscValidation("Успешное завершение валидации")
                }
            }
            operation("Изменить обсуждение", DiscCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadFile("Имитация ошибки загруженного файла")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidating = discussionRequest.deepCopy() }
                    worker("Очистка id") { discussionValidating.id = DiscId(discussionValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { discussionValidating.title = discussionValidating.title.trim() }
                    worker("Очистка url звукозаписи") { discussionValidating.soundUrl = discussionValidating.soundUrl.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateSoundUrlNotEmpty("Проверка, что url звукозаписи не пуст")
                    validateSoundUrlHasContent("Проверка символов")

                    finishDiscValidation("Успешное завершение валидации")
                }
            }
            operation("Закрыть обсуждение", DiscCommand.CLOSE) {
                stubs("Обработка стабов") {
                    stubCloseSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadFile("Имитация ошибки загруженного файла")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidating = discussionRequest.deepCopy() }
                    worker("Очистка id") { discussionValidating.id = DiscId(discussionValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { discussionValidating.title = discussionValidating.title.trim() }
                    worker("Очистка url звукозаписи") { discussionValidating.soundUrl = discussionValidating.soundUrl.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateSoundUrlNotEmpty("Проверка, что url звукозаписи не пуст")
                    validateSoundUrlHasContent("Проверка символов")

                    finishDiscValidation("Успешное завершение валидации")
                }
            }
            operation("Удалить обсуждение", DiscCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidating = discussionRequest.deepCopy() }
                    worker("Очистка id") { discussionValidating.id = DiscId(discussionValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishDiscValidation("Успешное завершение валидации")
                }
            }
            operation("Получить все обсуждения", DiscCommand.ALL_DISCUSSIONS) {
                stubs("Обработка стабов") {
                    stubAllDiscussionsSuccess("Имитация успешной обработки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить обсуждения пользователя", DiscCommand.USERS_DISCUSSIONS) {
                stubs("Обработка стабов") {
                    stubUsersDiscussionsSuccess("Имитация успешной обработки")
                    stubValidationBadUsersId("Имитация ошибки валидации user id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в discussionValidating") { discussionValidatingMulti = multiDiscussionsRequest.copy() }
                    worker("Очистка id пользователя") { discussionValidatingMulti.id = DiscUserId(discussionValidatingMulti.id?.asString()?.trim() ?: "") }
                    validateUsersIdNotEmpty("Проверка на непустой id пользователя")
                    validateUsersIdProperFormat("Проверка формата id пользователя")

                    finishDiscValidation("Успешное завершение валидации")
                }
            }
        }.build()
    }
}