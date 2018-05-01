package com.sandjelkovic.kombinator.domain.exception

/**
 * @author sandjelkovic
 * @date 1.5.18.
 */
open class ValidationException(parameterPath: String = "") : RuntimeException(parameterPath)
