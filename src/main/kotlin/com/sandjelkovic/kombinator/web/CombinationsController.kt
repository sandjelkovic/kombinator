package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.exception.InvalidUUIDException
import com.sandjelkovic.kombinator.domain.model.Combination
import com.sandjelkovic.kombinator.domain.service.CombinationService
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RestController
class CombinationsController(
        val resourceProcessorInvoker: ResourceProcessorInvoker,
        val combinationService: CombinationService
) {

    @GetMapping("/combinations/{uuid}")
    fun getCombination(@PathVariable uuid: String): ResponseEntity<Resource<Combination>>? {
        return try {
            combinationService.findByUUID(uuid)
                    .map { ResponseEntity.ok(resourceProcessorInvoker.invokeProcessorsFor(it.toResource())) }
                    .orElseGet { ResponseEntity.notFound().build() }
        } catch (error: InvalidUUIDException) {
            ResponseEntity.badRequest().build()
        }
    }

}
