package auth

import io.ktor.client.request.*
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig

expect fun HttpRequestBuilder.addAuth(
    id: String = "user1",
    groups: List<String> = listOf("USER", "TEST"),
    config: KtorAuthConfig = KtorAuthConfig.TEST,
)