package com.sandjelkovic.kombinator.domain.service

import arrow.core.Either
import arrow.core.Option
import com.sandjelkovic.kombinator.domain.exception.ValidationException
import com.sandjelkovic.kombinator.domain.model.Combination

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
interface CombinationService {
    fun getCombinationByInternalId(id: Long): Option<Combination>
    fun findAllCombinations() : List<Combination>
    fun findByUUID(uuid: String): Option<Combination>
    fun createCombination(combination: Combination): Either<ValidationException, Combination>
}
