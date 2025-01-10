// di/RepositoryModule.kt
package com.example.spacecraftcatalog.di

import com.example.spacecraftcatalog.data.repository.AgencyRepositoryImpl
import com.example.spacecraftcatalog.data.repository.SpacecraftRepositoryImpl
import com.example.spacecraftcatalog.domain.repository.AgencyRepository
import com.example.spacecraftcatalog.domain.repository.SpacecraftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAgencyRepository(
        repositoryImpl: AgencyRepositoryImpl
    ): AgencyRepository

    @Binds
    @Singleton
    abstract fun bindSpacecraftRepository(
        repositoryImpl: SpacecraftRepositoryImpl
    ): SpacecraftRepository
}