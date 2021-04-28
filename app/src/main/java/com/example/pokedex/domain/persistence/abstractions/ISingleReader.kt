package com.example.pokedex.domain.persistence.abstractions

interface ISingleReader<TEntity> {
    fun get(key: String) : TEntity?
}