package com.zaripov.mypokedex.di.module

import com.zaripov.mypokedex.database.PokemonDao
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.network.PokeApi
import com.zaripov.mypokedex.network.PokeService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class PokemonDatabaseServiceModule {
    @Provides
    @Singleton
    fun providePokemonDatabaseService(dao: PokemonDao): PokemonDatabaseService {
        return PokemonDatabaseService(dao)
    }
}