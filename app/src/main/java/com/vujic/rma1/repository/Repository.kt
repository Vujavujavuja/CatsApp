package com.vujic.rma1.repository

import com.vujic.rma1.api.CatsApi
import com.vujic.rma1.api.model.CatApiImage
import com.vujic.rma1.api.model.CatApiModel
import com.vujic.rma1.networking.retrofit

object Repository {
    private val catsApi by lazy { retrofit.create(CatsApi::class.java) }

    suspend fun fetchAllBreeds(): List<CatApiModel> {
        return catsApi.getBreeds().map { it }
    }

    suspend fun fetchBreedById(breedId: String): CatApiModel {
        return catsApi.getBreedById(breedId)
    }

    suspend fun fetchBreedImages(breedId: String): CatApiImage? {
        return catsApi.getBreedImages(breedId).firstOrNull()
    }




}
