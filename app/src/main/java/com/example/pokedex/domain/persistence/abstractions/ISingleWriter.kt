package com.example.pokedex.domain.persistence.abstractions

interface ISingleWriter<TEntity> {
    fun add(key: String, document: TEntity)
}