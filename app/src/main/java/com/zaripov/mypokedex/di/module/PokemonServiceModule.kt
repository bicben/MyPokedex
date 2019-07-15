package com.zaripov.mypokedex.di.module

import com.zaripov.mypokedex.network.PokeApi
import com.zaripov.mypokedex.network.PokeService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class PokemonServiceModule {
    @Provides
    @Singleton
    fun providePokemonService(pokeApi: PokeApi): PokeService {
        return PokeService(pokeApi)
    }
}