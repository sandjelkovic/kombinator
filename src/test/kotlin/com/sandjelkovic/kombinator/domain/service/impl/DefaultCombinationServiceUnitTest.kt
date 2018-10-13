package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.exception.ValidationException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.test.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.*
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationServiceUnitTest {
    companion object {
        private const val invalidId = 100000L
        private const val existingId = 555L
        private const val negativeId = -existingId
    }

    private val existingCombination = Combination(id = existingId, uuid = UUID.randomUUID().toString());

    private val repositoryMock: CombinationRepository = mockk<CombinationRepository>().also {
        every { it.findById(eq(existingId)) } returns Optional.of(existingCombination)
        every { it.findById(eq(invalidId)) } returns Optional.empty()
        every { it.findById(eq(negativeId)) } returns Optional.empty()
        every { it.findByUuid("") } returns Optional.empty()
    }

    private val service = DefaultCombinationService(repositoryMock)

    @Test
    fun `Should retrieve a Combination via ID`() {
        val option = service.getCombinationByInternalId(existingId)

        expectThat(option).isDefined {
            isEqualTo(existingCombination)
            get { id }.isNotNull().isEqualTo(existingId)
        }

        verify(exactly = 1) { repositoryMock.findById(existingId) }
    }

    @Test
    fun `Should not find any Combination and return None`() {
        val option = service.getCombinationByInternalId(invalidId)

        expectThat(option).isEmpty()

        verify(exactly = 1) { repositoryMock.findById(invalidId) }
    }

    @Test
    fun `Should not call try to retrieve if the ID is negative`() {
        val option = service.getCombinationByInternalId(negativeId)

        expectThat(option).isEmpty()

        verify(exactly = 0) { repositoryMock.findById(negativeId) }
    }

    @Test
    fun `Should return all combinations`() {
        val mockedCombinations = (1..5).map { Combination(id = it.toLong()) }

        every { repositoryMock.findAll() } returns mockedCombinations

        val allCombinations = service.findAllCombinations()

        expectThat(allCombinations)
            .isNotEmpty()
            .hasSize(mockedCombinations.size)
            .containsExactlyInAnyOrder(mockedCombinations)
    }

    @Test
    fun `Should return empty collection if there are not combinations`() {

        every { repositoryMock.findAll() } returns emptyList()

        val allCombinations = service.findAllCombinations()

        expectThat(allCombinations).isEmpty()
    }

    @Test
    fun `Should do a search by UUID`() {
        val uuidString = UUID.randomUUID().toString()

        val mockedCombination = Combination(uuid = uuidString, name = "Name")
        every { repositoryMock.findByUuid(uuidString) } returns Optional.of(mockedCombination)

        val option = service.findByUUID(uuidString)

        expectThat(option).isDefined {
            get { uuid }.isEqualTo(uuidString)
            get { name }.isEqualTo("Name")
            isEqualTo(mockedCombination)
        }

        verify(exactly = 1) { repositoryMock.findByUuid(uuidString) }
    }

    @Test
    fun `Should do a search by UUID with an Empty UUID`() {
        val option = service.findByUUID("")

        expectThat(option).isNone()
        verify(exactly = 1) { repositoryMock.findByUuid("") }
    }

    @Test
    fun `Should do a search by Invalid UUID`() {
        val invalidUUID = "12ASD"
        every { repositoryMock.findByUuid(invalidUUID) } returns Optional.empty()

        val option = service.findByUUID(invalidUUID)

        expectThat(option).isNone()
        verify(exactly = 1) { repositoryMock.findByUuid(invalidUUID) }
    }

    @Test
    fun `Should do a search for a valid UUID but no results should be found`() {
        val randomUUID = UUID.randomUUID().toString()

        every { repositoryMock.findByUuid(randomUUID) } returns Optional.empty()

        val option = service.findByUUID(randomUUID)

        expectThat(option).isEmpty()

        verify(exactly = 1) { repositoryMock.findByUuid(randomUUID) }
    }

    @Test
    fun `Should create a Combination`() {
        val combinationToSave = Combination(name = "Prepared combination")
        val combinationsAfterSavingWithIds =
            Combination(id = 11, uuid = UUID.randomUUID().toString(), name = "Prepared combination")
        every { repositoryMock.save(any<Combination>()) } returns combinationsAfterSavingWithIds

        val either = service.createCombination(combinationToSave)

        expectThat(either).isRight {
            get { name }.isEqualTo(combinationToSave.name)
            isEqualTo(combinationsAfterSavingWithIds)
        }

        verify(exactly = 1) { repositoryMock.save(any<Combination>()) }
    }

    @Test
    fun `Should not create a new combination with already existing ID`() {
        val combinationWithID = Combination(id = 1, name = "Super combination")

        val either = service.createCombination(combinationWithID)

        expectThat(either).isLeft {
            isA<ValidationException>()
        }

        verify(exactly = 0) { repositoryMock.save(any<Combination>()) }
    }

    @Test
    fun `Should not create a new combination with already existing UUID`() {
        val combinationWithID = Combination(uuid = UUID.randomUUID().toString(), name = "Super combination")

        val either = service.createCombination(combinationWithID)

        expectThat(either).isLeft {
            isA<ValidationException>()
        }

        verify(exactly = 0) { repositoryMock.save(any<Combination>()) }
    }
}
