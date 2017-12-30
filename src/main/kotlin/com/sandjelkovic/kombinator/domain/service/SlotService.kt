package com.sandjelkovic.kombinator.domain.service

import com.sandjelkovic.kombinator.domain.model.Slot

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
interface SlotService {
    fun getSlotsByCombination(combinationUUID: String): List<Slot>
}
