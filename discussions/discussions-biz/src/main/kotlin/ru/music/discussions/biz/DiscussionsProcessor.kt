package ru.music.discussions.biz

import ru.music.common.DiscContext
import ru.music.common.DiscCorSettings
import ru.music.common.models.*
import ru.music.discussions.biz.general.initRepo
import ru.music.discussions.biz.general.operation
import ru.music.discussions.biz.general.prepareResult
import ru.music.discussions.biz.general.stubs
import ru.music.discussions.biz.permissions.accessValidation
import ru.music.discussions.biz.permissions.accessValidationMulti
import ru.music.discussions.biz.permissions.chainPermissions
import ru.music.discussions.biz.permissions.frontPermissions
import ru.music.discussions.biz.repo.*
import ru.music.discussions.biz.validation.*
import ru.music.discussions.biz.workers.*
import ru.music.discussions.cor.chain
import ru.music.discussions.cor.rootChain
import ru.music.discussions.cor.worker

class DiscussionsProcessor(private val settings: DiscCorSettings = DiscCorSettings()) {
    suspend fun exec(ctx: DiscContext) = BusinessChain.exec(ctx.apply { settings =  this@DiscussionsProcessor.settings})
    companion object {
        private val BusinessChain = rootChain<DiscContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

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

                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    accessValidation("Вычисление прав доступа")
                    repoCreate("Создание обсуждения в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение обсуждения из БД")
                    accessValidation("Вычисление прав доступа")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == DiscState.RUNNING }
                        handle { discussionRepoDone = discussionRepoRead }
                    }
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { discussionValidating.lock = DiscLock(discussionValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { discussionValidating.title = discussionValidating.title.trim() }
                    worker("Очистка url звукозаписи") { discussionValidating.soundUrl = discussionValidating.soundUrl.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateSoundUrlNotEmpty("Проверка, что url звукозаписи не пуст")
                    validateSoundUrlHasContent("Проверка символов")

                    finishDiscValidation("Успешное завершение валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение обсуждения из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление обсуждения в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { discussionValidating.lock = DiscLock(discussionValidating.lock.asString().trim()) }
                    worker("Очистка заголовка") { discussionValidating.title = discussionValidating.title.trim() }
                    worker("Очистка url звукозаписи") { discussionValidating.soundUrl = discussionValidating.soundUrl.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateSoundUrlNotEmpty("Проверка, что url звукозаписи не пуст")
                    validateSoundUrlHasContent("Проверка символов")

                    finishDiscValidation("Успешное завершение валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение обсуждения из БД")
                    accessValidation("Вычисление прав доступа")
                    repoPrepareClose("Подготовка объекта для обновления")
                    repoClose("Закрытие обсуждения в БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                    worker("Очистка lock") { discussionValidating.lock = DiscLock(discussionValidating.lock.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateLockNotEmpty("Проверка на непустой lock")
                    validateLockProperFormat("Проверка формата lock")

                    finishDiscValidation("Успешное завершение валидации")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение обсуждения из БД")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление обсуждения из БД")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
            operation("Получить все обсуждения", DiscCommand.ALL_DISCUSSIONS) {
                stubs("Обработка стабов") {
                    stubAllDiscussionsSuccess("Имитация успешной обработки")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика поиска в БД"
                    accessValidationMulti("Вычисление прав доступа")
                    repoPrepareAllDiscussions("Подготовка загрузки всех обсуждений")
                    repoAllDiscussions("Чтение всех обсуждений")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
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
                chainPermissions("Вычисление разрешений для пользователя")
                chain {
                    title = "Логика поиска в БД"
                    accessValidationMulti("Вычисление прав доступа")
                    repoPrepareUsersDiscussions("Подготовка загрузки обсуждений пользователя")
                    repoUsersDiscussions("Чтение обсуждений пользователя")
                }
                frontPermissions("Вычисление пользовательских разрешений для фронтенда")
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}