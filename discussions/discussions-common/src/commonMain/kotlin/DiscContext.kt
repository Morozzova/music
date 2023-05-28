package ru.music.common

import kotlinx.datetime.Instant
import ru.music.common.models.*
import ru.music.common.stubs.DiscStubs
import repo.IDiscussionRepository

data class DiscContext(
    var command: DiscCommand = DiscCommand.NONE,
    var state: DiscState = DiscState.NONE,
    val errors: MutableList<DiscError> = mutableListOf(),
    var settings: DiscCorSettings = DiscCorSettings.NONE,

    var workMode: DiscWorkMode = DiscWorkMode.PROD,
    var stubCase: DiscStubs = DiscStubs.NONE,

    var discussionRepo: IDiscussionRepository = IDiscussionRepository.NONE,
    var discussionRepoRead: DiscDiscussion = DiscDiscussion(),
    var discussionRepoPrepare: DiscDiscussion = DiscDiscussion(),
    var discussionRepoDone: DiscDiscussion = DiscDiscussion(),
    var discussionsRepoDone: MutableList<DiscDiscussion> = mutableListOf(),

    var requestId: DiscRequestId = DiscRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var discussionRequest: DiscDiscussion = DiscDiscussion(),
    var multiDiscussionsRequest: DiscMulti = DiscMulti(),

    var discussionValidating: DiscDiscussion = DiscDiscussion(),
    var discussionValidatingMulti: DiscMulti = DiscMulti(),

    var discussionValidated: DiscDiscussion = DiscDiscussion(),
    var discussionValidatedMulti: DiscMulti = DiscMulti(),

    var discussionResponse: DiscDiscussion = DiscDiscussion(),
    var multiDiscussionsResponse: MutableList<DiscDiscussion> = mutableListOf(),
)
