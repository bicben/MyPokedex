package com.zaripov.mypokedex.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.zaripov.mypokedex.network.PokeService
import com.zaripov.mypokedex.testutils.Helpers
import com.zaripov.mypokedex.testutils.SchedulersRule
import com.zaripov.mypokedex.utils.PokemonRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = SchedulersRule()

    private val dbService = mock<PokemonDatabaseService> {
        on { getEntries(anyString()) } doReturn Single.just(Helpers.testEntries1)
        on { getPokemon(anyInt()) } doReturn Maybe.just(Helpers.testPoke1)

        on { insertEntries(anyList()) } doReturn Completable.complete()
        on { insertPokemon(any()) } doReturn Completable.complete()
    }

    private val apiService = mock<PokeService> {
        on { getPokemonEntries() } doReturn Single.just(Helpers.testPokeEntryWrapper)
        on { getPokemon(anyString()) } doReturn Single.just(Helpers.testPoke2)
    }

    private lateinit var repo: PokemonRepository

    @Before
    fun setUp() {
        repo = PokemonRepository(dbService, apiService)
    }

    @Test
    fun initEntriesReturnsResultFromApiDueToEmptyDb() {
        whenever(dbService.getEntries(anyString())).thenReturn(Single.just(listOf()))

        assert(repo.fetchEntriesIfNeeded().blockingGet()[0] == Helpers.testEntries2[0])

        verify(dbService).getEntries(anyString())
        verify(apiService).getPokemonEntries()
        verify(dbService).insertEntries(anyList())
    }

    @Test
    fun initEntriesReturnsResultFromDbAndNotFromApi() {
        assert(repo.fetchEntriesIfNeeded().blockingGet()[0] == Helpers.testEntries1[0])

        verify(dbService).getEntries(anyString())
        verify(apiService).getPokemonEntries()
        verify(dbService, never()).insertEntries(anyList())
    }

    @Test
    fun getPokemonReturnsResultFromApiDueToEmptyDb() {
        whenever(dbService.getPokemon(anyInt())).doReturn(Maybe.empty())

        assert(repo.getPokemon(1).blockingGet() == Helpers.testPoke2)

        verify(dbService).getPokemon(anyInt())
        verify(apiService).getPokemon(anyString())
        verify(dbService).insertPokemon(any())
    }

    @Test
    fun getPokemonReturnsResultFromDbAndNotApi() {
        assert(repo.getPokemon(1).blockingGet() == Helpers.testPoke1)

        verify(dbService).getPokemon(anyInt())
        verify(apiService).getPokemon(anyString())
        verify(dbService, never()).insertPokemon(any())
    }

    /**
     * Returns Mockito.any() as nullable type to avoid java.lang.IllegalStateException when
     * null is returned.
     */
    private fun <T> any(): T = Mockito.any()
}