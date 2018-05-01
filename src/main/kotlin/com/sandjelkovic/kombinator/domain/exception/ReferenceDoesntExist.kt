package com.sandjelkovic.kombinator.domain.exception

class ReferenceDoesntExist(parameterPath: String = "") : ValidationException(parameterPath)
