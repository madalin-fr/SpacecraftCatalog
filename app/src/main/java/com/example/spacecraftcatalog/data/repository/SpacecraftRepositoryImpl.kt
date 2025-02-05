// data/repository/SpacecraftRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.AgencyDao
import com.example.spacecraftcatalog.data.db.SpacecraftDao
import com.example.spacecraftcatalog.data.mapper.toAgency
import com.example.spacecraftcatalog.data.mapper.toSpacecraft
import com.example.spacecraftcatalog.data.mapper.toSpacecraftEntity
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class SpacecraftRepositoryImpl @Inject constructor(
    private val api: SpaceAgencyApi,
    private val spacecraftDao: SpacecraftDao,
    private val agencyDao: AgencyDao,
    private val connectivityManager: ConnectivityManager
) : SpacecraftRepository {

    override fun getAllSpacecraft(): Flow<List<Spacecraft>> {
        return spacecraftDao.getAllSpacecraft().map { entities ->
            entities.map { entity ->
                val agency = entity.agencyId?.let { agencyDao.getAgencyById(it)?.toAgency() }
                entity.toSpacecraft(agency)
            }
        }
    }

    override suspend fun refreshSpacecraft(shuffle: Boolean) {
        if (!isNetworkAvailable()) {
            if (shuffle) {
                // Get current data
                val currentData = spacecraftDao.getAllSpacecraft().first()

                // Create new list of shuffled entities
                val shuffledEntities = currentData.shuffled()

                // Clear and reinsert to ensure order is preserved
                withContext(Dispatchers.IO) {
                    spacecraftDao.deleteAllSpacecraft() // Add this method to SpacecraftDao
                    spacecraftDao.insertSpacecraft(shuffledEntities)
                }
            }
            return
        }

        try {
            val totalCount = api.getSpacecraft(limit = 1).count

            val offset = if (shuffle && totalCount > 0) {
                (0 until totalCount step 100).toList().random()
            } else 0

            val response = api.getSpacecraft(limit = 100, offset = offset)
            val entities = if (shuffle) {
                response.results.shuffled()
            } else {
                response.results
            }.map { it.toSpacecraftEntity() }

            // Clear existing data and insert new data
            withContext(Dispatchers.IO) {
                spacecraftDao.deleteAllSpacecraft()
                spacecraftDao.insertSpacecraft(entities)
            }
        } catch (e: Exception) {
            throw IOException("Failed to refresh spacecraft: ${e.message}", e)
        }
    }

    override suspend fun getSpacecraftById(id: Int): Spacecraft? {
        return spacecraftDao.getSpacecraftById(id)?.let { entity ->
            val agency = entity.agencyId?.let { agencyDao.getAgencyById(it)?.toAgency() }
            entity.toSpacecraft(agency)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}