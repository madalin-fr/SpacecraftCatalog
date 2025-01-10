// data/repository/AgencyRepositoryImpl.kt
package com.example.spacecraftcatalog.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.AgencyDao
import com.example.spacecraftcatalog.data.mapper.toAgency
import com.example.spacecraftcatalog.data.mapper.toAgencyEntity
import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

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

    override suspend fun refreshAgencies() {
        if (!isNetworkAvailable()) {
            throw IOException("No internet connection available")
        }

        try {
            val response = api.getAgencies(limit = 100)
            dao.insertAgencies(response.results.map { it.toAgencyEntity() })
        } catch (e: Exception) {
            throw IOException("Failed to refresh agencies: ${e.message}", e)
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