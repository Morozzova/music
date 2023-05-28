package ru.music.discussions.biz.general

import permissions.MusicUserGroups
import repo.IDiscussionRepository
import ru.music.common.DiscContext
import ru.music.common.helpers.errorAdministration
import ru.music.common.helpers.fail
import ru.music.common.models.DiscWorkMode
import ru.music.discussions.cor.ICorChainDsl
import ru.music.discussions.cor.worker

fun ICorChainDsl<DiscContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        discussionRepo = when {
            workMode == DiscWorkMode.TEST -> settings.repoTest
            workMode == DiscWorkMode.STUB -> settings.repoStub
            principal.groups.contains(MusicUserGroups.TEST) -> settings.repoTest
            else -> settings.repoProd
        }
        if (workMode != DiscWorkMode.STUB && discussionRepo == IDiscussionRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}
