package ru.music.discussions

import ru.music.common.DiscCorSettings

data class DiscAppSettings(
    val appUrls: List<String>,
    val corSettings: DiscCorSettings,
)
