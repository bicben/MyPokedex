package com.zaripov.mypokedex.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.zaripov.mypokedex.testutils.Helpers
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataBaseTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: PokemonDataBase
    private lateinit var dao: PokemonDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PokemonDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = db.pokemonDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getEntriesWhenNoEntriesInserted() {
        dao.getAllEntries()
            .test()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun getPokemonWhenThereIsNone() {
        dao.getPokemon(1)
            .test()
            .assertNoValues()
    }

    @Test
    fun insertPokemonAndQueryIt() {
        dao.insertPokemon(Helpers.testPoke1).blockingAwait()

        dao.getPokemon(Helpers.testPoke1.entryNum)
            .test()
            .assertValue {
                it == Helpers.testPoke1
            }
    }

    @Test
    fun insertEntryAndQueryItByName() {
        dao.insertAllEntries(Helpers.testEntries1).blockingAwait()

        dao.getEntries(Helpers.testEntries1[0].name)
            .test()
            .assertValue {
                it[0] == Helpers.testEntries1[0] &&
                        it.size == Helpers.testEntries1.size
            }
    }

    @Test
    fun deleteAllPokemons() {
        dao.insertPokemon(Helpers.testPoke1).blockingAwait()

        dao.deleteAllPokemons().blockingAwait()
        dao.getPokemon(Helpers.testPoke1.entryNum)
            .test()
            .assertNoValues()
    }

    @Test
    fun deleteAllEntries() {
        dao.insertAllEntries(Helpers.testEntries1).blockingAwait()

        dao.deleteAllEntries().blockingAwait()
        dao.getAllEntries()
            .test()
            .assertValue { it.isEmpty() }
    }
}