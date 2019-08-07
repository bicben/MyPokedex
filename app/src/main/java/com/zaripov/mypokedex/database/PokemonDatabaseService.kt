package com.zaripov.mypokedex.database

import androidx.paging.DataSource
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import timber.log.Timber

open class PokemonDatabaseService(private val dao: PokemonDao) {

    open fun getEntries(query: String): DataSource.Factory<Int, PokeListEntry> {
        return if (query.isBlank())
            dao.getAllEntries()
        else
            dao.getEntries(query)
    }

    open fun getPokemon(entry: Int): Maybe<Pokemon>{
        Timber.i( "queueing a pokemon $entry")
        return dao.getPokemon(entry)
    }

    open fun insertEntries(entries: List<PokeListEntry>): Completable{
        Timber.i("Inserting entries...")
        return dao.insertAllEntries(entries)
    }

    open fun insertPokemon(pokemon: Pokemon): Completable{
        Timber.i( "inserting a pokemon: $pokemon")
        return dao.insertPokemon(pokemon)
    }

    fun deletePokemons(): Completable{
        Timber.i("DROPPING THE POKEMON TABLE!")
        return dao.deleteAllPokemons()
    }

    fun deleteEntries(): Completable{
        Timber.i("DROPPING THE ENTRIES TABLE!")
        return dao.deleteAllEntries()
    }
}