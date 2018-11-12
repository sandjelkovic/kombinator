package com.sandjelkovic.kombinator.domain.repository

import com.sandjelkovic.kombinator.domain.model.Combination
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * @author sandjelkovic
 * @date 9.11.17.
 */
interface CombinationRepository : CrudRepository<Combination, Long> {
    fun findByName(name: String): Optional<Combination>
    fun findByUuid(uuid: String): Optional<Combination>
}
