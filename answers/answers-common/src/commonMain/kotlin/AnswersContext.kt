package ru.music.common

import kotlinx.datetime.Instant
import ru.music.common.models.*
import ru.music.common.stubs.AnswersStubs

data class AnswersContext(
    var command: AnswersCommand = AnswersCommand.NONE,
    var state: AnswersState = AnswersState.NONE,
    val errors: MutableList<AnswersError> = mutableListOf(),

    var workMode: AnswersWorkMode = AnswersWorkMode.PROD,
    var stubCase: AnswersStubs = AnswersStubs.NONE,

    var requestId: AnswersRequestId = AnswersRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var answersRequest: AnswersAnswer = AnswersAnswer(),
    var answersResponse: AnswersAnswer = AnswersAnswer(),
)
