// data/repository/SpacecraftRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.AgencyDao
import com.example.spacecraftcatalog.data.db.SpacecraftDao
import com.example.spacecraftcatalog.data.mapper.toAgency
import com.example.spacecraftcatalog.data.mapper.toSpacecraft
import com.example.spacecraftcatalog.data.mapper.toSpacecraftEntity
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SpacecraftRepositoryImpl @Inject constructor(
    private val api: SpaceAgencyApi,
    private val dao: SpacecraftDao,
    private val agencyDao: AgencyDao,
    private val connectivityManager: ConnectivityManager
) : SpacecraftRepository {

    override fun getSpacecraftByAgency(agencyId: Int): Flow<List<Spacecraft>> {
        return dao.getSpacecraftByAgency(agencyId).map { entities ->
            entities.map { entity ->
                val agency = agencyDao.getAgencyById(entity.agencyId ?: -1)
                entity.toSpacecraft(agency?.toAgency())
            }
        }
    }

    override suspend fun refreshSpacecraftForAgency(agencyId: Int) {
        if (!isNetworkAvailable()) {
            throw IOException("No internet connection available")
        }

        try {
            val response = api.getSpacecraft(limit = 100, agencyId = agencyId)
            val spacecraftEntities = response.results.mapNotNull { dto ->
                try {
                    dto.toSpacecraftEntity(requestedAgencyId = agencyId)
                } catch (e: Exception) {
                    Log.e("SpacecraftRepo", "Error mapping spacecraft", e)
                    null
                }
            }
            dao.upsertSpacecraftForAgency(agencyId, spacecraftEntities)
        } catch (e: Exception) {
            throw IOException("Failed to refresh spacecraft: ${e.message}", e)
        }
    }




    override suspend fun getSpacecraftById(id: Int): Spacecraft? {
        return dao.getSpacecraftById(id)?.toSpacecraft()
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}