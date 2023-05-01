package repo

import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscError

data class DbDiscussionsResponse(
    override val data: List<DiscDiscussion>?,
    override val isSuccess: Boolean,
    override val errors: List<DiscError> = emptyList(),
): IDbResponse<List<DiscDiscussion>> {

    companion object {
        val MOCK_SUCCESS_EMPTY = DbDiscussionsResponse(emptyList(), true)
        fun success(result: List<DiscDiscussion>) = DbDiscussionsResponse(result, true)
        fun error(errors: List<DiscError>) = DbDiscussionsResponse(null, false, errors)
        fun error(error: DiscError) = DbDiscussionsResponse(null, false, listOf(error))
    }
}
