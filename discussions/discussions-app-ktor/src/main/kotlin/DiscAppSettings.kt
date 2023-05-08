package ru.music.discussions

import ru.music.common.DiscCorSettings
import ru.music.discussions.biz.DiscussionsProcessor

data class DiscAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: DiscCorSettings,
    val processor: DiscussionsProcessor = DiscussionsProcessor(settings = corSettings),
)
