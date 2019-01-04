package com.sandjelkovic.kombinator.web

import arrow.core.Left
import arrow.core.Right
import arrow.core.Try
import arrow.core.filterOrElse
import arrow.data.Validated
import arrow.instances.either.monad.binding
import java.util.*

/**
 * @author sandjelkovic
 * @date 15.4.18.
 */

class UUIDValidator {
    fun validate(target: String): Validated<ValidationException, UUID> = Validated.fromEither(
        binding {
            Right(target).filterOrElse({ it.isNotEmpty() }) { EmptyParameterException }.bind()
            Right(target).filterOrElse({ it.isNotBlank() }) { EmptyParameterException }.bind()
            Try { UUID.fromString(target) }.fold({ Left(InvalidUuidException) }, { Right(it!!) }).bind()
        }
    )
}

