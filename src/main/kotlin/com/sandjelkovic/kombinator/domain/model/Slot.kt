package com.sandjelkovic.kombinator.domain.model

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
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "slot", orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
        @JsonIgnore // TODO move to links or DTO
        var entries: List<SlotEntry> = listOf(),
        var position: Int = 0,
        @ManyToOne
        @JsonIgnore
        var combination: Combination? = null,
        @Version @JsonIgnore
        var version: Long = 0) {
    fun value(): BigDecimal = entries.stream()
            .filter { it.selected }
            .findAny()
            .map { it.value }
            .orElse(BigDecimal.ZERO)
}
