package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.model.Combination
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ResourceProcessorInvoker
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RestController
class CombinationsController(
        val resourceProcessorInvoker: ResourceProcessorInvoker
) {

    @GetMapping("/combinations/{id}")
    fun getCombination(@PathVariable id: String): ResponseEntity<Resource<Combination>>? {
        val combination = Combination(id = id.toLong(), uuid = UUID.randomUUID()) // fake creation
        return ResponseEntity.ok(resourceProcessorInvoker.invokeProcessorsFor(combination.toResource()))
    }
}
