package com.slowik.pokemonapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.slowik.pokemonapplication.repository.Pokemon
import com.slowik.pokemonapplication.repository.UiState
import com.slowik.pokemonapplication.ui.theme.MainViewModel
import com.slowik.pokemonapplication.ui.theme.PokemonApplicationTheme
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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
                { MainView(viewModel = viewModel)
                }

            }
        }
    }
}



@Composable
fun MainView(viewModel: MainViewModel) {
    val uiState by viewModel.liveDataPokemon.observeAsState(UiState())

    when {
        uiState.isLoading-> {
            MyLoadingView()
        }
        uiState.error!=null -> {
            MyErrorView()
        }
        uiState.data!=null->{
            uiState.data?.let {MyListView(pokemon=it)}
        }
    }
}

@Composable
fun MyLoadingView(){
    CircularProgressIndicator()
}

@Composable
fun MyErrorView(){
}

@Composable
fun MyListView(pokemon:List<Pokemon>){
    LazyColumn{
        items(pokemon){pokemon->
            Log.d("Main Activity", "${pokemon.name} ${pokemon.id}")
            Layout(name = pokemon.name, id=pokemon.id)

        }
    }
}

fun getUnsafeClient(): OkHttpClient {
    val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    )
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())
    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
    okHttpClientBuilder.hostnameVerifier { _, _ -> true }
    return okHttpClientBuilder.build()
}

@Composable
fun Layout(name:String, id:Int) {
    Column {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 12.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box {
            Text(
                text = "Tekst",
                color = Color.White,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background(Color.Yellow, shape = RoundedCornerShape(4.dp))
                    .padding(start = 12.dp, top = 4.dp, bottom = 4.dp, end = 12.dp)
            )
        }
        Row {
            Text(
                text = "Experience:",
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )
            Text(
                text = "50",
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )
        }
        Row {
            Text(
                text = "Power:",
                modifier = Modifier.padding(start = 12.dp, top = 8.dp)
            )
            Text(
                text = "50",
                modifier = Modifier.padding(start = 12.dp, top = 8.dp)
            )
        }

        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
        val imageLoader = ImageLoader.Builder(LocalContext.current).okHttpClient { getUnsafeClient() }.build()

        AsyncImage(
            model = url,
            imageLoader = imageLoader,
            contentDescription = "card image"
        )
    }
}



@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    PokemonApplicationTheme {
        Layout("Pokemon",1)
    }
}