package com.sandjelkovic.kombinator.domain.model

import com.sandjelkovic.kombinator.kombinatorkofu.domain.model.Slot
import java.math.BigDecimal

/**
 * @author sandjelkovic
 */
data class SlotEntry(
	var id: Long = 0,
	var name: String = "",
	var data: String = "",
	var url: String = "",
	var value: BigDecimal = BigDecimal.ZERO,
	var selected: Boolean = false,
	var position: Int = 0,
	var slot: Slot? = null,
	var version: Long = 0
)

