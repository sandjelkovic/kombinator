package com.sandjelkovic.kombinator.domain

import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import com.sandjelkovic.kombinator.domain.service.impl.DefaultCombinationService
import com.sandjelkovic.kombinator.domain.service.impl.DefaultSlotService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author sandjelkovic
 * @date 11.11.17.
 */
@Configuration
class DomainConfiguration {
    @Bean
    fun defaultCombinationService(combinationRepository: CombinationRepository) = DefaultCombinationService(combinationRepository)

    @Bean
    fun defaultSlotService(slotRepository: SlotRepository, combinationRepository: CombinationRepository) =
            DefaultSlotService(slotRepository, combinationRepository)
}
