package ru.music.common

import kotlinx.datetime.Instant
import ru.music.common.models.*
import ru.music.common.stubs.UsersStubs

data class UsersContext(
    var command: UsersCommand = UsersCommand.NONE,
    var state: UsersState = UsersState.NONE,
    val errors: MutableList<UsersError> = mutableListOf(),

    var workMode: UsersWorkMode = UsersWorkMode.PROD,
    var stubCase: UsersStubs = UsersStubs.NONE,

    var requestId: UsersRequestId = UsersRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var usersRequest: UsersUser = UsersUser(),
    var usersResponse: UsersUser = UsersUser(),

    )
