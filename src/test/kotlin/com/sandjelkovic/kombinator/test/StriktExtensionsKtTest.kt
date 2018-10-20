package com.sandjelkovic.kombinator.test

import arrow.core.Left
import arrow.core.Right
import org.junit.Test
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.isNotNull

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
    }

    @Test
    fun `Should detect that the list of complex objects is not sorted`() {
        val list = (1..10).map { WrapperClass(it) }

        val innerValueComparator = Comparator.comparing<WrapperClass<Int>, Int> { it.innerValue }

        expectThat(catching { expectThat(list.reversed()).isSorted(innerValueComparator) }).isNotNull()
    }

    @Test
    fun `Should detect that Either is Right`() {
        val right = Right(5)

        expectThat(right).isRight()
        expectThat(catching { expectThat(right).isLeft() }).isNotNull()
    }

    @Test
    fun `Should detect that Either is Left`() {
        val left = Left(5)

        expectThat(left).isLeft()
        expectThat(catching { expectThat(left).isRight() }).isNotNull()
    }

    class WrapperClass<T>(val innerValue: T)
}
