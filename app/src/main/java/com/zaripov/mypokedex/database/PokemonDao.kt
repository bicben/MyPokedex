package com.zaripov.mypokedex.database

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface PokemonDao {

    @Query("select * from PokeListEntry")
    fun getAllEntries(): DataSource.Factory<Int, PokeListEntry>

    @Query("select * from PokeListEntry where entryNum = :query or name like '%' || :query || '%'")
    fun getEntries(query: String): DataSource.Factory<Int, PokeListEntry>

    @Query("select * from Pokemon where entryNum = :entry")
    fun getPokemon(entry: Int): Maybe<Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEntries(entries: List<PokeListEntry>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemon(pokemon: Pokemon): Completable

    @Query("delete from PokeListEntry")
    fun deleteAllEntries(): Completable

    @Query("delete from Pokemon")
    fun deleteAllPokemons(): Completable

}