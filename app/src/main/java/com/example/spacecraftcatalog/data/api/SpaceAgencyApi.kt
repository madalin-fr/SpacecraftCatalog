// data/api/SpaceAgencyApi.kt
package com.example.spacecraftcatalog.data.api

import com.example.spacecraftcatalog.data.model.AgencyResponse
import com.example.spacecraftcatalog.data.model.SpacecraftResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceAgencyApi {
    @GET("agencies/")
    suspend fun getAgencies(
        @Query("limit") limit: Int = 100,  // Increased limit to get more results
        @Query("offset") offset: Int = 0
    ): AgencyResponse

    @GET("spacecraft/")
    suspend fun getSpacecraft(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("agency__id") agencyId: Int  // Changed to agency__id to match API
    ): SpacecraftResponse
}