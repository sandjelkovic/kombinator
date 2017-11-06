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
        var entries: List<SlotEntry> = listOf(),
        var position: Int = 0,
        @ManyToOne
        var configuration: Combination,
        @Version @JsonIgnore
        var version: Long = 0) {
    fun value(): BigDecimal = entries.first { it.selected }.value
}
