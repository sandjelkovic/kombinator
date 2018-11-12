package com.sandjelkovic.kombinator.kombinatorkofu.domain.model


/**
 * @author sandjelkovic
 */
data class Combination(
	var id: Long? = null,
	var uuid: String? = null,
	var version: Long = 0,
	var name: String = "",
	var visibility: Visibility = Visibility.PRIVATE,
	var slots: List<Slot> = listOf()
)
