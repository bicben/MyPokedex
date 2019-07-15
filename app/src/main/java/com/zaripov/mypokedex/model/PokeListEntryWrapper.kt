package com.zaripov.mypokedex.model


/**
 * I know it's a shame
 * but I don't want to use Jackson just for this.
 */
data class PokeListEntryWrapper(val entryList: List<PokeListEntry>)