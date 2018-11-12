package com.sandjelkovic.kombinator.domain.model

import arrow.core.Option
import arrow.core.toOption
import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.*

/**
 * @author sandjelkovic
 * @date 6.11.17.
 */
@Entity
data class Slot(
	@Id @GeneratedValue
	var id: Long = 0,
	var name: String = "",
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "slot", orphanRemoval = true, cascade = [CascadeType.ALL])
	@JsonIgnore // TODO move to links or DTO
	var entries: List<SlotEntry> = listOf(),
	var position: Int = 0,
	@ManyToOne
	@JsonIgnore
	var combination: Combination? = null,
	@Version @JsonIgnore
	var version: Long = 0
) {
    val value: Option<BigDecimal>
        get() = entries
			.firstOrNull { it.selected }.toOption()
			.map { it.value }
}
