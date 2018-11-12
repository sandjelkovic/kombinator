package com.sandjelkovic.kombinator.domain.exception

/**
 * @author sandjelkovic
 * @date 1.5.18.
 */
sealed class DomainValidationException(parameterPath: String = "") : RuntimeException(parameterPath) {
    class RequiredParameterMissing(parameterPath: String = "") : DomainValidationException(parameterPath)
    class ReferenceNotFound(parameterPath: String = "") : DomainValidationException(parameterPath)
}

