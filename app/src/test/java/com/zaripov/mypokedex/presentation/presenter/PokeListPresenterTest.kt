package com.zaripov.mypokedex.presentation.presenter

import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.presentation.view.`PokeListView$$State`
import com.zaripov.mypokedex.testutils.Helpers
import com.zaripov.mypokedex.testutils.SchedulersRule
import com.zaripov.mypokedex.testutils.TestComponent
import com.zaripov.mypokedex.testutils.TestComponentRule
import com.zaripov.mypokedex.utils.PokemonRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableJust
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import org.robolectric.util.Scheduler


@RunWith(RobolectricTestRunner::class)
class PokeListPresenterTest {
    @Rule
    @JvmField
    val testComponentRule = TestComponentRule(testAppComponent())

    @Rule
    @JvmField
    val schedulers = SchedulersRule()

    val pokeRepo = mock<PokemonRepository> {
        on { getEntries(anyString()) } doReturn Observable.just(Helpers.getTestPagedList())
        on { deleteEntries() } doReturn Completable.complete()
        on { deletePokemons() } doReturn Completable.complete()
    }
    private val viewState: `PokeListView$$State` = mock()
    private lateinit var presenter: PokeListPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = PokeListPresenter()
        presenter.setViewState(viewState)
    }

    @After
    fun tearDown(){

    }

    @Test
    fun onCancelErrorShouldClose() {
        presenter.cancelError()
        verify(viewState).cancelError()
    }

    @Test
    fun onFirstEntryLoadSuccess() {

        presenter.firstEntryLoad()

//        Robolectric.flushBackgroundThreadScheduler()
//        Robolectric.getBackgroundThreadScheduler().idleState = Scheduler.IdleState.CONSTANT_IDLE
//        Robolectric.flushForegroundThreadScheduler()
//        Robolectric.getForegroundThreadScheduler().idleState = Scheduler.IdleState.CONSTANT_IDLE

        verify(pokeRepo).getEntries(anyString())
        verify(viewState, never()).showError(anyString())
        val logInfo = ShadowLog.getLogs().last()
        println(logInfo.msg)
        verify(viewState).setEntries(any())
        verify(viewState).onFinishLoading(anyBoolean())
        verify(viewState).searchFieldEnabled(anyBoolean())
        verify(pokeRepo, never()).fetchAndCacheEntries()
    }

    @Test
    fun onFirstEntryLoadEmpty() {
        presenter.firstEntryLoad()

        verify(pokeRepo).getEntries(anyString())
        verify(viewState).showError(anyString())
        verify(viewState).setEntries(any())
        verify(viewState).onFinishLoading(anyBoolean())
        verify(viewState).searchFieldEnabled(anyBoolean())
        verify(pokeRepo).fetchAndCacheEntries()
    }

    @Test
    fun onNukingEntries() {
        presenter.nukeEntryDatabase()

        verify(viewState).onStartLoading()
        verify(viewState).searchFieldEnabled(anyBoolean())
        verify(viewState, never()).showError(anyString())
    }

    @Test
    fun onNukingPokemons() {
        presenter.nukePokeDatabase()

        verify(viewState).onStartLoading()
        verify(viewState).onFinishLoading(anyBoolean())
        verify(viewState, never()).showError(anyString())
    }

    private fun testAppComponent(): AppComponent {
        return object : TestComponent() {
            override fun inject(pokeListPresenter: PokeListPresenter) {
                pokeListPresenter.pokemonRepository = pokeRepo
            }
        }
    }
}