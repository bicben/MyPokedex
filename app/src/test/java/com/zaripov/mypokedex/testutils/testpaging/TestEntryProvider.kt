package com.zaripov.mypokedex.testutils.testpaging

import com.zaripov.mypokedex.model.PokeListEntry

class TestEntryProvider(private val list: List<PokeListEntry>) {

    fun getSize() = list.size

    fun getList(start: Int, size: Int): List<PokeListEntry> {
        return when {
            (start+size < list.size) -> list.subList(start, start + size)
            (start + size > list.size) -> list.subList(start, list.size-1)
            (start > list.size) -> emptyList()
            else -> emptyList()
        }
    }
}