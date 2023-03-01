package ru.music.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class UsersRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = UsersRequestId("")
    }
}