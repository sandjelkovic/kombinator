package com.sandjelkovic.kombinator.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.validator.constraints.URL
import java.math.BigDecimal
import javax.persistence.*

/**
 * @author sandjelkovic
 * @date 6.11.17.
 */
@Entity
data class SlotEntry(
	@Id @GeneratedValue
	var id: Long = 0,
	var name: String = "",
	var data: String = "",
	@URL
	var url: String = "",
	var value: BigDecimal = BigDecimal.ZERO,
	var selected: Boolean = false,
	var position: Int = 0,
	@ManyToOne
	@JsonIgnore // TODO move to links or DTO
	var slot: Slot? = null,
	@Version
	var version: Long = 0
)

