package com.zaripov.mypokedex.presentation.presenter

import android.util.Log
import androidx.paging.PagedList
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.presentation.view.PokeListView
import com.zaripov.mypokedex.utils.PokeApp
import com.zaripov.mypokedex.utils.PokemonRepository
import com.zaripov.mypokedex.utils.applySchedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
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
        Timber.i("on first view attach")
        firstEntryLoad()
    }

    fun getEntryList(query: String): Observable<PagedList<PokeListEntry>> {
        return pokemonRepository.getEntries(query)
    }

    /**
     * To check if entries BD is empty and load the list
     */
    internal fun firstEntryLoad() {
        Timber.i("first entry load")

        disposables.add(
            pokemonRepository.getEntries("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        Timber.i("on load finish. Items loaded: ${it.size}")
                        viewState.setEntries(it)
                        viewState.onFinishLoading(false)
                        viewState.searchFieldEnabled(true)
                    } else{
                        Timber.i("Entry DB is empty, Fetching is needed")
                        fetchEntries()
                    }
                },
                    {
                        Timber.e(it)
                        viewState.showError(it.toString())
                    })
        )
    }

    private fun fetchEntries(){
        disposables.add(pokemonRepository.fetchAndCacheEntries()
            .subscribe({
                Timber.i("Entries have been fetched and stored in DB")
            },{
                Timber.e(it)
            })
        )
    }

    fun nukePokeDatabase() {
        viewState.onStartLoading()

        disposables.add(
            pokemonRepository.deletePokemons()
                .applySchedulers()
                .subscribe({
                    Timber.i("Pokemons were deleted successfully!")
                    viewState.onFinishLoading(false)
                }, {
                    Timber.e(it)
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
                    Timber.i("Entries were deleted successfully!")
                }, {
                    Timber.e(it)
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
