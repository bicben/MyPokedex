package com.zaripov.mypokedex.presentation.view

import androidx.paging.PagedList
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.zaripov.mypokedex.model.PokeListEntry

@StateStrategyType(AddToEndSingleStrategy::class)
interface PokeListView : MvpView {
    fun onStartLoading()
    fun onFinishLoading(emptyList: Boolean)
    fun showError(error: String)
    fun setEntries(entries: PagedList<PokeListEntry>)
    fun searchFieldEnabled(enabled: Boolean)
    fun cancelError()
}
