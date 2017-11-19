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
        var id: Long? = null,
        var uuid: String? = null,
        @Version
        var version: Long = 0,
        @NotBlank
        var name: String = "",
        @Enumerated(value = EnumType.STRING)
        var visibility: Visibility = Visibility.PRIVATE,
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "configuration", orphanRemoval = true)
        @JsonIgnore // TODO move to links or DTO
        var slots: List<Slot> = listOf()
)
