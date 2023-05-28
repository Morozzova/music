package ru.music.discussions.ru.music.discussions.plugins

import io.ktor.server.application.*
import repo.IDiscussionRepository
import ru.music.discussions.repo.inmemory.DiscussionsRepoInMemory
import ru.music.discussions.repo.postgresql.RepoDiscussionsSQL
import ru.music.discussions.repo.postgresql.SqlProperties
import ru.music.discussions.ru.music.discussions.configs.PostgresConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class DiscussionDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.getDatabaseConf(type: DiscussionDbType): IDiscussionRepository {
    val dbSettingPath = "discussions.repository.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'postgres'"
        )
    }
}

private fun Application.initPostgres(): IDiscussionRepository {
    val config = PostgresConfig(environment.config)
    return RepoDiscussionsSQL(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}

private fun Application.initInMemory(): IDiscussionRepository {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return DiscussionsRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}
