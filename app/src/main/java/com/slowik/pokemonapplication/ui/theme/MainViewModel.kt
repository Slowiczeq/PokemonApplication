package com.slowik.pokemonapplication.ui.theme

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowik.pokemonapplication.repository.PokeRepository
import com.slowik.pokemonapplication.repository.Pokemon
import com.slowik.pokemonapplication.repository.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel (){
    private val pokeRepository= PokeRepository()
    val liveDataPokemon=MutableLiveData<UiState<List<Pokemon>>>()

    fun getData(){
        liveDataPokemon.postValue(UiState(isLoading = true))
        viewModelScope.launch (Dispatchers.IO){

            try {
                val request = pokeRepository.getPokeResponse()
                Log.d("MainViewModel", "request response code:${request.code()}")
                if (request.isSuccessful) {
                    val pokemonList = request.body()?.results ?: emptyList()
                    val updatedPokemonList = pokemonList.mapIndexed { index, pokemon ->
                        pokemon.copy(id = index + 1)
                    }
                    liveDataPokemon.postValue(UiState(data = updatedPokemonList))
                } else {
                    liveDataPokemon.postValue(UiState(error = "${request.code()}"))
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "request failed, exception", e)
                liveDataPokemon.postValue(UiState(error = "exception $e"))
            }
        }
    }
}