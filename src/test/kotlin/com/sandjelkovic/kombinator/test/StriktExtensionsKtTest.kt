package com.sandjelkovic.kombinator.test

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

        expectThat(list).isSortedAccordingTo(innerValueComparator)

        expectThrows<AssertionError> { expectThat(list.reversed()).isSortedAccordingTo(innerValueComparator) }
    }

    @Test
    fun `Should detect that the list of complex objects is not sorted`() {
        val list = (1..10).map { WrapperClass(it) }

        val innerValueComparator = Comparator.comparing<WrapperClass<Int>, Int> { it.innerValue }

        expectThrows<AssertionError> { expectThat(list.reversed()).isSortedAccordingTo(innerValueComparator) }
    }

    class WrapperClass<T>(val innerValue: T)
}
