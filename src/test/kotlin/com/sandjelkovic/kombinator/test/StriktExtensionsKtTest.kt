package com.sandjelkovic.kombinator.test

import arrow.core.Left
import arrow.core.Right
import org.junit.Test
import strikt.api.expectThat
import strikt.api.expectThrows

/**
 * @author sandjelkovic
 * @date 2018-10-20
 */
class StriktExtensionsKtTest {

    @Test
    fun `Should detect that the list of complex objects is sorted`() {
        val list = (1..10).map { WrapperClass(it) }

        val innerValueComparator = Comparator.comparing<WrapperClass<Int>, Int> { it.innerValue }

        expectThat(list).isSorted(innerValueComparator)

        expectThrows<AssertionError> { expectThat(list.reversed()).isSorted(innerValueComparator) }
    }

    @Test
    fun `Should detect that the list of complex objects is not sorted`() {
        val list = (1..10).map { WrapperClass(it) }

        val innerValueComparator = Comparator.comparing<WrapperClass<Int>, Int> { it.innerValue }

        expectThrows<AssertionError> { expectThat(list.reversed()).isSorted(innerValueComparator) }
    }

    @Test
    fun `Should detect that Either is Right`() {
        val right = Right(5)

        expectThat(right).isRight()
        expectThrows<AssertionError> { expectThat(right).isLeft() }
    }

    @Test
    fun `Should detect that Either is Left`() {
        val left = Left(5)

        expectThat(left).isLeft()
        expectThrows<AssertionError> { expectThat(left).isRight() }
    }

    class WrapperClass<T>(val innerValue: T)
}
