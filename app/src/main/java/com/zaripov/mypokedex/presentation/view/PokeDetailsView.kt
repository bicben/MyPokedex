package com.zaripov.mypokedex.presentation.view

import com.arellomobile.mvp.MvpView
import com.zaripov.mypokedex.model.Pokemon

interface PokeDetailsView : MvpView {
    fun bindPokemon(pokemon: Pokemon)
    fun displayError(throwable: Throwable)
    fun cancelError()
}
