package com.vujic.rma1.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CatApiModel(
    val id: String,
    val name: String,
    val description: String,
    val alt_names: String,
    val temperament: String,
    val image: CatApiImage,
    val weight: CatApiWeight,
    var energy_level: Int,
    var affection_level: Int,
    var child_friendly: Int,
    var dog_friendly: Int,
    var stranger_friendly: Int,
    var life_span: String,
    var origin : String,
    var country_codes: String,
    var wikipedia_url: String?,
    var rare : Int,
)

