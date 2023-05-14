package ru.music.discussions.repo.postgresql

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.PostgreSQLContainer
import ru.music.common.models.DiscDiscussion
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "postgres-pass"
    private const val SCHEMA = "discussions"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initObjects: Collection<DiscDiscussion> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoDiscussionsSQL {
        return RepoDiscussionsSQL(
            SqlProperties(url, USER, PASS, SCHEMA, dropDatabase = true),
            initObjects, randomUuid = randomUuid)
    }
}
