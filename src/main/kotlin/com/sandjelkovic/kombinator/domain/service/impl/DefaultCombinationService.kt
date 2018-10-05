package com.sandjelkovic.kombinator.domain.service.impl

import arrow.core.Either
import arrow.core.Option
import arrow.core.filterOrElse
import arrow.core.rightIfNotNull
import com.sandjelkovic.flatMapToOption
import com.sandjelkovic.kombinator.domain.exception.ValidationException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.service.CombinationService
import java.util.*

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
class DefaultCombinationService(
	private val combinationRepository: CombinationRepository
) : CombinationService {
    override fun findByUUID(uuid: String): Option<Combination> =
		combinationRepository.findByUuid(uuid).flatMapToOption()

    override fun findAllCombinations(): List<Combination> = combinationRepository.findAll().toList()

    override fun getCombinationByInternalId(id: Long): Option<Combination> =
		if (id > 0) combinationRepository.findById(id).flatMapToOption()
		else Option.empty()

    override fun createCombination(combination: Combination): Either<ValidationException, Combination> =
		combination.rightIfNotNull { ValidationException("Combination can't be null") }
			.filterOrElse({ it.id == null }, { ValidationException("ID can't be set on creation") })
			.filterOrElse({ it.uuid == null }, { ValidationException("UUID can't be set on creation") })
			.map(combinationUuidEnricher(::generateUUIDString))
			.map { combinationRepository.save(it) }

    private fun generateUUIDString() = UUID.randomUUID().toString()
    private fun combinationUuidEnricher(uuidSupplier: () -> String) =
		{ combination: Combination -> combination.copy(uuid = uuidSupplier()) }


}
