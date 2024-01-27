package com.slowik.pokemonapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.slowik.pokemonapplication.details.DetailsActivity
import com.slowik.pokemonapplication.repository.Pokemon
import com.slowik.pokemonapplication.repository.UiState
import com.slowik.pokemonapplication.ui.theme.MainViewModel
import com.slowik.pokemonapplication.ui.theme.PokemonApplicationTheme


class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        setContent {
            PokemonApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                { MainView(
                    viewModel = viewModel,
                    onClick={id->navigateToDetailsActivity(id)})
                }

            }
        }
    }
    private fun navigateToDetailsActivity(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("CUSTOM_KEY", id)
        startActivity(intent)
    }
}

@Composable
fun MainView(viewModel: MainViewModel, onClick:(String)-> Unit) {
    val uiState by viewModel.liveDataPokemon.observeAsState(UiState())

    when {
        uiState.isLoading-> {
            MyLoadingView()
        }
        uiState.error!=null -> {
            MyErrorView()
        }
        uiState.data!=null->{
            uiState.data?.let {MyListView(pokemon=it, onClick={id->onClick.invoke(id)})}
        }
    }
}

@Composable
fun MyLoadingView(){
    CircularProgressIndicator(
        modifier = Modifier
        .padding(16.dp))
}

@Composable
fun MyErrorView(){
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
fun MyListView(pokemon:List<Pokemon>, onClick:(String)->Unit){
    LazyColumn{
        items(pokemon){pokemon->
            Log.d("Main Activity", "${pokemon.name} ${pokemon.id}")
            Layout(name = pokemon.name, id=pokemon.id, onClick={id->onClick.invoke(id)})

        }
    }
}


@Composable
fun Layout(name: String, id: Int, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick.invoke(name) }
            .fillMaxSize()
            .padding(top = 20.dp, end = 70.dp, start = 70.dp)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, top = 20.dp)
                .wrapContentSize(Alignment.TopEnd)
        ) {
            Text(
                text = "#$id",
                fontFamily = FontFamily.Monospace,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

        Image(
            painter = rememberAsyncImagePainter(model = url),
            contentDescription = "card image",
            modifier = Modifier
                .size(160.dp)
                .padding(5.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = name,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 25.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    PokemonApplicationTheme {
        Layout("Pokemon",1, onClick = {})
    }
}