package com.example.pokedex.domain.persistence.abstractions

interface IMultipleReader<TEntity> {
    fun getAll() : List<TEntity>
    fun getMany(keys: List<String>) : List<TEntity>
}