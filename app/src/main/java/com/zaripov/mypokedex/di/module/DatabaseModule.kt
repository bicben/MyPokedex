package com.zaripov.mypokedex.di.module

import android.content.Context
import androidx.room.Room
import com.zaripov.mypokedex.database.PokemonDao
import com.zaripov.mypokedex.database.PokemonDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(db: PokemonDataBase): PokemonDao {
        return db.pokemonDao
    }

    @Provides
    @Singleton
    fun providePokemonDataBase(context: Context): PokemonDataBase {
        return Room.databaseBuilder(context.applicationContext, PokemonDataBase::class.java, "pokemons").build()
    }
}