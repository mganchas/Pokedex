package com.example.pokedex.domain.persistence

import android.util.Log
import com.example.pokedex.data.models.PokemonDetails
import com.example.pokedex.data.models.PokemonDetails_
import com.example.pokedex.data.persistence.ObjectBox
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import javax.inject.Inject

class PersistenceApiObjectBox : IPersistenceApi
{
    companion object {
        private val TAG = PersistenceApiObjectBox::class.java.simpleName
    }

    private val db : Box<PokemonDetails> by lazy {
        ObjectBox.boxStore.boxFor()
    }

    override suspend fun getAll(): List<PokemonDetails> {
        Log.d(TAG, "getAll()")
        return db.all
    }

    override suspend fun findByName(name: String): PokemonDetails? {
        Log.d(TAG, "findByName() name: $name")
        return db.query().equal(PokemonDetails_.name, name).build().findFirst()
    }

    override suspend fun findById(id: String): PokemonDetails? {
        Log.d(TAG, "findById() id: $id")
        return db.query().equal(PokemonDetails_.pokemonId, id).build().findFirst()
    }

    override suspend fun add(pokemon: PokemonDetails) {
        Log.d(TAG, "add() pokemon: $pokemon")
        db.put(pokemon)
    }

    override suspend fun addMany(pokemons: List<PokemonDetails>) {
        Log.d(TAG, "addMany() pokemons: $pokemons")
        db.put(pokemons)
    }

    override suspend fun remove(pokemon: PokemonDetails) {
        Log.d(TAG, "remove() pokemon: $pokemon")
        db.remove(pokemon)
    }

    override suspend fun removeAll() {
        Log.d(TAG, "removeAll()")
        db.removeAll()
    }
}