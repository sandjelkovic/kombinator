package com.sandjelkovic.kombinator.web

/**
 * @author sandjelkovic
 * @date 5.10.18.
 */
sealed class ValidationException

object EmptyParameterException : ValidationException()
object InvalidUuidException : ValidationException()
