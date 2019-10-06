package com.zaripov.mypokedex.presentation.presenter

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.presentation.view.`PokeDetailsView$$State`
import com.zaripov.mypokedex.testutils.Helpers
import com.zaripov.mypokedex.testutils.SchedulersRule
import com.zaripov.mypokedex.testutils.TestComponent
import com.zaripov.mypokedex.testutils.TestComponentRule
import com.zaripov.mypokedex.utils.PokemonRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PokeDetailsPresenterTest {

    @Rule
    @JvmField
    val testComponentRule = TestComponentRule(testAppComponent())

    @Rule
    @JvmField
    val schedulers = SchedulersRule()

    val pokeRepo = mock<PokemonRepository> {
        on {getPokemon(anyInt())} doReturn Single.just(Helpers.testPoke1)
    }

    private val viewState: `PokeDetailsView$$State` = mock()
    private lateinit var presenter: PokeDetailsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = PokeDetailsPresenter()
        presenter.setViewState(viewState)
    }

    @Test
    fun loadPokemon() {
        presenter.loadPokemon(ArgumentMatchers.anyInt())

        verify(viewState).bindPokemon(ArgumentMatchers.any(Pokemon::class.java))
        verify(viewState, never()).displayError(any(Throwable::class.java))
    }

    @Test
    fun cancelError() {
        presenter.cancelError()
        verify(viewState).cancelError()
    }

    @Test
    fun tet(){

    }

    private fun testAppComponent(): AppComponent {
        return object : TestComponent() {
            override fun inject(pokeDetailsPresenter: PokeDetailsPresenter) {
                pokeDetailsPresenter.pokemonRepository = pokeRepo
            }
        }
    }
}