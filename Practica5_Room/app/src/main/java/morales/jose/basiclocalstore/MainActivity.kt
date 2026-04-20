package morales.jose.basiclocalstore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import morales.jose.basiclocalstore.data.DataStoreManager
import morales.jose.basiclocalstore.data.PokemonDataBase
import morales.jose.basiclocalstore.data.PokemonRepository
import morales.jose.basiclocalstore.data.PreferenceManager
import morales.jose.basiclocalstore.navigation.AppNavigation
import morales.jose.basiclocalstore.screens.HomeScreen
import morales.jose.basiclocalstore.screens.LoginScreen
import morales.jose.basiclocalstore.ui.theme.BasicLocalStoreTheme
import morales.jose.basiclocalstore.viewModel.AuthViewModel
import morales.jose.basiclocalstore.viewModel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel = AuthViewModel(DataStoreManager(this))
        val database by lazy { PokemonDataBase.getDataBase(this) }
        val repository by lazy { PokemonRepository(database.pokemonDao()) }
        val pokemonViewModel: PokemonViewModel by viewModels { PokemonViewModelFactory(repository) }

        setContent {
            //val prefs = PreferenceManager(this)
            //var isLoggedIn by remember { mutableStateOf(prefs.isLoggedIn()) }

            BasicLocalStoreTheme {
                //MainScreen(viewModel = AuthViewModel(DataStoreManager(this)))

                AppNavigation(authViewModel,pokemonViewModel)
            }
        }
    }
}

class PokemonViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonViewModel(repository) as T
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicLocalStoreTheme {
        Greeting("Android")
    }
}