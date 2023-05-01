package ru.music.discussions.plugins

import io.ktor.server.application.*
import ru.music.common.DiscCorSettings
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.biz.DiscussionsProcessor

fun Application.initAppSettings(): DiscAppSettings = DiscAppSettings(
    appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
    corSettings = DiscCorSettings(
        loggerProvider = getLoggerProviderConf()
    ),
    processor = DiscussionsProcessor()
)
