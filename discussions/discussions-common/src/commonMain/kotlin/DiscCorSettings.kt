package ru.music.common

import ru.music.discussions.MpLoggerProvider

data class DiscCorSettings(
    val loggerProvider: MpLoggerProvider = MpLoggerProvider(),
) {
    companion object {
        val NONE = DiscCorSettings()
    }
}
