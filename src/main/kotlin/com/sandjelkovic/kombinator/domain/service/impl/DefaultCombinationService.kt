package com.sandjelkovic.kombinator.domain.service.impl

import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.service.CombinationService
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationService(
        val combinationRepository: CombinationRepository) : CombinationService {
    override fun getCombinationByInternalId(id: Long): Optional<Combination> =
            if (id > 0) combinationRepository.findById(id)
            else Optional.empty()

}
