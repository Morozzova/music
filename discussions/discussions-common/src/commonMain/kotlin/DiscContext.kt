package ru.music.common

import kotlinx.datetime.Instant
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs

data class DiscContext(
    var command: DiscCommand = DiscCommand.NONE,
    var state: DiscState = DiscState.NONE,
    val errors: MutableList<DiscError> = mutableListOf(),

    var workMode: DiscWorkMode = DiscWorkMode.PROD,
    var stubCase: DiscStubs = DiscStubs.NONE,

    var requestId: DiscRequestId = DiscRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var discussionRequest: DiscDiscussion = DiscDiscussion(),
    var multiDiscussionsRequest: DiscMulti = DiscMulti(),
    var discussionResponse: DiscDiscussion = DiscDiscussion(),
    var multiDiscussionsResponse: MutableList<DiscDiscussion> = mutableListOf(),
)
