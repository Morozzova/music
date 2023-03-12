package ru.music.common.models

data class AnswersAnswer(
    var id: AnswersId = AnswersId.NONE,
    var discId: AnswersDiscId = AnswersDiscId.NONE,
    var ownerId: AnswersUserId = AnswersUserId.NONE,
    var text: String = "",
    var isRight: Boolean = false,
    var permissionsClient: MutableList<AnswersPermissionsClient> = mutableListOf()
)
