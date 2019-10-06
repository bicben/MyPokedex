package com.zaripov.mypokedex.presentation.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.zaripov.mypokedex.presentation.view.PokeDetailsView
import com.zaripov.mypokedex.utils.PokeApp
import com.zaripov.mypokedex.utils.PokemonRepository
import com.zaripov.mypokedex.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@InjectViewState
class PokeDetailsPresenter : MvpPresenter<PokeDetailsView>() {

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private val disposables = CompositeDisposable()

    init {
        PokeApp.getAppComponent().inject(this)
    }

    companion object {
        const val TAG = "PokeDetailsPresenter"
    }

    fun loadPokemon(entry: Int) {
        Log.i(TAG, "loading a pokemon with entry: $entry ...")
        disposables.add(pokemonRepository.getPokemon(entry)
            .applySchedulers()
            .subscribe ({ pokemon ->
                Log.i(TAG, "a pokemon loaded: $pokemon")
                viewState.bindPokemon(pokemon)
            },{
                viewState.displayError(it)
            })
        )
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    fun cancelError() {
        viewState.cancelError()
    }
}
