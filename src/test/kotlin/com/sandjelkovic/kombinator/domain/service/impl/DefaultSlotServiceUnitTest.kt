package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*
import java.util.Comparator.comparing

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
class DefaultSlotServiceUnitTest {
    lateinit var repositoryMock: SlotRepository

    private val invalidId = 100000L
    private val existingId = 555L
    private val negativeId = -existingId
    private val existingCombination = Combination(id = existingId, uuid = UUID.randomUUID().toString())

    lateinit var target: DefaultSlotService

    @Before
    fun setUp() {
        repositoryMock = Mockito.mock(SlotRepository::class.java)

        Mockito.`when`(repositoryMock.findByCombinationUuid(existingCombination.uuid!!))
                .thenReturn(listOf(
                        Slot(name = "GPU", combination = existingCombination, position = 2),
                        Slot(name = "CPU", combination = existingCombination, position = 1)
                ))
        target = DefaultSlotService(repositoryMock)
    }

    @Test
    fun getSlotsByCombination() {
        val slots = target.getSlotsByCombination(existingCombination.uuid!!)

        assertThat(slots)
                .isNotEmpty
                .hasSize(2)
                .allMatch { slot -> slot.combination!!.id == existingCombination.id }
                .allMatch { slot -> slot.combination!!.uuid == existingCombination.uuid }
                .allMatch { it.name == "CPU" || it.name == "GPU" }
                .isSortedAccordingTo(comparing<Slot, Int> { it.position })
    }

}
