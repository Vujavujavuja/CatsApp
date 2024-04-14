package com.vujic.rma1.api

import com.vujic.rma1.api.model.CatApiImage
import com.vujic.rma1.api.model.CatApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {

    @GET("breeds")
    suspend fun getBreeds(): List<CatApiModel>

    @GET("breeds/{id}")
    suspend fun getBreedById(
        @Path("id") id: String
    ): CatApiModel

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("breed_id") id: String
    ): List<CatApiImage>
}