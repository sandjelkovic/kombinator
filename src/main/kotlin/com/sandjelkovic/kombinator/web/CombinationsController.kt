package com.sandjelkovic.kombinator.web

import arrow.core.getOrElse
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.service.CombinationService
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RestController
@RequestMapping("/combinations")
class CombinationsController(
    val resourceProcessorInvoker: ResourceProcessorInvoker,
    val combinationService: CombinationService,
    val uuidValidator: UUIDValidator
) {

    @GetMapping("/{uuid}")
    fun getCombination(@PathVariable uuid: String): ResponseEntity<Resource<Combination>> {
        return uuidValidator.validateUuid(uuid)
            .fold({ ResponseEntity.badRequest().build<Resource<Combination>>() },
                { validUuid ->
                    combinationService.findByUUID(validUuid.toString())
                        .map { it.toResource() }
                        .map { resourceProcessorInvoker.invokeProcessorsFor(it) }
                        .map { ResponseEntity.ok(it) }
                        .getOrElse { ResponseEntity.notFound().build() }
                }
            )

    }

    @GetMapping
    fun getAllCombinations(): ResponseEntity<Resources<Combination>> {
        val allCombinations = combinationService.findAllCombinations()
        val resources = Resources(allCombinations)
        return ResponseEntity.ok().body(resourceProcessorInvoker.invokeProcessorsFor(resources))
    }

    @PostMapping
    fun createCombination(@Valid combination: Combination): ResponseEntity<Void> =
        combinationService.createCombination(combination)
            .map { URI.create("/combinations/${it.uuid}") }
            .fold(
                { ResponseEntity.badRequest().build<Void>() },
                { ResponseEntity.created(it).build() }
            )

}
