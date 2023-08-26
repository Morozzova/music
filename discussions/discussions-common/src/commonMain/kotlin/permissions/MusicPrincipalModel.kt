package permissions

import ru.music.common.models.DiscUserId

data class MusicPrincipalModel(
    val id: DiscUserId = DiscUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<MusicUserGroups> = emptySet()
) {
    companion object {
        val NONE = MusicPrincipalModel()
    }
}
