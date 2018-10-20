package com.sandjelkovic.kombinator.domain.service.impl

import arrow.core.Either
import arrow.core.filterOrElse
import arrow.core.right
import com.sandjelkovic.kombinator.domain.exception.DomainValidationException
import com.sandjelkovic.kombinator.domain.exception.DomainValidationException.ReferenceNotFound
import com.sandjelkovic.kombinator.domain.exception.DomainValidationException.RequiredParameterMissing
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import com.sandjelkovic.kombinator.domain.service.SlotService

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
class DefaultSlotService(
    private val slotRepository: SlotRepository,
    private val combinationRepository: CombinationRepository
) : SlotService {
    override fun save(slot: Slot): Either<DomainValidationException, Slot> = slot.right()
        .filterOrElse({ slot.combination != null }, { RequiredParameterMissing("slot.combination") })
        .filterOrElse({ combinationExists(slot.combination) }, { ReferenceNotFound("slot.combination") })
        .map { slotRepository.save(it) }

    override fun getSlotsByCombination(combinationUUID: String): List<Slot> =
        slotRepository.findByCombinationUuid(combinationUUID)
            .sortedBy { it.position }

    private fun combinationExists(combination: Combination?) =
        combinationRepository.findByUuid(combination?.uuid ?: "").isPresent

}
