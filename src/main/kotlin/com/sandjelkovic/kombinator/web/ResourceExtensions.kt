package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.domain.model.Combination
import org.springframework.hateoas.Resource

/**
 * @author sandjelkovic
 * @date 8.11.17.
 */

fun Combination.toResource() = Resource<Combination>(this)
