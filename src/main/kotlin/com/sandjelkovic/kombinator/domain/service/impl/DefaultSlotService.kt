package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import com.sandjelkovic.kombinator.domain.service.SlotService

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
class DefaultSlotService(private val slotRepository: SlotRepository) : SlotService {
    override fun getSlotsByCombination(combinationUUID: String): List<Slot> {
        return slotRepository.findByCombinationUuid(combinationUUID)
                .sortedBy { it.position }
    }

}
