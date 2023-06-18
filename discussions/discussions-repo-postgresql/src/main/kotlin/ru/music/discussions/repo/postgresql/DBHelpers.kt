package ru.music.discussions.repo.postgresql

import ru.music.common.models.DiscAnswer


/** Здесь, по-хорошему, нужно делать отдельный CRUD по добавлению ответов в обсуждение,
 * но, в рамках данной работы я реализую только один CRUD. Для MVP такого решения будет достаточно. */

fun String.toDBAnswers(): MutableList<DiscAnswer> =
    this.trim().splitToSequence('~').filter { it.isNotEmpty() }.map { DiscAnswer(it) }.toMutableList()

fun MutableList<DiscAnswer>.toDBString(): String = this.joinToString(separator = "~") { it.asString() }