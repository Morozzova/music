package ru.music.discussions.biz.validation

import DiscussionsRepoStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.music.common.DiscCorSettings
import ru.music.common.models.DiscCommand
import ru.music.discussions.biz.DiscussionsProcessor
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = DiscCommand.CREATE
    private val settings by lazy {
        DiscCorSettings(
            repoTest = DiscussionsRepoStub()
        )
    }
    private val processor by lazy { DiscussionsProcessor(settings) }

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctSoundUrl() = validationSoundUrlCorrect(command, processor)
    @Test fun trimSoundUrl() = validationSoundUrlTrim(command, processor)
    @Test fun emptySoundUrl() = validationSoundUrlEmpty(command, processor)
    @Test fun badSymbolsSoundUrl() = validationSoundUrlSymbols(command, processor)

}

