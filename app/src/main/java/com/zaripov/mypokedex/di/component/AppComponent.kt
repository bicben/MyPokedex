package com.zaripov.mypokedex.di.component

import android.content.Context
import com.zaripov.mypokedex.di.module.ContextModule
import com.zaripov.mypokedex.di.module.PokemonServiceModule
import com.zaripov.mypokedex.di.module.RepositoryModule
import com.zaripov.mypokedex.di.module.RepositoryModule_ProvideRepositoryFactory
import com.zaripov.mypokedex.presentation.presenter.PokeDetailsPresenter
import com.zaripov.mypokedex.presentation.presenter.PokeListPresenter
import com.zaripov.mypokedex.utils.PokemonRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RepositoryModule::class])
interface AppComponent {
    fun getContext(): Context

    fun inject(pokeDetailsPresenter: PokeDetailsPresenter)
    fun inject(pokeListPresenter: PokeListPresenter)
}