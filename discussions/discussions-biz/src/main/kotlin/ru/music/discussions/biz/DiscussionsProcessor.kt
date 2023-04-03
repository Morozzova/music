package ru.music.discussions.biz

import ru.music.common.DiscContext
import ru.music.discussions.stubs.DiscStub

class DiscussionsProcessor {
    suspend fun exec(ctx: DiscContext) {
        ctx.discussionResponse = DiscStub.get()
    }
}
