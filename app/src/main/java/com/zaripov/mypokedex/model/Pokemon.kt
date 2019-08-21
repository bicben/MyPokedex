package com.zaripov.mypokedex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zaripov.mypokedex.R

enum class PokemonType(val type: String, val color: Int) {
    BUG("Bug", R.color.bug) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIRE, FLYING, ROCK)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(GRASS, FIGHTING, GROUND)
        }
    },
    DARK("Dark", R.color.dark) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIGHTING, BUG, FAIRY)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(PSYCHIC, GHOST, DARK)
        }

    },
    DRAGON("Dragon", R.color.dragon) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(ICE, DRAGON, FAIRY)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(FIRE, WATER, ELECTRIC, GRASS)
        }
    },
    ELECTRIC("Electric", R.color.electric) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(GROUND)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(ELECTRIC, FLYING, STEEL)
        }
    },
    FAIRY("Fairy", R.color.fairy) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(POISON, STEEL)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(FIGHTING, BUG, DRAGON, DARK)
        }
    },
    FIGHTING("Fighting", R.color.fighting) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FLYING, PSYCHIC, FAIRY)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(BUG, ROCK, DARK)
        }
    },
    FIRE("Fire", R.color.fire) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(WATER, GROUND, ROCK)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(FIRE, GRASS, ICE, BUG, STEEL, FAIRY)
        }
    },
    FLYING("Flying", R.color.flying) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(ELECTRIC, ICE, ROCK)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(GRASS, FIGHTING, GROUND, BUG)
        }
    },
    GHOST("Ghost", R.color.ghost) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(GHOST, DARK)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(NORMAL, FIGHTING, POISON, BUG)
        }
    },
    GRASS("Grass", R.color.grass) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIRE, ICE, POISON, FLYING, BUG)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(WATER, ELECTRIC, GRASS, GROUND)
        }
    },
    GROUND("Ground", R.color.ground) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(WATER, GRASS, ICE)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(ELECTRIC, POISON, ROCK)
        }
    },
    ICE("Ice", R.color.ice) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIRE, FIGHTING, ROCK, STEEL)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(ICE)
        }
    },
    NORMAL("Normal", R.color.normal) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIGHTING)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(GHOST)
        }
    },
    POISON("Poison", R.color.poison) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(GROUND, PSYCHIC)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(GRASS, FIGHTING, POISON, BUG, FAIRY)
        }
    },
    PSYCHIC("Psychic", R.color.psychic) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(BUG, GHOST, DARK)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(FIGHTING, PSYCHIC)
        }
    },
    ROCK("Rock", R.color.rock) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(WATER, GRASS, FIGHTING, GROUND, STEEL)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(NORMAL, FIRE, POISON, FLYING)
        }
    },

    STEEL("Steel", R.color.steel) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(FIRE, FIGHTING, GROUND)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(NORMAL, GRASS, ICE, FLYING, PSYCHIC, BUG, ROCK, DRAGON, STEEL, FAIRY)
        }

    },
    WATER("Water", R.color.water) {
        override fun getWeaknesses(): List<PokemonType> {
            return listOf(ELECTRIC, GRASS)
        }

        override fun getResistances(): List<PokemonType> {
            return listOf(FIRE, WATER, ICE, STEEL)
        }
    },
    NO("No", R.color.colorAccent) {
        override fun getWeaknesses(): List<PokemonType> {
            return emptyList()
        }

        override fun getResistances(): List<PokemonType> {
            return emptyList()
        }
    };

    abstract fun getWeaknesses(): List<PokemonType>
    abstract fun getResistances(): List<PokemonType>
}

@Entity
data class Pokemon(
    @PrimaryKey
    val entryNum: Int,

    val name: String,
    val height: Int,
    val weight: Int,
    val sprite: String,
    val type1: PokemonType,
    val type2: PokemonType
)