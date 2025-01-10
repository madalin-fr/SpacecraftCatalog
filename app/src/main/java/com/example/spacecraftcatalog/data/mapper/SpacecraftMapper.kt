// data/mapper/SpacecraftMapper.kt
package com.example.spacecraftcatalog.data.mapper

import com.example.spacecraftcatalog.data.db.SpacecraftEntity
import com.example.spacecraftcatalog.data.model.SpacecraftDto
import com.example.spacecraftcatalog.data.model.SpacecraftStatus as DataSpacecraftStatus
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus as DomainSpacecraftStatus

fun SpacecraftDto.toSpacecraftEntity() = SpacecraftEntity(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description,
    imageUrl = imageUrl,
    agencyId = agency?.id,
    status = status.toDomainStatus().name
)

fun SpacecraftEntity.toSpacecraft() = Spacecraft(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description ?: "",
    imageUrl = imageUrl,
    agency = null, // Would need to be populated separately if needed
    status = try {
        DomainSpacecraftStatus.valueOf(status)
    } catch (e: IllegalArgumentException) {
        DomainSpacecraftStatus.UNKNOWN
    }
)

fun SpacecraftDto.toSpacecraft() = Spacecraft(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description ?: "",
    imageUrl = imageUrl,
    agency = agency?.toAgency(),
    status = status.toDomainStatus()
)

private fun DataSpacecraftStatus.toDomainStatus(): DomainSpacecraftStatus {
    return when (this) {
        DataSpacecraftStatus.ACTIVE -> DomainSpacecraftStatus.ACTIVE
        DataSpacecraftStatus.RETIRED -> DomainSpacecraftStatus.RETIRED
        DataSpacecraftStatus.IN_DEVELOPMENT -> DomainSpacecraftStatus.IN_DEVELOPMENT
        DataSpacecraftStatus.UNKNOWN -> DomainSpacecraftStatus.UNKNOWN
    }
}

private fun DomainSpacecraftStatus.toDataStatus(): DataSpacecraftStatus {
    return when (this) {
        DomainSpacecraftStatus.ACTIVE -> DataSpacecraftStatus.ACTIVE
        DomainSpacecraftStatus.RETIRED -> DataSpacecraftStatus.RETIRED
        DomainSpacecraftStatus.IN_DEVELOPMENT -> DataSpacecraftStatus.IN_DEVELOPMENT
        DomainSpacecraftStatus.UNKNOWN -> DataSpacecraftStatus.UNKNOWN
    }
}