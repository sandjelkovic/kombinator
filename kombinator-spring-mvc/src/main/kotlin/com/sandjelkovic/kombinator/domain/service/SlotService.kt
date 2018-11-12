package com.sandjelkovic.kombinator.domain.service

import arrow.core.Either
import com.sandjelkovic.kombinator.domain.exception.DomainValidationException
import com.sandjelkovic.kombinator.domain.model.Slot

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
interface SlotService {
    fun getSlotsByCombination(combinationUUID: String): List<Slot>
    fun save(slot: Slot): Either<DomainValidationException, Slot>
}
