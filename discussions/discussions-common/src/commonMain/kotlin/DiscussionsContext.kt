package ru.music.common

import kotlinx.datetime.Instant
import ru.music.common.models.DiscussionsCommand
import ru.music.common.models.DiscussionsState

data class DiscussionsContext(
    var command: DiscussionsCommand = DiscussionsCommand.NONE,
    var state: DiscussionsState = DiscussionsState.NONE,
    val errors: MutableList<DiscussionsError> = mutableListOf(),

    var workMode: DiscussionsWorkMode = DiscussionsWorkMode.PROD,
    var stubCase: DiscussionsStubs = DiscussionsStubs.NONE,

    var requestId: DiscussionsRequestId = DiscussionsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    )
