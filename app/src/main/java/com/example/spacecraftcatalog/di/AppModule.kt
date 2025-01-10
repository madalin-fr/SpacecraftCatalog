// di/AppModule.kt
package com.example.spacecraftcatalog.di

import android.content.Context
import androidx.room.Room
import com.example.spacecraftcatalog.data.api.SpaceAgencyApi
import com.example.spacecraftcatalog.data.db.SpaceCatalogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideSpaceAgencyApi(client: OkHttpClient): SpaceAgencyApi {
        return Retrofit.Builder()
            .baseUrl("https://lldev.thespacedevs.com/2.3.0/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceAgencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SpaceCatalogDatabase {
        return Room.databaseBuilder(
            context,
            SpaceCatalogDatabase::class.java,
            "space_catalog_db"
        ).build()
    }

    @Provides
    fun provideAgencyDao(database: SpaceCatalogDatabase) = database.agencyDao()

    @Provides
    fun provideSpacecraftDao(database: SpaceCatalogDatabase) = database.spacecraftDao()
}