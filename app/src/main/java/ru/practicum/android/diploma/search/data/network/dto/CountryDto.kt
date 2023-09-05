package ru.practicum.android.diploma.search.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val id: String? = "",
    var name: String? = "",
    var areas: List<RegionArea?>
)