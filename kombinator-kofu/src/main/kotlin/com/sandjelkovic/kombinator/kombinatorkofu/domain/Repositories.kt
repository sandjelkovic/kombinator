package com.sandjelkovic.kombinator.kombinatorkofu.domain

import com.sandjelkovic.kombinator.domain.model.SlotEntry
import com.sandjelkovic.kombinator.kombinatorkofu.domain.model.Combination
import com.sandjelkovic.kombinator.kombinatorkofu.domain.model.Slot
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// switch to classes to be able to instantiate with bean<>()
interface CombinationRepository : ReactiveCrudRepository<Combination, Long> {
    fun findByName(name: String): Mono<Combination>
    fun findByUuid(uuid: String): Mono<Combination>
}
interface SlotEntryRepository : ReactiveCrudRepository<SlotEntry, Long> {
}
interface SlotRepository : ReactiveCrudRepository<Slot, Long> {
    fun findByCombinationUuid(combinationUuid: String): Flux<Slot>
}
