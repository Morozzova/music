package exceptions

class UnknownRequestClass(cl: Class<*>) : RuntimeException("Class $cl cannot be mapped to DiscContext")