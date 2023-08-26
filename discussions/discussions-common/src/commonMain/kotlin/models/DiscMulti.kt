package ru.music.common.models

import models.DiscMultiPermissions

data class DiscMulti(
    var id: DiscUserId? = null,
    var multiPermissions: MutableSet<DiscMultiPermissions> = mutableSetOf(),
)