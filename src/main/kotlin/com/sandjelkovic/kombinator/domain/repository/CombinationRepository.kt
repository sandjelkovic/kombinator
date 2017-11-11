package com.sandjelkovic.kombinator.domain.repository

import com.sandjelkovic.kombinator.domain.model.Combination
import org.springframework.data.repository.CrudRepository

/**
 * @author sandjelkovic
 * @date 9.11.17.
 */
interface CombinationRepository : CrudRepository<Combination, Long>{
}
