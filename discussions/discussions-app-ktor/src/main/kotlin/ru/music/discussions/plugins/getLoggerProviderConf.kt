package ru.music.discussions.plugins

import io.ktor.server.application.*
import ru.music.discussions.MpLoggerProvider
import ru.music.discussions.logging.jvm.mpLoggerLogback
import ru.music.discussions.logging.kermit.mpLoggerKermit

fun Application.getLoggerProviderConf(): MpLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> MpLoggerProvider { mpLoggerKermit(it) }
        "logback", null -> MpLoggerProvider { mpLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp and logback")
}
