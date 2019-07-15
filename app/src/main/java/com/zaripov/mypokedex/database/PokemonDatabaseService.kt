package com.zaripov.mypokedex.database

import android.util.Log
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class PokemonDatabaseService(private val dao: PokemonDao) {


    fun getEntries(query: String): Single<List<PokeListEntry>> {
        return if (query.isBlank())
            dao.getAllEntries()
        else
            dao.getEntries(query)
    }

    fun getPokemon(entry: Int): Maybe<Pokemon>{
        Log.i("PokeDB", "queueing a pokemon $entry")
        return dao.getPokemon(entry)
    }

    fun insertEntries(entries: List<PokeListEntry>): Completable{
        return dao.insertAllEntries(entries)
    }

    fun insertPokemon(pokemon: Pokemon): Completable{
        return dao.insertPokemon(pokemon)
    }

    fun deletePokemons(): Completable{
        return dao.deleteAllPokemons()
    }

    fun deleteEntries(): Completable{
        return dao.deleteAllEntries()
    }
}