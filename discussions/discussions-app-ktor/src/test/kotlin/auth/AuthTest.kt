package auth

import helpers.testSettings
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.music.discussions.module
import ru.music.discussions.ru.music.discussions.base.KtorAuthConfig
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun invalidAudience() = testApplication {
        application {
            module(testSettings())
        }

        val response = client.post("/discussion/create") {
            addAuth(config = KtorAuthConfig.TEST.copy(audience = "invalid"))
        }
        assertEquals(401, response.status.value)
    }
}
