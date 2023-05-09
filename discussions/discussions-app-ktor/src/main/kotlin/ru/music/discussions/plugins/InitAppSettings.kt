package ru.music.discussions.plugins

import DiscussionsRepoStub
import io.ktor.server.application.*
import ru.music.common.DiscCorSettings
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repo.inmemory.DiscussionsRepoInMemory

fun Application.initAppSettings(): DiscAppSettings {

    val corSettings = DiscCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = DiscussionsRepoInMemory(),
        repoProd = DiscussionsRepoInMemory(),
        repoStub = DiscussionsRepoStub(),
    )

    return DiscAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DiscussionsProcessor(corSettings)
    )
}