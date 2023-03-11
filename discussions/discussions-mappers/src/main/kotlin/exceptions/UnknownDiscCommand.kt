package exceptions

import ru.music.common.models.DiscCommand

class UnknownDiscCommand(command: DiscCommand) : Exception("Wrong command $command at mapping toTransport stage")