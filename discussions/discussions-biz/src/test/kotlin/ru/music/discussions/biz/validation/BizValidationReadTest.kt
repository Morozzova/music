package ru.music.discussions.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.music.common.DiscCorSettings
import ru.music.common.models.DiscCommand
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repository.inmemory.DiscussionsRepoStub
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = DiscCommand.READ
    private val settings by lazy {
        DiscCorSettings(
            repoTest = DiscussionsRepoStub()
        )
    }
    private val processor by lazy { DiscussionsProcessor(settings) }

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}

