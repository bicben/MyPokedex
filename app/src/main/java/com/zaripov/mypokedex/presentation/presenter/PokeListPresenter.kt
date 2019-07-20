package com.zaripov.mypokedex.presentation.presenter

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.zaripov.mypokedex.presentation.view.PokeListView
import com.zaripov.mypokedex.utils.PokeApp
import com.zaripov.mypokedex.utils.PokemonRepository
import com.zaripov.mypokedex.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@InjectViewState
class PokeListPresenter : MvpPresenter<PokeListView>() {

    @Inject
    lateinit var pokemonRepository: PokemonRepository
    private val disposables = CompositeDisposable()

    init {
        PokeApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.i("PokeListPresenter", "on first view attach")
        firstEntryLoad()
        //loadText()
    }

    /**
     * To check if entries BD is empty and load the list
     */
    internal fun firstEntryLoad() {
        Log.i("PokeListPresenter", "first entry load")
        viewState.onStartLoading()

        disposables.add(
            pokemonRepository.initEntries()
                .applySchedulers()
                .subscribe({
                    Log.i("PokeListPresenter", "on load finish. Items loaded: ${it.size}")
                    viewState.searchFieldEnabled(true)
                    viewState.setEntries(it)
                    viewState.onFinishLoading(it.isEmpty())
                },
                    {
                        Log.e("PokeListPresenter", it.toString())
                        viewState.showError(it.toString())
                    })
        )
    }

    fun queryEntries(query: String) {
        Log.i("PokeListPresenter", "loading entries for $query ...")
        viewState.onStartLoading()

        disposables.add(
            pokemonRepository.getEntries(query)
                .applySchedulers()
                .subscribe(
                    {
                        Log.i("PokeListPresenter", "on load finish. Items loaded: ${it.size}")
                        viewState.setEntries(it)
                        viewState.onFinishLoading(it.isEmpty())
                    }, {
                        Log.e("PokeListPresenter", it.toString())
                        viewState.showError(it.toString())
                    }
                )
        )
    }

    fun nukePokeDatabase() {
        viewState.onStartLoading()

        disposables.add(
            pokemonRepository.deletePokemons()
                .applySchedulers()
                .subscribe({
                    Log.i("PokeListPresenter", "Pokemons were deleted successfully!")
                    viewState.onFinishLoading(false)
                }, {
                    Log.e("PokeListPresenter", it.toString())
                    viewState.showError(it.toString())
                })
        )
    }

    fun nukeEntryDatabase() {
        viewState.onStartLoading()
        viewState.searchFieldEnabled(false)

        disposables.add(
            pokemonRepository.deleteEntries()
                .applySchedulers()
                .subscribe({
                    Log.i("PokeListPresenter", "Entries were deleted successfully!")
                    firstEntryLoad()

                }, {
                    Log.e("PokeListPresenter", it.toString())
                    viewState.showError(it.toString())
                })
        )
    }

    fun cancelError() {
        viewState.cancelError()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
