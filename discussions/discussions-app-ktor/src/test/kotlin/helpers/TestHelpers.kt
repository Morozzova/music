package helpers

import repo.IDiscussionRepository
import ru.music.common.DiscCorSettings
import ru.music.discussions.DiscAppSettings
import ru.music.discussions.repo.inmemory.DiscussionsRepoInMemory
import ru.music.discussions.repository.inmemory.DiscussionsRepoStub
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig

fun testSettings(repo: IDiscussionRepository? = null) = DiscAppSettings(
    corSettings = DiscCorSettings(
        repoStub = DiscussionsRepoStub(),
        repoTest = repo ?: DiscussionsRepoInMemory(),
        repoProd = repo ?: DiscussionsRepoInMemory(),
    ),
    auth = KtorAuthConfig.TEST
)
