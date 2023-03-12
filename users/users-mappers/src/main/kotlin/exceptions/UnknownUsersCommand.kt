package exceptions

import ru.music.common.models.UsersCommand

class UnknownUsersCommand(command: UsersCommand) : Throwable("Wrong command $command at mapping toTransport stage")