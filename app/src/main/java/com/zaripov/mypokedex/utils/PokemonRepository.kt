package com.zaripov.mypokedex.utils

import android.util.Log
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.network.PokeService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class PokemonRepository(
    private val pokeDatabaseService: PokemonDatabaseService,
    private val pokeApiService: PokeService
) {

    fun initEntries(): Single<List<PokeListEntry>> {
        return Single.concat(pokeDatabaseService.getEntries(""), fetchAndCacheEntries())
            .filter { it.isNotEmpty() }
            .firstElement()
            .toSingle()
    }

    fun getEntries(query: String): Single<List<PokeListEntry>> {
        return pokeDatabaseService.getEntries(query)
    }

    fun getPokemon(query: Int): Single<Pokemon> {
        Log.i("Poke Repo", "getting a pokemon: $query")
        return Maybe.concat(pokeDatabaseService.getPokemon(query), fetchAndCachePokemon(query))
            .firstElement()
            .toSingle()
    }

    private fun fetchAndCacheEntries(): Single<List<PokeListEntry>> {
        val fetch = pokeApiService.getPokemonEntries().map { it.entryList }.cache()

        return fetch.flatMapCompletable {
            Log.i("Poke Repo", "Inserting entries...")
            pokeDatabaseService.insertEntries(it)
        }
            .andThen(fetch)
    }

    private fun fetchAndCachePokemon(query: Int): Maybe<Pokemon> {
        val fetch = pokeApiService.getPokemon(query.toString()).cache()

        return fetch
            .flatMapCompletable {
                Log.i("Poke Repo", "inserting a pokemon: $it")
                pokeDatabaseService.insertPokemon(it)
            }
            .andThen(fetch.toMaybe())
    }

    fun deletePokemons(): Completable{
        Log.i("Poke Repo", "DROPPING THE POKEMON TABLE!")
        return pokeDatabaseService.deletePokemons()
    }

    fun deleteEntries(): Completable{
        Log.i("Poke Repo", "DROPPING THE ENTRIES TABLE!")
        return pokeDatabaseService.deleteEntries()
    }
}