package com.zaripov.mypokedex.network

import android.util.Log
import com.zaripov.mypokedex.database.PokemonDatabaseService
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Single

class PokeService(private val pokeApi: PokeApi) {

    fun getPokemonEntries(): Single<PokeListEntryWrapper> {
        Log.i("PokeService", "fetching entries...")
        return pokeApi.getPokemonList()
    }

    fun getPokemon(entry: String): Single<Pokemon> {
        Log.i("PokeService", "fetching a pokemon $entry")
        return pokeApi.getPokemon(entry)
    }






    fun getText(): Single<String>{
        Log.i("PokeService", "text1...")
        return pokeApi.getText()
    }

    fun getText2(query: String): Single<String> {
        Log.i("PokeService", "text2...")
        return pokeApi.getPokemonText(query)
    }
}