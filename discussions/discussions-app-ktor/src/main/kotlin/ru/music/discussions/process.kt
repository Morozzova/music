package ru.music.discussions.ru.music.discussions

import ru.music.common.DiscContext
import ru.music.discussions.biz.DiscussionsProcessor

private val processor = DiscussionsProcessor()
suspend fun process(ctx: DiscContext) = processor.exec(ctx)