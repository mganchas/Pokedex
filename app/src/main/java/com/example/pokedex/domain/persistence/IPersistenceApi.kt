package com.example.pokedex.domain.persistence

import com.example.pokedex.domain.persistence.abstractions.IMultipleReader
import com.example.pokedex.domain.persistence.abstractions.IMultipleWriter
import com.example.pokedex.domain.persistence.abstractions.ISingleReader
import com.example.pokedex.domain.persistence.abstractions.ISingleWriter

interface IPersistenceApi<TEntity> :
    ISingleReader<TEntity>,
    IMultipleReader<TEntity>,
    ISingleWriter<TEntity>,
    IMultipleWriter<TEntity>