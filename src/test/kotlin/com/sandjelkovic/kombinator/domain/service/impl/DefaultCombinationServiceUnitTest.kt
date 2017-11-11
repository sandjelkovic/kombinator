package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationServiceUnitTest {
    lateinit var repositoryMock : CombinationRepository

    private val invalidId = 100000L
    private val existingId = 555L
    private val negativeId = -existingId

    private val existingCombination = Combination(id = existingId, uuid = UUID.randomUUID())

    @Before
    fun setUp() {
        repositoryMock = Mockito.mock(CombinationRepository::class.java)

        Mockito.`when`(repositoryMock.findById(existingId))
                .thenReturn(Optional.of(existingCombination))
        Mockito.`when`(repositoryMock.findById(invalidId))
                .thenReturn(Optional.empty())
        Mockito.`when`(repositoryMock.findById(negativeId))
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

        verify(repositoryMock, Mockito.times(1)).findById(existingId)

    }

    @Test
    fun getCombinationvByInternalIdNonExisting() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(invalidId)
        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))

        verify(repositoryMock, Mockito.times(1)).findById(invalidId)
    }

    @Test
    fun getCombinationByInternalIdNegativeid() {
        val service = DefaultCombinationService(repositoryMock)

        val optional = service.getCombinationByInternalId(negativeId)
        assertThat(optional, notNullValue())
        assertThat(optional.isPresent, equalTo(false))

        verify(repositoryMock, never()).findById(Mockito.any())

    }

}
