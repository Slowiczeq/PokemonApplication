package com.slowik.pokemonapplication.repository

import retrofit2.Response


class PokeRepository {

    suspend fun getPokeResponse(): Response <PokeResponse> =
        PokeService.pokeService.getPokeResponse()

}