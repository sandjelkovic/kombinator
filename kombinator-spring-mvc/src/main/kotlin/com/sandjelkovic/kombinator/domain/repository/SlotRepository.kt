package com.sandjelkovic.kombinator.domain.repository

import com.sandjelkovic.kombinator.domain.model.Slot
import org.springframework.data.repository.CrudRepository

/**
 * @author sandjelkovic
 * @date 19.11.17.
 */
interface SlotRepository : CrudRepository<Slot, Long> {
    fun findByCombinationUuid(combinationUuid: String): List<Slot>
}
