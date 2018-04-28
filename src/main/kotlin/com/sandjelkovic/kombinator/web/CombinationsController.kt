package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.service.CombinationService
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid
import javax.validation.ValidationException

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RestController
@RequestMapping("/combinations")
class CombinationsController(
        val resourceProcessorInvoker: ResourceProcessorInvoker,
        val combinationService: CombinationService,
        val uuidValidator: UUIDValidator) {

    @GetMapping("/{uuid}")
    fun getCombination(@PathVariable uuid: String): ResponseEntity<Resource<Combination>> {
        return try {
            uuidValidator.validate(uuid)
            combinationService.findByUUID(uuid)
                    .map { ResponseEntity.ok(resourceProcessorInvoker.invokeProcessorsFor(it.toResource())) }
                    .orElseGet { ResponseEntity.notFound().build() }
        } catch (error: InvalidUUIDException) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping
    fun getAllCombinations(): ResponseEntity<Resources<Combination>> {
        val allCombinations = combinationService.findAllCombinations()
        val resources = Resources(allCombinations)
        return ResponseEntity.ok().body(resourceProcessorInvoker.invokeProcessorsFor(resources))
    }

    @PostMapping
    fun createCombination(@Valid combination: Combination): ResponseEntity<Void> {
        return try {
            val created = combinationService.createCombination(combination)
            ResponseEntity.created(URI.create("/combinations/${created.uuid}")).build()
        } catch (exception: ValidationException) {
            ResponseEntity.badRequest().build<Void>()
        }
    }
}
