// data/api/SpaceAgencyApi.kt
package com.example.spacecraftcatalog.data.api

import com.example.spacecraftcatalog.data.model.AgencyDto
import com.example.spacecraftcatalog.data.model.AgencyResponse
import com.example.spacecraftcatalog.data.model.SpacecraftResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpaceAgencyApi {
    @GET("agencies/")
    suspend fun getAgencies(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0 // Add offset parameter
    ): AgencyResponse

    @GET("agencies/{id}/")
    suspend fun getAgencyById(
        @Path("id") id: Int
    ): AgencyDto

    @GET("spacecraft/")
    suspend fun getSpacecraft(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0 // Add offset parameter
    ): SpacecraftResponse
}