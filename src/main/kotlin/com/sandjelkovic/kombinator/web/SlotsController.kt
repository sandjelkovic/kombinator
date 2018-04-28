package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Slot
import com.sandjelkovic.kombinator.domain.service.CombinationService
import com.sandjelkovic.kombinator.domain.service.SlotService
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
//
//    @PostMapping
//    @PutMapping
//    fun addSlots(@RequestBody slots: List<Slot>) {
//
//    }
}
