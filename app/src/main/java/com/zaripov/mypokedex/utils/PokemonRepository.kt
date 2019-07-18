package com.zaripov.mypokedex.utils

import android.util.Log
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.network.PokeService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

open class PokemonRepository(
    private val pokeDatabaseService: PokemonDatabaseService,
    private val pokeApiService: PokeService
) {

    companion object {
        const val TAG = "PokemonRepository"
    }

    open fun initEntries(): Single<List<PokeListEntry>> {
        return Single.concat(pokeDatabaseService.getEntries(""), fetchAndCacheEntries())
            .filter { it.isNotEmpty() }
            .firstElement()
            .toSingle()
    }

    open fun getEntries(query: String): Single<List<PokeListEntry>> {
        return pokeDatabaseService.getEntries(query)
    }

    open fun getPokemon(query: Int): Single<Pokemon> {
        Log.i(TAG, "Requesting a pokemon: $query")
        return Maybe.concat(pokeDatabaseService.getPokemon(query), fetchAndCachePokemon(query))
            .firstElement()
            .toSingle()
    }

    private fun fetchAndCacheEntries(): Single<List<PokeListEntry>> {
        val fetch = pokeApiService.getPokemonEntries().map { it.entryList }.cache()

        return fetch.flatMapCompletable {
            pokeDatabaseService.insertEntries(it)
        }
            .andThen(fetch)
    }

    private fun fetchAndCachePokemon(query: Int): Maybe<Pokemon> {
        val fetch = pokeApiService.getPokemon(query.toString()).cache()

        return fetch
            .flatMapCompletable {
                pokeDatabaseService.insertPokemon(it)
            }
            .andThen(fetch.toMaybe())
    }

    open fun deletePokemons(): Completable {
        return pokeDatabaseService.deletePokemons()
    }

    open fun deleteEntries(): Completable {
        return pokeDatabaseService.deleteEntries()
    }
}