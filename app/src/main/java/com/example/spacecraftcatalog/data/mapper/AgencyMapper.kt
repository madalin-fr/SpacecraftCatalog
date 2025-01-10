// data/mapper/AgencyMapper.kt
package com.example.spacecraftcatalog.data.mapper

import com.example.spacecraftcatalog.data.db.AgencyEntity
import com.example.spacecraftcatalog.data.model.AgencyDto
import com.example.spacecraftcatalog.domain.model.Agency

fun AgencyDto.toAgencyEntity() = AgencyEntity(
    id = id,
    name = name,
    description = description,
    imageUrl = image?.imageUrl,  // Handle nested nullability
    foundingYear = foundingYear,
    administrator = administrator,
    url = url
)

fun AgencyEntity.toAgency() = Agency(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = imageUrl,
    foundingYear = foundingYear,
    administrator = administrator,
    url = url
)

fun AgencyDto.toAgency() = Agency(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = image?.imageUrl,
    foundingYear = foundingYear,
    administrator = administrator,
    url = url
)