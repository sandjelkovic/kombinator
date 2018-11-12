package com.sandjelkovic.kombinator.domain.repository

import com.sandjelkovic.kombinator.domain.model.SlotEntry
import org.springframework.data.repository.CrudRepository

/**
 * @author sandjelkovic
 * @date 19.11.17.
 */
interface SlotEntryRepository : CrudRepository<SlotEntry, Long> {
}
