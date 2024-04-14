package com.vujic.rma1.list.model

data class Cat(
    val id: String,
    val name: String,
    val description: String,
    val alt_names: String?,
    val temperament: String,
    var image: Image?,
    val weight: Weight?,
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

data class Image(
    val id: String,
    val url: String,
)

data class Weight(
    val imperial: String,
    val metric: String,
)