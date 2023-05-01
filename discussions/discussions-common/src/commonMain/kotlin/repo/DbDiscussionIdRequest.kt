package repo

import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId

data class DbDiscussionIdRequest(
    val id: DiscId,
) {
    constructor(discussion: DiscDiscussion): this(discussion.id)
}
