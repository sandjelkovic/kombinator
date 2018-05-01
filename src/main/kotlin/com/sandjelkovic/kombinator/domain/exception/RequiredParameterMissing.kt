package com.sandjelkovic.kombinator.domain.exception

class RequiredParameterMissing(parameterPath: String = "") : ValidationException(parameterPath)
