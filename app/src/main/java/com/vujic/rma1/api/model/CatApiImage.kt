package com.vujic.rma1.api.model

import com.vujic.rma1.list.model.Image
import kotlinx.serialization.Serializable


@Serializable
data class CatApiImage(
    val id: String,
    val url: String,
)