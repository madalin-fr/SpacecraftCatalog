// data/mapper/SpacecraftMapper.kt
package com.example.spacecraftcatalog.data.mapper

import android.util.Log
import com.example.spacecraftcatalog.data.db.SpacecraftEntity
import com.example.spacecraftcatalog.data.model.SpacecraftDto
import com.example.spacecraftcatalog.data.model.SpacecraftStatus
import com.example.spacecraftcatalog.data.model.SpacecraftStatusDto
import com.example.spacecraftcatalog.data.model.SpacecraftStatus as DataSpacecraftStatus
import com.example.spacecraftcatalog.domain.model.Spacecraft
import com.example.spacecraftcatalog.domain.model.SpacecraftStatus as DomainSpacecraftStatus

fun SpacecraftDto.toSpacecraftEntity(requestedAgencyId: Int? = null): SpacecraftEntity {
    // Use either the agency from DTO or the requested agency ID
    val resolvedAgencyId = agency?.id ?: requestedAgencyId

    if (resolvedAgencyId == null) {
        Log.e("SpacecraftMapper", "Cannot resolve agency ID for spacecraft: $name")
    }

    return SpacecraftEntity(
        id = id,
        name = name,
        serialNumber = serialNumber,
        description = description ?: "",
        imageUrl = imageUrl,
        agencyId = resolvedAgencyId,
        status = status.name.let { SpacecraftStatus.fromApiName(it).name }
    )
}



fun SpacecraftEntity.toSpacecraft() = Spacecraft(
    id = id,
    name = name,
    serialNumber = serialNumber,
    description = description ?: "",
    imageUrl = imageUrl,
    agency = null,
    status = try {
        DomainSpacecraftStatus.valueOf(status)
    } catch (e: IllegalArgumentException) {
        DomainSpacecraftStatus.UNKNOWN
    }
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
    imageUrl = imageUrl,
    agency = agency?.toAgency(),
    status = SpacecraftStatus.fromApiName(status.name).toDomainStatus()
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