package exceptions

import ru.music.common.models.DiscLock

class RepoConcurrencyException(expectedLock: DiscLock, actualLock: DiscLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
