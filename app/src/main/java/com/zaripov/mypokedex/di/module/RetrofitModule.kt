package com.zaripov.mypokedex.di.module

import android.util.Log
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.zaripov.mypokedex.model.PokeListEntry
import com.zaripov.mypokedex.model.PokeListEntryWrapper
import com.zaripov.mypokedex.model.Pokemon
import com.zaripov.mypokedex.model.PokemonType
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://pokeapi.co/api/v2/").build()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(converterFactory: Converter.Factory): Retrofit.Builder {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(converterFactory)
    }

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGson(
        pokeEntryDeserializer: JsonDeserializer<PokeListEntryWrapper>,
        pokemonDeserializer: JsonDeserializer<Pokemon>
    ): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .serializeNulls()
            .registerTypeAdapter(PokeListEntryWrapper::class.java, pokeEntryDeserializer)
            .registerTypeAdapter(Pokemon::class.java, pokemonDeserializer)
            .create()
    }

    @Provides
    @Singleton
    fun providePokeEntryDeserializer(): JsonDeserializer<PokeListEntryWrapper> {
        return JsonDeserializer { json, _, _ ->
            Log.i("RetrofitModule", "Beginning deserialization...")
            val jsonObject = json.asJsonObject
            val entries = mutableListOf<PokeListEntry>()
            val jsonArray = jsonObject.getAsJsonArray("pokemon_entries")
            Log.i("RetrofitModule", "entries: ${entries.size}")

            jsonArray.forEach {
                val entry = it.asJsonObject
                entries.add(
                    PokeListEntry(
                        entry.getAsJsonPrimitive("entry_number").asInt,
                        entry.getAsJsonObject("pokemon_species").getAsJsonPrimitive("name").asString.capitalize()
                    )
                )
            }

            return@JsonDeserializer PokeListEntryWrapper(entries)
        }
    }

    @Provides
    @Singleton
    fun providePokemonDeserializer(): JsonDeserializer<Pokemon> {
        return JsonDeserializer { json, _, _ ->
            val jsonObject = json.asJsonObject

            val types = jsonObject.getAsJsonArray("types")
            val type1 =
                PokemonType.valueOf(value = types[0].asJsonObject.getAsJsonObject("type").getAsJsonPrimitive("name").asString.toUpperCase())
            val type2 =
                if (types.size() < 2) PokemonType.NO
                else PokemonType.valueOf(value = types[1].asJsonObject.getAsJsonObject("type").getAsJsonPrimitive("name").asString.toUpperCase())

            return@JsonDeserializer Pokemon(
                jsonObject.getAsJsonPrimitive("id").asInt,
                jsonObject.getAsJsonPrimitive("name").asString.capitalize(),
                jsonObject.getAsJsonPrimitive("height").asInt,
                jsonObject.getAsJsonPrimitive("weight").asInt,
                jsonObject.getAsJsonObject("sprites").getAsJsonPrimitive("front_default").asString,
                type1,
                type2
            )
        }
    }
}