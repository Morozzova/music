package exceptions

import ru.music.common.models.AnswersCommand

class UnknownAnswersCommand(command: AnswersCommand) : Throwable("Wrong command $command at mapping toTransport stage")