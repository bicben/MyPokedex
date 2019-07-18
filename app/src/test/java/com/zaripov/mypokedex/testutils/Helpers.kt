package com.zaripov.mypokedex.testutils

import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.model.PokemonType

class Helpers {
    companion object{
        val testPoke1 = Pokemon(1,
            "Shiny Bulbasaur",
            7,
            69,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
            PokemonType.POISON,
            PokemonType.GRASS)

        val testPoke2 = Pokemon(25,
            "Shiny Pikachu",
            4,
            60,
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/female/25.png",
            PokemonType.ELECTRIC,
            PokemonType.NO)

        val testEntries1 = listOf(PokeListEntry(7, "Shiny Squirtle"))
        val testEntries2 = listOf(PokeListEntry(4, "Shiny Charmander"))

        val testPokeEntryWrapper = PokeListEntryWrapper(testEntries2)
    }
}