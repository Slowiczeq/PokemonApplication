package com.slowik.pokemonapplication.repository

data class PokemonDetailsResponse(
    val weight: Int,
    val height: Int,
    val base_experience: Int,
    val id: Int,
    val name: String
)
