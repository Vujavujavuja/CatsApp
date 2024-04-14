package com.vujic.rma1.api.model

import com.vujic.rma1.list.model.Weight
import kotlinx.serialization.Serializable

@Serializable
data class CatApiWeight(
    val imperial: String,
    val metric: String,
)