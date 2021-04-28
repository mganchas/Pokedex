package com.example.pokedex.domain.persistence.abstractions

interface IMultipleWriter<TEntity> {
    fun addMany(keys: List<String>, documents: List<TEntity>)
}