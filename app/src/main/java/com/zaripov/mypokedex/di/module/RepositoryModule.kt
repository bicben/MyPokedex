package com.zaripov.mypokedex.di.module

import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.network.PokeService
import com.zaripov.mypokedex.utils.PokemonRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [PokemonDatabaseServiceModule::class, PokemonServiceModule::class])
class RepositoryModule{

    @Provides
    @Singleton
    fun provideRepository(dbServiceModule: PokemonDatabaseService, service: PokeService): PokemonRepository{
        return PokemonRepository(dbServiceModule, service)
    }
}