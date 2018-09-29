package com.sandjelkovic.kombinator.web

import arrow.core.Either
import arrow.core.Try
import arrow.core.getOrElse
import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.service.CombinationService
import com.sandjelkovic.kombinator.domain.service.SlotService
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
@RestController
@RequestMapping("/combinations/{uuid}/slots")
class SlotsController(
        val resourceProcessorInvoker: ResourceProcessorInvoker,
        val slotService: SlotService,
        val combinationService: CombinationService,
        val uuidValidator: UUIDValidator) {

    @GetMapping
    fun getSlots(@PathVariable uuid: String): ResponseEntity<Resources<Slot>> {
        return try {
            uuidValidator.validate(uuid)
            combinationService.findByUUID(uuid)
                    .map { slotService.getSlotsByCombination(it.uuid!!) }
                    .map { Resources(it) }
                    .map { ResponseEntity.ok().body(resourceProcessorInvoker.invokeProcessorsFor(it)) }
                    .getOrElse { ResponseEntity.notFound().build() }
        } catch (error: InvalidUUIDException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping
    @PutMapping
    fun addSlots(@Valid @RequestBody slot: Slot, @PathVariable uuid: String): ResponseEntity<Void> {
        return Try { uuidValidator.validate(uuid) }
                .fold(
                        { ResponseEntity.badRequest().build<Void>() },
                        { _ ->
                            combinationService.findByUUID(uuid)
                                    .map { combination -> slot.copy(combination = combination) }
                                    .map { newSlot -> slotService.save(newSlot) }
                                    .fold({ ResponseEntity.notFound().build<Void>() },
                                            eitherToResponseEntityMapper(slotToResponseMapper()))
                        })
    }


    fun <T> eitherToResponseEntityMapper(rightMapper: (T) -> ResponseEntity<Void>)
            : (Either<Exception, T>) -> ResponseEntity<Void> {
        return { either -> either.fold({ ResponseEntity.badRequest().build<Void>() }, rightMapper) }
    }

    fun slotToResponseMapper(): (Slot) -> ResponseEntity<Void> {
        return { slot -> ResponseEntity.created(mapToUri(slot)).build<Void>() }
    }

    fun mapToUri(it: Slot) = URI.create("/combinations/${it.combination?.uuid}/slots/${it.id}")!!
}
