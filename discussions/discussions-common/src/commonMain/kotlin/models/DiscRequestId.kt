package ru.music.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class DiscRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = DiscRequestId("")
    }
}