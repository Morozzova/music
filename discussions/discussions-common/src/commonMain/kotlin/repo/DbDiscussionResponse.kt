package repo

import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscError

data class DbDiscussionResponse(
    override val data: DiscDiscussion?,
    override val isSuccess: Boolean,
    override val errors: List<DiscError> = emptyList()
): IDbResponse<DiscDiscussion> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbDiscussionResponse(null, true)
        fun success(result: DiscDiscussion) = DbDiscussionResponse(result, true)
        fun error(errors: List<DiscError>) = DbDiscussionResponse(null, false, errors)
        fun error(error: DiscError) = DbDiscussionResponse(null, false, listOf(error))
    }
}
