package com.sandjelkovic.kombinator.web

import org.hamcrest.Matchers

/**
 * @author sandjelkovic
 * @date 30.12.17.
 */
open class ControllerTest {
    val fullCombinationName = "EPIC Computer"
    protected fun jsonListSizeMatcher(number: Number) = Matchers.everyItem(Matchers.equalTo(number))
}
