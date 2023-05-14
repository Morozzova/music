package ru.music.discussions.ru.music.discussions.configs

import io.ktor.server.config.*

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/discussions",
    val user: String = "postgres",
    val password: String = "postgres-pass",
    val schema: String = "discussions",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "discussions.repository.psql"
    }
}
