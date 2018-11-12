package com.sandjelkovic.kombinator.kombinatorkofu.domain.model

import arrow.core.Option
import arrow.core.toOption
import com.sandjelkovic.kombinator.domain.model.SlotEntry
import java.math.BigDecimal

/**
 * @author sandjelkovic
 */
data class Slot(
	var id: Long = 0,
	var name: String = "",
	var entries: List<SlotEntry> = listOf(),
	var position: Int = 0,
	var combination: Combination? = null,
	var version: Long = 0
) {
    val value: Option<BigDecimal>
        get() = entries
			.firstOrNull { it.selected }.toOption()
			.map { it.value }
}
