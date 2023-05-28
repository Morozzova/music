package repo

import ru.music.common.helpers.errorRepoConcurrency
import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscError
import ru.music.common.models.DiscLock
import ru.music.common.helpers.errorEmptyId as discErrorEmptyId
import ru.music.common.helpers.errorNotFound as discErrorNotFound

data class DbDiscussionResponse(
    override val data: DiscDiscussion?,
    override val isSuccess: Boolean,
    override val errors: List<DiscError> = emptyList()
): IDbResponse<DiscDiscussion> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbDiscussionResponse(null, true)
        fun success(result: DiscDiscussion) = DbDiscussionResponse(result, true)
        fun error(errors: List<DiscError>, data: DiscDiscussion? = null) = DbDiscussionResponse(data, false, errors)
        fun error(error: DiscError, data: DiscDiscussion? = null) = DbDiscussionResponse(data, false, listOf(error))

        val errorEmptyId = error(discErrorEmptyId)

        fun errorConcurrent(lock: DiscLock, disc: DiscDiscussion?) = error(
            errorRepoConcurrency(lock, disc?.lock?.let { DiscLock(it.asString()) }),
            disc
        )

        val errorNotFound = error(discErrorNotFound)
    }
}
