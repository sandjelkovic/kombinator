package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.exception.ReferenceDoesntExist
import com.sandjelkovic.kombinator.domain.exception.RequiredParameterMissing
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import com.sandjelkovic.kombinator.domain.service.SlotService

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
class DefaultSlotService(private val slotRepository: SlotRepository,
                         private val combinationRepository: CombinationRepository) : SlotService {
    override fun save(slot: Slot): Slot {
        if (slot.combination == null) {
            throw RequiredParameterMissing("slot.combination")
        }
        if (!combinationRepository.findByUuid(slot.combination?.uuid ?: "").isPresent) {
            throw ReferenceDoesntExist("slot.combination")
        }
        return slotRepository.save(slot)
    }

    override fun getSlotsByCombination(combinationUUID: String): List<Slot> {
        return slotRepository.findByCombinationUuid(combinationUUID)
                .sortedBy { it.position }
    }

}
