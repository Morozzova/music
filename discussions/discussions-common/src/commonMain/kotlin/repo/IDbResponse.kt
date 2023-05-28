package repo

import ru.music.common.models.DiscError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<DiscError>
}
