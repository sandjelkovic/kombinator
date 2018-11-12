package com.sandjelkovic.kombinator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport

@SpringBootApplication
@EnableHypermediaSupport(type = arrayOf(EnableHypermediaSupport.HypermediaType.HAL))
class KombinatorApplication

fun main(args: Array<String>) {
    runApplication<KombinatorApplication>(*args)
}
