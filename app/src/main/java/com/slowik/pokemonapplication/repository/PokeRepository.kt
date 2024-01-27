package com.slowik.pokemonapplication.repository

import retrofit2.Response


class PokeRepository {

    suspend fun getPokeResponse(): Response<PokeResponse> =
        PokeService.pokeService.getPokeResponse()

    suspend fun getPokeDetails(id:String): Response<PokemonDetailsResponse> =
        PokeService.pokeService.getPokeDetails(id)

}