package ru.music.discussions.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.music.common.models.DiscCommand
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationUsersDiscussionsTest {

    private val command = DiscCommand.USERS_DISCUSSIONS
    private val processor by lazy { DiscussionsProcessor() }

    @Test fun correctUsersId() = validationUsersIdCorrect(command, processor)
    @Test fun trimUsersId() = validationUsersIdTrim(command, processor)
    @Test fun emptyUsersId() = validationUsersIdEmpty(command, processor)
    @Test fun badFormatUsersId() = validationUsersIdFormat(command, processor)

}

