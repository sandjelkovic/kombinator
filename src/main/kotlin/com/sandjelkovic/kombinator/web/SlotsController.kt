package com.sandjelkovic.kombinator.web

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
import javax.validation.ValidationException

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
        val uuidValidator: UUIDValidator
) {
    @GetMapping
    fun getSlots(@PathVariable uuid: String): ResponseEntity<Resources<Slot>> {
        return try {
            uuidValidator.validate(uuid)
            combinationService.findByUUID(uuid)
                    .map { slotService.getSlotsByCombination(it.uuid!!) }
                    .map { Resources(it) }
                    .map { ResponseEntity.ok().body(resourceProcessorInvoker.invokeProcessorsFor(it)) }
                    .orElseGet { ResponseEntity.notFound().build() }
        } catch (error: InvalidUUIDException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping
    @PutMapping
    fun addSlots(@Valid @RequestBody slot: Slot, @PathVariable uuid: String): ResponseEntity<Void> {
        return try {
            uuidValidator.validate(uuid)
            combinationService.findByUUID(uuid)
                    .map { slot.combination = it }
                    .map { slotService.save(slot) }
                    .map { ResponseEntity.created(URI.create("/combinations/${it.combination?.uuid}/slots/${it.id}")).build<Void>() }
                    .orElseGet { ResponseEntity.notFound().build() }
        } catch (error: ValidationException) {
            ResponseEntity.badRequest().build<Void>()
        }
    }
}
