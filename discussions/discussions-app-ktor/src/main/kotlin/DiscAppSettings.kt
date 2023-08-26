package ru.music.discussions

import ru.music.common.DiscCorSettings
import ru.music.discussions.biz.DiscussionsProcessor
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig

data class DiscAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: DiscCorSettings,
    val processor: DiscussionsProcessor = DiscussionsProcessor(settings = corSettings),
    val auth: KtorAuthConfig = KtorAuthConfig.NONE,
)
