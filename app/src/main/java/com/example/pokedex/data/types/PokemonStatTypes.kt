package com.example.pokedex.data.types

/*
    Note: I'm using polymorphism to implement (and retrieve) each entry's
    details (using the PokemonStatsManager)
 */
enum class PokemonStatTypes {
    HP,
    Attack,
    SpecialAttack,
    Defense,
    SpecialDefense,
    Speed
}