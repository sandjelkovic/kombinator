package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.test.isInvalid
import com.sandjelkovic.kombinator.test.isValid
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.util.*

/**
 * @author sandjelkovic
 * @date 2019-01-04
 */
class UUIDValidatorTest {

    private val validator = UUIDValidator()

    @Test
    fun `Should validate the correct UUID`() {
        val randomUUID = UUID.randomUUID()!!
        val validated = validator.validate(randomUUID.toString())

        expectThat(validated).isValid {
            isEqualTo(randomUUID)
        }
    }

    @Test
    fun `Should be Invalid for empty UUID`() {
        val validated = validator.validate("")

        expectThat(validated).isInvalid {
            isA<EmptyParameterException>()
        }
    }

    @Test
    fun `Should be Invalid for blank UUID`() {
        val validated = validator.validate("            ")

        expectThat(validated).isInvalid {
            isA<EmptyParameterException>()
        }
    }

    @Test
    fun `Should be Invalid for malformed UUID`() {
        val validated = validator.validate("malformed-uuid")

        expectThat(validated).isInvalid {
            isA<InvalidUuidException>()
        }
    }
}
