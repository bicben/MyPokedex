package com.zaripov.mypokedex.testutils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.presentation.presenter.PokeDetailsPresenter
import com.zaripov.mypokedex.presentation.presenter.PokeListPresenter

open class TestComponent : AppComponent {

    override fun getContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }

    override fun inject(pokeListPresenter: PokeListPresenter) {

    }

    override fun inject(pokeDetailsPresenter: PokeDetailsPresenter) {

    }
}