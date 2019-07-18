package com.zaripov.mypokedex.presentation.presenter

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.presentation.view.`PokeListView$$State`
import com.zaripov.mypokedex.testutils.Helpers
import com.zaripov.mypokedex.testutils.SchedulersRule
import com.zaripov.mypokedex.testutils.TestComponent
import com.zaripov.mypokedex.testutils.TestComponentRule
import com.zaripov.mypokedex.utils.PokemonRepository
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableEmpty
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class PokeListPresenterTest {
    @Rule
    @JvmField
    val testComponentRule = TestComponentRule(testAppComponent())

    @Rule
    @JvmField
    val schedulers = SchedulersRule()

    val pokeRepo = mock<PokemonRepository> {
        on { initEntries() } doReturn Single.just(Helpers.testEntries1)
        on { getEntries(anyString()) } doReturn Single.just(Helpers.testEntries1)
        on { deleteEntries() } doReturn CompletableEmpty.INSTANCE
        on { deletePokemons() } doReturn CompletableEmpty.INSTANCE
    }

    private val viewState: `PokeListView$$State` = mock()

    private lateinit var presenter: PokeListPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = PokeListPresenter()
        presenter.setViewState(viewState)
    }


    @Test
    fun onCancelErrorShouldClose() {
        presenter.cancelError()
        verify(viewState).cancelError()
    }

    @Test
    fun onFirstEntryLoadSuccess() {
        presenter.firstEntryLoad()

        verify(viewState).onStartLoading()
        verify(viewState, never()).showError(anyString())
        verify(viewState).searchFieldEnabled(anyBoolean())
        verify(viewState).setEntries(anyList())
    }

    @Test
    fun onQueryEntries(){
        presenter.queryEntries("blah")

        verify(viewState).onStartLoading()
        verify(viewState, never()).showError(anyString())
        verify(viewState).setEntries(anyList())
    }

    @Test
    fun onNukingEntries(){
        presenter.nukeEntryDatabase()

        verify(viewState, times(2)).onStartLoading()
        verify(viewState, times(2)).searchFieldEnabled(anyBoolean())
        verify(viewState).setEntries(anyList())
        verify(viewState, never()).showError(anyString())
    }

    @Test
    fun onNukingPokemons(){
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