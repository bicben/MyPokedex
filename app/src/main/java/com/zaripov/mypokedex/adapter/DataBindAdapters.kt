package com.zaripov.mypokedex.adapter

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zaripov.mypokedex.R
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.model.PokemonType

@BindingAdapter(value = ["bind_entry_name"])
fun TextView.bindEntryName(item: PokeListEntry?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter(value = ["bind_entry_num"])
fun TextView.bindEntryNum(item: PokeListEntry?) {
    item?.let {
        text = item.entryNum.toString()
    }
}


@BindingAdapter(value = ["bind_detail_sprite"])
fun ImageView.bindSprite(pokemon: Pokemon?) {
    pokemon?.let {
        val imgUri = pokemon.sprite.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
            .load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
            )
            .into(this)
    }
}

@BindingAdapter(value = ["bind_pokemon_name"])
fun TextView.bindPokemonName(pokemon: Pokemon?) {
    pokemon?.let {
        text = pokemon.name
    }
}

@BindingAdapter(value = ["bind_pokemon_entry"])
fun TextView.bindPokemonEntry(pokemon: Pokemon?) {
    pokemon?.let {
        text = pokemon.entryNum.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_height"])
fun TextView.bindPokemonHeight(pokemon: Pokemon?) {
    pokemon?.let {
        text = pokemon.height.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_weight"])
fun TextView.bindPokemonWeight(pokemon: Pokemon?) {
    pokemon?.let {
        text = pokemon.weight.toString()
    }
}

@BindingAdapter(value = ["bind_pokemon_type_chips"])
fun ChipGroup.bindPokemonTypeChips(pokemon: Pokemon?) {
    val inflater = LayoutInflater.from(this.context)
    pokemon?.let {
        val chip1 = inflater.inflate(R.layout.poke_type_chip, this, false) as Chip
        chip1.text = pokemon.type1.type
        chip1.setChipBackgroundColorResource(pokemon.type1.color)
        addView(chip1)

        if (pokemon.type2 != PokemonType.NO) {
            val chip2 = inflater.inflate(R.layout.poke_type_chip, this, false) as Chip
            chip2.text = pokemon.type2.type
            chip2.setChipBackgroundColorResource(pokemon.type2.color)
            addView(chip2)
        }
    }
}

@BindingAdapter(value = ["bind_pokemon_weaknesses_chips"])
fun ChipGroup.bindPokemonWeaknessesChips(pokemon: Pokemon?) {
    val inflater = LayoutInflater.from(this.context)
    pokemon?.let {
        val weaknesses = (pokemon.type1.getWeaknesses() + pokemon.type2.getWeaknesses()).distinct()
        val resistances = (pokemon.type1.getResistances() + pokemon.type2.getResistances()).distinct()
        val chips = weaknesses.subtract(resistances).map { pokeType ->
            val chip = inflater.inflate(R.layout.poke_type_chip, this, false) as Chip
            chip.text = pokeType.type
            chip.setChipBackgroundColorResource(pokeType.color)
            chip
        }

        for(chip in chips){
            addView(chip)
        }
    }
}

@BindingAdapter(value = ["bind_pokemon_resistances_chips"])
fun ChipGroup.bindPokemonResistancesChips(pokemon: Pokemon?) {
    val inflater = LayoutInflater.from(this.context)
    pokemon?.let {
        val weaknesses = (pokemon.type1.getWeaknesses() + pokemon.type2.getWeaknesses()).distinct()
        val resistances = (pokemon.type1.getResistances() + pokemon.type2.getResistances()).distinct()
        val chips = resistances.subtract(weaknesses).map { pokeType ->
            val chip = inflater.inflate(R.layout.poke_type_chip, this, false) as Chip
            chip.text = pokeType.type
            chip.setChipBackgroundColorResource(pokeType.color)
            chip
        }

        for(chip in chips){
            addView(chip)
        }
    }
}