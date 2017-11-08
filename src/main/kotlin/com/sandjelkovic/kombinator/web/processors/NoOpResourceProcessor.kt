package com.sandjelkovic.kombinator.web.processors

import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceProcessor

/**
 * @author sandjelkovic
 * @date 8.11.17.
 */
class NoOpResourceProcessor : ResourceProcessor<Resource<Any>>{
    override fun process(resource: Resource<Any>): Resource<Any> {
        // no op Resource Processor so the context can load if all others are disabled.
        return resource;
    }

}
