package com.sandjelkovic.kombinator.web

import arrow.core.*
import arrow.data.Validated
import arrow.typeclasses.binding
import java.util.*

/**
 * @author sandjelkovic
 * @date 15.4.18.
 */

class UUIDValidator {
    fun validateUuid(target: String): Validated<ValidationException, UUID> = Validated.fromEither(
        Either.monadError<ValidationException>().binding {
            Right(target).filterOrElse({ it.isNotEmpty() }) { EmptyParameterException }.bind()
            Right(target).filterOrElse({ it.isNotBlank() }) { EmptyParameterException }.bind()
            Try { UUID.fromString(target) }.fold({ Left(InvalidUuidException) }, { Right(it!!) }).bind()
        }.fix()
    )
}

