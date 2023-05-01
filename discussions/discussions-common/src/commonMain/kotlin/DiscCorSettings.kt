package ru.music.common

import repo.IDiscussionRepository
import ru.music.discussions.MpLoggerProvider

data class DiscCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
    val repoStub: IDiscussionRepository = IDiscussionRepository.NONE,
    val repoTest: IDiscussionRepository = IDiscussionRepository.NONE,
    val repoProd: IDiscussionRepository = IDiscussionRepository.NONE
) {
    companion object {
        val NONE = DiscCorSettings()
    }
}
