package ru.music.discussions.biz

import ru.music.common.DiscContext
import ru.music.common.models.DiscCommand
import ru.music.discussions.biz.groups.operation
import ru.music.discussions.biz.groups.stubs
import ru.music.discussions.biz.workers.*
import ru.music.discussions.cor.rootChain

class DiscussionsProcessor {
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
            }
            operation("Получить обсуждение", DiscCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
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
            }
            operation("Удалить обсуждение", DiscCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить все обсуждения", DiscCommand.ALL_DISCUSSIONS) {
                stubs("Обработка стабов") {
                    stubAllDiscussionsSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить обсуждения пользователя", DiscCommand.USERS_DISCUSSIONS) {
                stubs("Обработка стабов") {
                    stubUsersDiscussionsSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
        }.build()
    }
}