// data/mapper/SpacecraftMapper.kt
package com.example.spacecraftcatalog.data.mapper

import android.util.Log
import com.example.spacecraftcatalog.data.db.SpacecraftEntity
import com.example.spacecraftcatalog.data.model.SpacecraftDto
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus
import com.example.spacecraftcatalog.data.model.SpacecraftStatusDto
import com.example.spacecraftcatalog.domain.model.Agency
import com.example.spacecraftcatalog.data.model.SpacecraftStatus as DataSpacecraftStatus
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus as DomainSpacecraftStatus

fun SpacecraftDto.toSpacecraftEntity(requestedAgencyId: Int? = null): SpacecraftEntity {
    val resolvedAgencyId = spacecraftConfig.agency?.id ?: requestedAgencyId

    return SpacecraftEntity(
        id = id,
        name = name,
        serialNumber = serialNumber,
        description = description ?: "",
        imageUrl = image?.imageUrl,
        agencyId = resolvedAgencyId,
        status = convertStatusToString(status)
    )
}


fun SpacecraftEntity.toSpacecraft(agency: Agency? = null) = Spacecraft(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description ?: "",
    imageUrl = imageUrl,
    agency = agency,
    status = convertStringToStatus(status)
)

private fun SpacecraftStatusDto.toDomainStatus(): DomainSpacecraftStatus {
    return when (name.uppercase()) {
        "ACTIVE" -> DomainSpacecraftStatus.ACTIVE
        "RETIRED" -> DomainSpacecraftStatus.RETIRED
        "IN DEVELOPMENT" -> DomainSpacecraftStatus.IN_DEVELOPMENT
        else -> DomainSpacecraftStatus.UNKNOWN
    }
}

fun SpacecraftDto.toSpacecraft() = Spacecraft(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description ?: "",
    imageUrl = image?.imageUrl,
    agency = spacecraftConfig.agency?.toAgency(),
    status = convertApiStatusToDomain(status)
)

private fun convertStatusToString(status: SpacecraftStatusDto): String {
    return when (status.name.uppercase()) {
        "ACTIVE" -> SpacecraftStatus.ACTIVE.name
        "RETIRED" -> SpacecraftStatus.RETIRED.name
        "IN DEVELOPMENT" -> SpacecraftStatus.IN_DEVELOPMENT.name
        else -> SpacecraftStatus.UNKNOWN.name
    }
}

private fun convertStringToStatus(status: String): SpacecraftStatus {
    return try {
        SpacecraftStatus.valueOf(status)
    } catch (e: IllegalArgumentException) {
        SpacecraftStatus.UNKNOWN
    }
}

private fun convertApiStatusToDomain(status: SpacecraftStatusDto): SpacecraftStatus {
    return when (status.name.uppercase()) {
        "ACTIVE" -> SpacecraftStatus.ACTIVE
        "RETIRED" -> SpacecraftStatus.RETIRED
        "IN DEVELOPMENT" -> SpacecraftStatus.IN_DEVELOPMENT
        else -> SpacecraftStatus.UNKNOWN
    }
}