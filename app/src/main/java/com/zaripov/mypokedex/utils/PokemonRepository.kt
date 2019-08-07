package com.zaripov.mypokedex.utils

import androidx.paging.PagedList
import androidx.paging.toObservable
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.network.PokeService
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

open class PokemonRepository(
    private val pokeDatabaseService: PokemonDatabaseService,
    private val pokeApiService: PokeService
) {

    companion object {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10)
            .build()
    }

    open fun getEntries(query: String): Observable<PagedList<PokeListEntry>> {
        return pokeDatabaseService.getEntries(query).toObservable(config)
    }

    open fun getPokemon(query: Int): Single<Pokemon> {
        Timber.i("Requesting a pokemon: $query")
        return Maybe.concat(pokeDatabaseService.getPokemon(query), fetchAndCachePokemon(query))
            .firstElement()
            .toSingle()
    }

    fun fetchAndCacheEntries(): Completable {
        return pokeApiService.getPokemonEntries()
            .flatMapCompletable {
                Timber.i("Parsing entries...")
                pokeDatabaseService.insertEntries(it.entryList)
            }
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