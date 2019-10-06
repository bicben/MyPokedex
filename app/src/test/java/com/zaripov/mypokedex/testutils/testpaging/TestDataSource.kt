package com.zaripov.mypokedex.testutils.testpaging

import androidx.paging.PositionalDataSource
import com.zaripov.mypokedex.model.PokeListEntry

class TestDataSource(private val entryProvider: TestEntryProvider) : PositionalDataSource<PokeListEntry>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<PokeListEntry>) {
        val result = entryProvider.getList(params.startPosition, params.loadSize)
        callback.onResult(result)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<PokeListEntry>) {
        val result = entryProvider.getList(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(result, 0)
    }
}