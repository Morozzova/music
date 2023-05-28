package ru.music.discussions.repo.postgresql

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/discussions",
    val user: String = "postgres",
    val password: String = "postgres-pass",
    val schema: String = "discussions",
    // Удалять таблицы при старте - нужно для тестирования
    val dropDatabase: Boolean = false,
)