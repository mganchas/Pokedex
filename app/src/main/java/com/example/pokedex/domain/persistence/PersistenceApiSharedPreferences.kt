package com.example.pokedex.domain.persistence
import android.content.Context
import androidx.core.content.edit
import com.example.pokedex.data.model.pokemon.Pokemon
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PersistenceApiSharedPreferences @Inject constructor(
    @ApplicationContext private val appContext: Context
) : IPersistenceApi<Pokemon>
{
    private val preferences = appContext.getSharedPreferences("prefs", 0)

    override fun get(key: String): Pokemon? {
        val rawValue = preferences.getString(key, null)
        return Gson().fromJson(rawValue, Pokemon::class.java)
    }

    override fun getAll(): List<Pokemon> {
        val rawValues = preferences.all
        val values = mutableListOf<Pokemon>()
        rawValues.forEach { mapEntry ->
            val rawValue = mapEntry.value
            if (rawValue is String) {
                values.add(Gson().fromJson(rawValue, Pokemon::class.java))
            }
        }
        return values
    }

    override fun getMany(keys: List<String>): List<Pokemon> {
        val values = mutableListOf<Pokemon>()
        keys.forEach { key ->
            get(key)?.let {
                values.add(it)
            }
        }
        return values
    }

    override fun add(key: String, document: Pokemon) {
        val convertedDocument = Gson().toJson(document)
        preferences.edit {
            putString(key, convertedDocument)
        }
    }

    override fun addMany(keys: List<String>, documents: List<Pokemon>) {
        if (keys.size != documents.size) {
            throw IllegalArgumentException("keys and documents are not of the same size")
        }
        for(i in keys.indices) {
            add(keys[i], documents[i])
        }
    }
}