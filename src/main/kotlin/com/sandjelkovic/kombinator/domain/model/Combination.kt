package com.sandjelkovic.kombinator.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank


/**
 * @author sandjelkovic
 * @date 6.11.17.
 */
@Entity
data class Combination(
        @Id @GeneratedValue
        var id: Long = 0,
        @Version @JsonIgnore
        var version: Long = 0,
        @NotBlank
        var name: String = "",
        @Enumerated(value = EnumType.STRING)
        var visibility: Visibility = Visibility.PRIVATE,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "configuration", orphanRemoval = true)
        var slots: List<Slot> = listOf()
)