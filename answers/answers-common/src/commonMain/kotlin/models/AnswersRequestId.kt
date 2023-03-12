package ru.music.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class AnswersRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AnswersRequestId("")
    }
}