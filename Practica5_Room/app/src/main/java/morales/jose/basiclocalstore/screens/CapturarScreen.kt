package morales.jose.basiclocalstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import morales.jose.basiclocalstore.viewModel.PokemonViewModel

@Composable
fun CapturarScreen(pokemonViewModel: PokemonViewModel, onBack: () -> Unit){

    val pokemons by pokemonViewModel.pokemonsState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {pokemonViewModel.searchPokemon()}
        ) {
            Text(text = "Buscar en la hierva")
        }

        Spacer(modifier = Modifier.height(16.dp))

        pokemonViewModel.wildPokemon?.let { pokemon ->
            Text("Apareció un ${pokemon.name}!")
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {pokemonViewModel.capturarPokemon()}
            ) {
                Text("Capturar")
            }
        }

        if (pokemonViewModel.pokemonSeEscapo){
            Text("El pokemon se escapó", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Pokemons capturados: ${pokemons.size}")

        LazyColumn{
            items(pokemonViewModel.capturedPokemons){pokemon ->
                Text("${pokemon.name} - ${pokemon.type}")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        ElevatedButton(
            onClick = {
                pokemonViewModel.saveCapturedPokemons {
                    onBack()
                }
            },
            enabled = pokemonViewModel.capturedPokemons.isNotEmpty()
        ) {
            Text("Mandar a bolsa (${pokemonViewModel.capturedPokemons.size})")
        }
        TextButton(
            onClick = {
                pokemonViewModel.releaseCapturedPokemon()
            }
        ) {
            Text("Liberar pokemones")
        }


    }
}