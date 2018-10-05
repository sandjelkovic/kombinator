package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import java.util.*

/**
 * @author sandjelkovic
 * @date 15.4.18.
 */
class UUIDValidator {
    fun validate(target: String) {
        try {
            assert(target.isNotEmpty())
            assert(target.isNotBlank())
            UUID.fromString(target)
        } catch (error: AssertionError) {
            throw InvalidUUIDException()
        } catch (illegalArgument: IllegalArgumentException) {
            throw InvalidUUIDException()
        }
    }
}

