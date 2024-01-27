package com.slowik.pokemonapplication.ui.theme

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slowik.pokemonapplication.repository.PokeRepository
import com.slowik.pokemonapplication.repository.PokemonDetailsResponse
import com.slowik.pokemonapplication.repository.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val pokeRepository = PokeRepository()
    val liveDataPokemonDetails = MutableLiveData<UiState<PokemonDetailsResponse>>()

    fun getPokemonDetails(id: String) {
        liveDataPokemonDetails.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = pokeRepository.getPokeDetails(id)
                Log.d("DetailsViewModel", "request response code: ${response.code()}")
                if (response.isSuccessful) {
                    val details = response.body()
                    liveDataPokemonDetails.postValue(UiState(data = details))
                } else {
                    liveDataPokemonDetails.postValue(UiState(error = "${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "request failed, exception", e)
                liveDataPokemonDetails.postValue(UiState(error = "exception $e"))
            }
        }
    }
}
