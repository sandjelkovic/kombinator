package com.sandjelkovic.kombinator.config

import com.sandjelkovic.kombinator.domain.repository.CombinationRepository
import com.sandjelkovic.kombinator.domain.repository.SlotEntryRepository
import com.sandjelkovic.kombinator.domain.repository.SlotRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author sandjelkovic
 * @date 8.11.17.
 */
@Configuration
class AppConfiguration {
    @Bean
    fun exampleDataRunner(combinationRepository: CombinationRepository,
                          slotEntryRepository: SlotEntryRepository,
                          slotRepository: SlotRepository) = ExampleDataRunner(combinationRepository, slotRepository, slotEntryRepository)
}
