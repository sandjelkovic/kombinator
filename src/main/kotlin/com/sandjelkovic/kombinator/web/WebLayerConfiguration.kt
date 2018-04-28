package com.sandjelkovic.kombinator.web

import com.sandjelkovic.kombinator.web.processors.NoOpResourceProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.ResourceProcessor
import org.springframework.hateoas.mvc.ResourceProcessorInvoker

/**
 * @author sandjelkovic
 * @date 8.11.17.
 */
@Configuration
class WebLayerConfiguration {
    @Bean fun noOpResourceProcessor() = NoOpResourceProcessor()
    @Bean fun resourceProcessorInvoker(processors: List<ResourceProcessor<*>>) = ResourceProcessorInvoker(processors)
    @Bean
    fun uuidValidator() = UUIDValidator()
}
