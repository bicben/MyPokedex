package com.zaripov.mypokedex.testutils

import androidx.paging.PagedList
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.model.PokemonType
import com.zaripov.mypokedex.testutils.testpaging.TestDataSource
import com.zaripov.mypokedex.testutils.testpaging.TestDataSourceFactory
import com.zaripov.mypokedex.testutils.testpaging.TestEntryProvider
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class Helpers {
    companion object{
        val testPoke1 = Pokemon(1,
            "Shiny Bulbasaur",
            7,
            69,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
            PokemonType.POISON,
            PokemonType.GRASS)

        val testPoke2 = Pokemon(25,
            "Shiny Pikachu",
            4,
            60,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/female/25.png",
            PokemonType.ELECTRIC,
            PokemonType.NO)

        val testEntries1 = listOf(
            PokeListEntry(7, "Shiny Squirtle"),
            PokeListEntry(4, "Shiny Charmander"))

        val testEntries2 = listOf(
            PokeListEntry(54, "Shiny Psyduck"),
            PokeListEntry(79, "Shiny Slowpoke"),
            PokeListEntry(258, "Shiny Mudkip")
        )

        val testPokeEntryWrapper = PokeListEntryWrapper(testEntries2)

        val testConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .setInitialLoadSizeHint(5)
            .build()

        fun getTestPagedList(): PagedList<PokeListEntry>{
            val dataSource = TestDataSource(TestEntryProvider(testEntries1 + testEntries2))
            return PagedList.Builder(dataSource, testConfig)
               .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor {
                    runBlocking { it.run() }
                }
                .build()
        }

        fun gettestFactoryEmpty(): TestDataSourceFactory{
            return TestDataSourceFactory(TestEntryProvider(listOf()))
        }
    }
}