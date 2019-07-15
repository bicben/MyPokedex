package com.zaripov.mypokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.model.PokemonType

@Database(entities = [PokeListEntry::class, Pokemon::class], version = 1)
@TypeConverters(MyConverters::class)
abstract class PokemonDataBase : RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}

class MyConverters{
    @TypeConverter
    fun fromTypeToString(value: PokemonType?): String?{
        return value?.let { it.type }
    }

    @TypeConverter
    fun fromStringToType(value: String?): PokemonType?{
        return value?.let { PokemonType.valueOf(it.toUpperCase()) }
    }
}