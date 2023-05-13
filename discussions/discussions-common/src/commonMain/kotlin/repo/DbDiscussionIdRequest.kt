package repo

import ru.music.common.models.DiscDiscussion
import ru.music.common.models.DiscId
import ru.music.common.models.DiscLock

data class DbDiscussionIdRequest(
    val id: DiscId,
    val lock: DiscLock = DiscLock.NONE,
) {
    constructor(discussion: DiscDiscussion): this(discussion.id, discussion.lock)
}
