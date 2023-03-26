package ru.music.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class DiscAnswer(private val answer: String) {
    fun asString() = answer

    companion object {
        val NONE = DiscAnswer("")
    }
}