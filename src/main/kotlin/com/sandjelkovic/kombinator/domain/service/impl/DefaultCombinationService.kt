package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.service.CombinationService
import java.lang.IllegalArgumentException
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationService(
        private val combinationRepository: CombinationRepository) : CombinationService {
    override fun findByUUID(uuid: String): Optional<Combination> {
        validateUUID(uuid)

        return combinationRepository.findByUuid(uuid);
    }

    override fun findAllCombinations(): List<Combination> {
        return combinationRepository.findAll().toList()
    }

    override fun getCombinationByInternalId(id: Long): Optional<Combination> {
        return if (id > 0) combinationRepository.findById(id)
        else Optional.empty()
    }

    private fun validateUUID(uuid: String) {
        try {
            assert(uuid.isNotEmpty())
            assert(uuid.isNotBlank())
            UUID.fromString(uuid)
        } catch (error: AssertionError) {
            throw InvalidUUIDException()
        } catch (illegalArgument: IllegalArgumentException) {
            throw InvalidUUIDException()
        }
    }
}
