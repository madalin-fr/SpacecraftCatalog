// data/api/SpaceAgencyApi.kt
package com.example.spacecraftcatalog.data.api

import com.example.spacecraftcatalog.data.model.AgencyResponse
import com.example.spacecraftcatalog.data.model.SpacecraftResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceAgencyApi {
    @GET("agencies/")
    suspend fun getAgencies(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): AgencyResponse

    @GET("spacecraft/")
    suspend fun getSpacecraft(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("agency_id") agencyId: Int  // Check if this matches your API documentation
    ): SpacecraftResponse
}
