package com.sandjelkovic.kombinator.domain.service.impl

import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.sandjelkovic.kombinator.domain.exception.ReferenceDoesntExist
import com.sandjelkovic.kombinator.domain.exception.RequiredParameterMissing
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.Comparator.comparing

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
class DefaultSlotServiceUnitTest {

    private val existingId = 555L
    private val existingCombination = Combination(id = existingId, uuid = UUID.randomUUID().toString())
    private val slotWithCombination = Slot(name = "Slot name", combination = existingCombination)
    private val slotAfterSaving = slotWithCombination.copy(id = 5)

    private val mockSlotRepository = mockk<SlotRepository>().also {
        every { it.findByCombinationUuid(existingCombination.uuid!!) } returns listOf(
                Slot(name = "GPU", combination = existingCombination, position = 2),
                Slot(name = "CPU", combination = existingCombination, position = 1))
        every { it.save(eq(slotWithCombination)) } returns slotAfterSaving
    }

    private val mockCombinationRepository = mockk<CombinationRepository>().also {
        every { it.findByUuid(eq("")) } returns Optional.empty()
        every { it.findByUuid(eq(existingCombination.uuid!!)) } returns Optional.of(existingCombination)
    }

    private lateinit var slotService: DefaultSlotService

    @Before
    fun setUp() {
        slotService = DefaultSlotService(mockSlotRepository, mockCombinationRepository)
    }

    @Test
    fun `Should get the slots associated with Combination from UUID`() {
        val slots = slotService.getSlotsByCombination(existingCombination.uuid!!)

        assertThat(slots)
                .isNotEmpty
                .hasSize(2)
                .allMatch { slot -> slot.combination!!.id == existingCombination.id }
                .allMatch { slot -> slot.combination!!.uuid == existingCombination.uuid }
                .allMatch { it.name == "CPU" || it.name == "GPU" }
                .isSortedAccordingTo(comparing<Slot, Int> { it.position })
    }

    @Test
    fun `Should save the Slot`() {
        assertk.assert { slotService.save(slotWithCombination) }.returnedValue {
            isEqualTo(slotAfterSaving)
        }
    }

    @Test
    fun `Should throw validation exception if the connected Combination doesn't exist`() {
        assertk.assert { slotService.save(Slot(name = "Slot name", combination = Combination())) }.thrownError {
            isInstanceOf(ReferenceDoesntExist::class)
            hasMessage("slot.combination")
        }
    }

    @Test
    fun `Should throw validation exception if there is no Combination object connected to the Slot`() {
        assertk.assert { slotService.save(Slot(name = "Slot name")) }.thrownError {
            isInstanceOf(RequiredParameterMissing::class)
            hasMessage("slot.combination")
        }
    }
}
