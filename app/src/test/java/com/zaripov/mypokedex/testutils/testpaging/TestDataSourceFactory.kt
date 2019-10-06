package com.zaripov.mypokedex.testutils.testpaging

import androidx.paging.DataSource
import com.zaripov.mypokedex.model.PokeListEntry

data class TestDataSourceFactory(private val provider: TestEntryProvider) : DataSource.Factory<Int, PokeListEntry>() {
    override fun create(): DataSource<Int, PokeListEntry> {
        return TestDataSource(provider)
    }
}