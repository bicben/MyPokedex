package com.zaripov.mypokedex.database

import android.util.Log
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

open class PokemonDatabaseService(private val dao: PokemonDao) {
    companion object{
        const val TAG = "PokemonDatabaseService"
    }

    open fun getEntries(query: String): Single<List<PokeListEntry>> {
        return if (query.isBlank())
            dao.getAllEntries()
        else
            dao.getEntries(query)
    }

    open fun getPokemon(entry: Int): Maybe<Pokemon>{
        Log.i(TAG, "queueing a pokemon $entry")
        return dao.getPokemon(entry)
    }

    open fun insertEntries(entries: List<PokeListEntry>): Completable{
        Log.i(TAG, "Inserting entries...")
        return dao.insertAllEntries(entries)
    }

    open fun insertPokemon(pokemon: Pokemon): Completable{
        Log.i(TAG, "inserting a pokemon: $pokemon")
        return dao.insertPokemon(pokemon)
    }

    fun deletePokemons(): Completable{
        Log.i(TAG, "DROPPING THE POKEMON TABLE!")
        return dao.deleteAllPokemons()
    }

    fun deleteEntries(): Completable{
        Log.i(TAG, "DROPPING THE ENTRIES TABLE!")
        return dao.deleteAllEntries()
    }
}