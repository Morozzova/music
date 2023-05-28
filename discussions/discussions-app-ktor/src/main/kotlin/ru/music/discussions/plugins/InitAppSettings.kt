package ru.music.discussions.plugins

import io.ktor.server.application.*
import ru.music.common.DiscCorSettings
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.repository.inmemory.DiscussionsRepoStub
import ru.music.discussions.ru.music.discussions.plugins.DiscussionDbType
import ru.music.discussions.ru.music.discussions.plugins.getDatabaseConf

fun Application.initAppSettings(): DiscAppSettings {

    val corSettings = DiscCorSettings(
        loggerProvider = getLoggerProviderConf(),
        repoTest = getDatabaseConf(DiscussionDbType.TEST),
        repoProd = getDatabaseConf(DiscussionDbType.PROD),
        repoStub = DiscussionsRepoStub(),
    )

    return DiscAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DiscussionsProcessor(corSettings)
    )
}