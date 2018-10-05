package com.sandjelkovic.kombinator.domain.service.impl

import arrow.core.Either
import assertk.assertions.isTrue
import com.sandjelkovic.kombinator.domain.exception.ValidationException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationServiceUnitTest {
    lateinit var repositoryMock: CombinationRepository

    private val invalidId = 100000L
    private val existingId = 555L
    private val negativeId = -existingId

    private val existingCombination = Combination(id = existingId, uuid = UUID.randomUUID().toString())

    @Before
    fun setUp() {
        repositoryMock = mock(CombinationRepository::class.java)

        `when`(repositoryMock.findById(existingId))
            .thenReturn(Optional.of(existingCombination))
        `when`(repositoryMock.findById(invalidId))
            .thenReturn(Optional.empty())
        `when`(repositoryMock.findById(negativeId))
            .thenReturn(Optional.empty())
    }


    @Test
    fun getCombinatioByInternalId() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(existingId)

        assertThat(optional).isNotNull
        assertThat(optional.isDefined()).isTrue()
        assertThat(optional.get().id)
            .isNotNull()
            .isEqualTo(existingId)
        assertThat(optional.get()).isEqualTo(existingCombination)

        verify(repositoryMock).findById(existingId)

    }

    @Test
    fun getCombinationvByInternalIdNonExisting() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(invalidId)

        assertThat(optional).isNotNull
        assertThat(optional.isEmpty()).isTrue()

        verify(repositoryMock).findById(invalidId)
    }

    @Test
    fun getCombinationByInternalIdNegativeid() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(negativeId)

        assertThat(optional)
            .isNotNull
        assertThat(optional.isEmpty()).isTrue()

        verify(repositoryMock, never()).findById(any())

    }

    @Test
    fun findAllCombinations() {
        val service = DefaultCombinationService(repositoryMock)
        val mockedCombinations = (1..5)
            .map { Combination(id = it.toLong()) }

        `when`(repositoryMock.findAll())
            .thenReturn(mockedCombinations)

        val allCombinations = service.findAllCombinations()

        assertThat(allCombinations)
            .isNotNull
            .isNotEmpty
            .hasSize(5)
            .contains(*mockedCombinations.toTypedArray())
    }

    @Test
    fun findAllCombinationsEmpty() {
        val service = DefaultCombinationService(repositoryMock)

        `when`(repositoryMock.findAll())
            .thenReturn(emptyList())

        val allCombinations = service.findAllCombinations()

        assertThat(allCombinations)
            .isNotNull
            .isEmpty()
    }

    @Test
    fun findByUUID() {
        val service = DefaultCombinationService(repositoryMock)
        val uuid = UUID.randomUUID().toString()

        `when`(repositoryMock.findByUuid(uuid))
            .thenReturn(Optional.of(Combination(uuid = uuid, name = "Name")))

        val option = service.findByUUID(uuid)

        assertThat(option.isDefined()).isTrue()
        assertThat(option.get().uuid).isEqualTo(uuid)
        assertThat(option.get().name).isEqualTo("Name")

        verify(repositoryMock).findByUuid(uuid)
    }

    @Test
    fun findByUUIDEmptyUUID() {
        val service = DefaultCombinationService(repositoryMock)

        assertk.assert { service.findByUUID("") }.returnedValue {
            assertk.assert(actual.isEmpty()).isTrue()
        }
    }

    @Test
    fun findByUUIDInvalidUUID() {
        val service = DefaultCombinationService(repositoryMock)

        assertk.assert { service.findByUUID("12ASD") }.returnedValue {
            assertk.assert(actual.isEmpty()).isTrue()
        }
    }

    @Test
    fun findByUUIDNotFound() {
        val service = DefaultCombinationService(repositoryMock)
        val uuid = UUID.randomUUID()

        `when`(repositoryMock.findByUuid(uuid.toString()))
            .thenReturn(Optional.empty())

        val option = service.findByUUID(uuid.toString())

        assertThat(option.isEmpty()).isTrue()

        verify(repositoryMock).findByUuid(uuid.toString())
    }

    @Test
    fun shouldCreateCombination() {
        val service = DefaultCombinationService(repositoryMock)
        `when`(repositoryMock.save(any<Combination>()))
            .thenReturn(Combination(id = 11, uuid = UUID.randomUUID().toString(), name = "Super combination"))

        val preparedCombination = Combination(name = "Super combination")

        val either = service.createCombination(preparedCombination)

        assertThat(either is Either.Right).isTrue()
        val eitherRight = either as Either.Right
        assertThat(eitherRight.b.name).isEqualTo(preparedCombination.name)
        verify(repositoryMock).save(any<Combination>())
    }

    @Test
    fun shouldNotCreateCombinationWithID() {
        val service = DefaultCombinationService(repositoryMock)

        val combinationWithID = Combination(id = 1, name = "Super combination")

        val either = service.createCombination(combinationWithID)

        assertThat(either is Either.Left).isTrue()
        val leftEither = either as Either.Left
        assertThat(leftEither.a).isInstanceOf(ValidationException::class.java)
        verify(repositoryMock, never()).save(any<Combination>())
    }

    @Test
    fun shouldNotCreateCombinationWithUUID() {
        val service = DefaultCombinationService(repositoryMock)

        val combinationWithID = Combination(uuid = UUID.randomUUID().toString(), name = "Super combination")

        val either = service.createCombination(combinationWithID)

        assertThat(either is Either.Left).isTrue()
        val leftEither = either as Either.Left
        assertThat(leftEither.a).isInstanceOf(ValidationException::class.java)
        verify(repositoryMock, never()).save(any<Combination>())
    }
}
