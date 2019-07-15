package com.zaripov.mypokedex.network

import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {
    @GET("pokedex/1")
    fun getPokemonList(): Single<PokeListEntryWrapper>

    @GET("pokemon/{entry}")
    fun getPokemon(@Path("entry") entry: String): Single<Pokemon>






    @GET("pokedex/1")
    fun getText(): Single<String>

    @GET("pokemon/{entry}")
    fun getPokemonText(@Path("entry") entry: String): Single<String>
}