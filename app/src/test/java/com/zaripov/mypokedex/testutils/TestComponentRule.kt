package com.zaripov.mypokedex.testutils

import androidx.annotation.NonNull
import com.zaripov.mypokedex.di.component.AppComponent
import com.zaripov.mypokedex.utils.PokeApp
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestComponentRule : TestRule {

    private var appComponent: AppComponent

    constructor() {
        appComponent = TestComponent()
    }

    constructor(@NonNull component: AppComponent) {
        this.appComponent = component
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                PokeApp.setAppComponent(appComponent)
                base.evaluate()
            }
        }
    }
}