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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
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
                val localData = dao.getAllAgencies().firstOrNull() ?: emptyList()
                val shuffledData = localData.shuffled()
                dao.insertAgencies(shuffledData)
            }
            return
        }

        try {
            // First, get the total count of available agencies
            val initialResponse = api.getAgencies(limit = 1)
            val totalItems = initialResponse.count

            if (shuffle && totalItems > 0) {
                // Calculate how many complete pages of 100 items exist
                val totalPages = (totalItems + 99) / 100

                // Pick a random page, ensuring we don't exceed the total count
                val randomPage = Random.nextInt(totalPages)
                val offset = randomPage * 100

                // Fetch the random page
                val response = api.getAgencies(limit = 100, offset = offset)

                // Shuffle the results before saving
                val shuffledResults = response.results.shuffled()
                dao.insertAgencies(shuffledResults.map { it.toAgencyEntity() })
            } else {
                // If not shuffling, just get the first page
                val response = api.getAgencies(limit = 100, offset = 0)
                dao.insertAgencies(response.results.map { it.toAgencyEntity() })
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