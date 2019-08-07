package com.zaripov.mypokedex.network

import android.util.Log
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Single
import timber.log.Timber

open class PokeService(private val pokeApi: PokeApi) {

    open fun getPokemonEntries(): Single<PokeListEntryWrapper> {
        Timber.i("fetching entries...")
        return pokeApi.getPokemonList()
    }

    open fun getPokemon(entry: String): Single<Pokemon> {
        Timber.i( "fetching a pokemon $entry")
        return pokeApi.getPokemon(entry)
    }
}