package ru.music.discussions.biz.validation

import DiscussionsRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.music.common.DiscCorSettings
import ru.music.common.models.DiscCommand
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationUsersDiscussionsTest {

    private val command = DiscCommand.USERS_DISCUSSIONS
    private val settings by lazy {
        DiscCorSettings(
            repoTest = DiscussionsRepoStub()
        )
    }
    private val processor by lazy { DiscussionsProcessor(settings) }

    @Test fun correctUsersId() = validationUsersIdCorrect(command, processor)
    @Test fun trimUsersId() = validationUsersIdTrim(command, processor)
    @Test fun emptyUsersId() = validationUsersIdEmpty(command, processor)
    @Test fun badFormatUsersId() = validationUsersIdFormat(command, processor)

}

