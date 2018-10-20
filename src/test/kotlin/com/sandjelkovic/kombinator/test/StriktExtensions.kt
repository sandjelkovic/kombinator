package com.sandjelkovic.kombinator.test

import arrow.core.Either
import arrow.core.Option
import arrow.core.Some
import strikt.api.Assertion
import strikt.assertions.isA

/**
 * @author sandjelkovic
 * @date 2018-10-05
 */

fun Assertion.Builder<String>.isEqualToOneOf(possibilities: Collection<String>): Assertion.Builder<String> =
    assertThat("Value is not present") { actual -> possibilities.contains(actual) }

fun <T : Collection<E>, E : Any> Assertion.Builder<T>.isSorted(comparator: Comparator<E>) =
    assert("is sorted") { actual ->
        for (index in 0 until (actual.size - 1)) {
            if (comparator.compare(actual.elementAt(index), actual.elementAt(index + 1)) > 0)
                fail(
                    actual,
                    "${actual.elementAt(index)} is greater than ${actual.elementAt(index + 1)}"
                )
        }
        pass()
    }

// Arrow
fun <T> Assertion.Builder<Option<T>>.isSome(valueAssertions: Assertion.Builder<T>.() -> Unit = {}): Assertion.Builder<T> =
    assertThat("is Some") { it.isDefined() }
        .isA<Some<T>>()
        .get { t }
        .and(valueAssertions)

fun <T> Assertion.Builder<Option<T>>.isNone(): Assertion.Builder<Option<T>> =
    assertThat("is None") { it.isEmpty() }

fun <L, R> Assertion.Builder<Either<L, R>>.isRight(valueAssertions: Assertion.Builder<R>.() -> Unit = {}): Assertion.Builder<R> =
    assertThat("is Right") { it.isRight() }
        .isA<Either.Right<R>>()
        .get { b }
        .and(valueAssertions)

fun <L, R> Assertion.Builder<Either<L, R>>.isLeft(valueAssertions: Assertion.Builder<L>.() -> Unit = {}): Assertion.Builder<L> =
    assertThat("is Left") { it.isLeft() }
        .isA<Either.Left<L>>()
        .get { a }
        .and(valueAssertions)

