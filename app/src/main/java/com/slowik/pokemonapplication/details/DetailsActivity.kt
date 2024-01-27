package com.slowik.pokemonapplication.details

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.slowik.pokemonapplication.repository.PokemonDetailsResponse
import com.slowik.pokemonapplication.repository.UiState
import com.slowik.pokemonapplication.ui.theme.DetailsViewModel
import com.slowik.pokemonapplication.ui.theme.PokemonApplicationTheme

class DetailsActivity : ComponentActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = intent.getStringExtra("CUSTOM_KEY")
        if (pokemonId != null) {
            detailsViewModel.getPokemonDetails(id = pokemonId)
        } else {
            showError()
        }

        setContent {
            PokemonApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    DetailsView(
                        detailsViewModel = detailsViewModel
                    )
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "No pokemon id.", Toast.LENGTH_SHORT).show()
    }
    @Composable
    fun DetailsView(detailsViewModel: DetailsViewModel) {
        val uiState by detailsViewModel.liveDataPokemonDetails.observeAsState(UiState())

        when {
            uiState.isLoading -> {
                MyDetailsLoadingView()
            }

            uiState.error != null -> {
                MyDetailsErrorView()
            }

            uiState.data != null -> {
                uiState.data?.let {
                    val details: PokemonDetailsResponse = uiState!!.data!!
                    PokemonDetailsContent(details)
                }
            }
        }
    }

    @Composable
    fun MyDetailsLoadingView() {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
        )
    }

    @Composable
    fun MyDetailsErrorView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("An error occured", color = Color.Red)
        }
    }

    @Composable
    fun PokemonDetailsContent(details: PokemonDetailsResponse) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp, start = 10.dp, bottom = 90.dp, top = 90.dp)
                .background(
                    color = Color(0xFF3490DC),
                    shape = MaterialTheme.shapes.medium.copy(
                        topEnd = CornerSize(40.dp),
                        topStart = CornerSize(40.dp),
                        bottomEnd = CornerSize(40.dp),
                        bottomStart = CornerSize(40.dp)
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val url =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${details.id}.png"

            Text(
                text = details.name,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )

            Image(
                painter = rememberAsyncImagePainter(model = url),
                contentDescription = "card image",
                modifier = Modifier
                    .size(250.dp)
                    .padding(5.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "height: ${details.height}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "weight: ${details.weight}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "experience: ${details.base_experience}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PokemonDetailsContentPreview() {
        val details = PokemonDetailsResponse(
            weight = 10,
            height = 5,
            base_experience = 50,
            id = 1,
            name = "pikachu"
        )
        PokemonApplicationTheme {
            PokemonDetailsContent(details = details)
        }
    }
}
