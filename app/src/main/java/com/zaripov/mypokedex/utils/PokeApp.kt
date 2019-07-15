package com.zaripov.mypokedex.utils

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.di.component.DaggerAppComponent
import com.zaripov.mypokedex.di.module.ContextModule
import io.reactivex.plugins.RxJavaPlugins



class PokeApp : Application() {

    companion object {
        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }

        @VisibleForTesting
        fun setAppComponent(@NonNull appComponent: AppComponent) {
            this.appComponent = appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler { throwable -> Log.e("PokeApp", throwable.toString())}

        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }
}