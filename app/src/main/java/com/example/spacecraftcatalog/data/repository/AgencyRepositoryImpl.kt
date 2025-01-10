// data/repository/AgencyRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.AgencyDao
import com.example.spacecraftcatalog.data.mapper.toAgency
import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import com.example.spacecraftcatalog.data.mapper.toAgencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class AgencyRepositoryImpl @Inject constructor(
    private val api: SpaceAgencyApi,
    private val dao: AgencyDao,
    private val connectivityManager: ConnectivityManager
) : AgencyRepository {

    override fun getAgencies(): Flow<List<Agency>> {
        return dao.getAllAgencies().map { entities ->
            entities.map { it.toAgency() }
        }
    }

    override suspend fun refreshAgencies(shuffle: Boolean) {
        if (!isNetworkAvailable()) {
            if (shuffle) {
                // Get current data
                val currentData = dao.getAllAgencies().first()

                // Create new list of shuffled entities
                val shuffledEntities = currentData.shuffled()

                // Clear and reinsert to ensure order is preserved
                withContext(Dispatchers.IO) {
                    dao.deleteAllAgencies() // Add this method to AgencyDao
                    dao.insertAgencies(shuffledEntities)
                }
            }
            return
        }

        try {
            val initialResponse = api.getAgencies(limit = 1)
            val totalItems = initialResponse.count

            if (totalItems > 0) {
                val offset = if (shuffle) {
                    val totalPages = (totalItems + 99) / 100
                    Random.nextInt(totalPages) * 100
                } else 0

                val response = api.getAgencies(limit = 100, offset = offset)
                val entities = if (shuffle) {
                    response.results.shuffled()
                } else {
                    response.results
                }.map { it.toAgencyEntity() }

                // Clear existing data and insert new data
                withContext(Dispatchers.IO) {
                    dao.deleteAllAgencies()
                    dao.insertAgencies(entities)
                }
            }
        } catch (e: Exception) {
            throw IOException("Failed to refresh agencies: ${e.message}", e)
        }
    }

    override suspend fun refreshAgency(id: Int) {
        if (!isNetworkAvailable()) {
            throw IOException("No internet connection available")
        }

        try {
            val response = api.getAgencyById(id)
            dao.insertAgency(response.toAgencyEntity())
        } catch (e: Exception) {
            throw IOException("Failed to refresh agency: ${e.message}", e)
        }
    }

    override suspend fun getAgencyById(id: Int): Agency? {
        return dao.getAgencyById(id)?.toAgency()
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}