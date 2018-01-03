package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import javax.validation.ValidationException

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationServiceUnitTest {
    lateinit var repositoryMock : CombinationRepository

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
        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(true))
        assertThat(optional.get().id, notNullValue())
        assertThat(optional.get().id, equalTo(existingId))
        assertThat(optional.get(), equalTo(existingCombination))

        verify(repositoryMock, times(1)).findById(existingId)

    }

    @Test
    fun getCombinationvByInternalIdNonExisting() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(invalidId)
        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))

        verify(repositoryMock, times(1)).findById(invalidId)
    }

    @Test
    fun getCombinationByInternalIdNegativeid() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(negativeId)
        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))

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
        assertThat(allCombinations, notNullValue())
        assertThat(allCombinations, not(empty()))
        assertThat(allCombinations, hasSize(5))
        assertThat(allCombinations, containsInAnyOrder(*mockedCombinations.toTypedArray()))
    }

    @Test
    fun findAllCombinationsEmpty() {
        val service = DefaultCombinationService(repositoryMock)

        `when`(repositoryMock.findAll())
                .thenReturn(emptyList())

        val allCombinations = service.findAllCombinations()
        assertThat(allCombinations, notNullValue())
        assertThat(allCombinations, empty())
    }

    @Test
    fun findByUUID() {
        val service = DefaultCombinationService(repositoryMock)
        val uuid = UUID.randomUUID().toString()

        `when`(repositoryMock.findByUuid(uuid))
                .thenReturn(Optional.of(Combination(uuid = uuid, name = "Name")))

        val optional = service.findByUUID(uuid)

        assertThat(optional.isPresent, equalTo(true))
        assertThat(optional.get().uuid, equalTo(uuid))
        assertThat(optional.get().name, equalTo("Name"))
        verify(repositoryMock).findByUuid(uuid)
    }

    @Test(expected = InvalidUUIDException::class)
    fun findByUUIDEmptyUUID() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.findByUUID("")
    }

    @Test(expected = InvalidUUIDException::class)
    fun findByUUIDInvalidUUID() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.findByUUID("12ASD")
    }

    @Test
    fun findByUUIDNotFound() {
        val service = DefaultCombinationService(repositoryMock)
        val uuid = UUID.randomUUID()

        `when`(repositoryMock.findByUuid(uuid.toString()))
                .thenReturn(Optional.empty())

        val optional = service.findByUUID(uuid.toString())
        assertThat(optional.isPresent, equalTo(false))
        verify(repositoryMock).findByUuid(uuid.toString())
    }

    @Test
    fun shouldCreateCombination() {
        val service = DefaultCombinationService(repositoryMock)
        `when`(repositoryMock.save(any<Combination>()))
                .thenReturn(Combination(id = 11, uuid = UUID.randomUUID().toString(), name = "Super combination"))

        val preparedCombination = Combination(name = "Super combination")

        val savedCombination = service.createCombination(preparedCombination)

        Assertions.assertThat(savedCombination.name).isEqualTo(preparedCombination.name)
        verify(repositoryMock).save(any<Combination>())
    }

    @Test
    fun shouldNotCreateCombinationWithID() {
        val service = DefaultCombinationService(repositoryMock)

        val combinationWithID = Combination(id = 1, name = "Super combination")

        val throwable = Assertions.catchThrowable { service.createCombination(combinationWithID) }

        Assertions.assertThat(throwable).isInstanceOf(ValidationException::class.java)
        verify(repositoryMock, never()).save(any<Combination>())
    }

    @Test
    fun shouldNotCreateCombinationWithUUID() {
        val service = DefaultCombinationService(repositoryMock)

        val combinationWithID = Combination(uuid = UUID.randomUUID().toString(), name = "Super combination")

        val throwable = Assertions.catchThrowable { service.createCombination(combinationWithID) }

        Assertions.assertThat(throwable).isInstanceOf(ValidationException::class.java)
        verify(repositoryMock, never()).save(any<Combination>())
    }
}
