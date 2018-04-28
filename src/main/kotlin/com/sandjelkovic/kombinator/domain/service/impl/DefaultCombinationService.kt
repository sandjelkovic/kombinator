package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.service.CombinationService
import java.util.*
import javax.validation.ValidationException

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationService(
        private val combinationRepository: CombinationRepository) : CombinationService {
    override fun findByUUID(uuid: String): Optional<Combination> {
        return combinationRepository.findByUuid(uuid);
    }

    override fun findAllCombinations(): List<Combination> {
        return combinationRepository.findAll().toList()
    }

    override fun getCombinationByInternalId(id: Long): Optional<Combination> {
        return if (id > 0) combinationRepository.findById(id)
        else Optional.empty()
    }

    override fun createCombination(combination: Combination): Combination {
        if (combination.id != null || combination.uuid != null) {
            throw ValidationException("ID can't be set when creating")
        }
        val newUUID = UUID.randomUUID().toString()
        return combinationRepository.save(combination.copy(uuid = newUUID))
    }
}
