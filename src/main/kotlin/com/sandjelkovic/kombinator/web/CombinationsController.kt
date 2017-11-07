package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.model.Combination
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * @author sandjelkovic
 * @date 7.11.17.
 */
@RestController
class CombinationsController {
    @GetMapping("/combinations/{id}")
    fun getCombination(@PathVariable id:String): ResponseEntity<Combination> {
        return ResponseEntity.ok(Combination(id = id.toLong()))
    }
}
