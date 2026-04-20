package morales.jose.basiclocalstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import morales.jose.basiclocalstore.data.PokemonEntity
import morales.jose.basiclocalstore.viewModel.PokemonViewModel

@Composable
fun BolsaScreen(pokemonViewModel: PokemonViewModel){
    val pokemons by pokemonViewModel.filteredPokemons.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var pokemonParaEliminar by remember { mutableStateOf<PokemonEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        Text(text = "Bolsa de Pokemon",
            style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = pokemonViewModel.searchQuery,
            onValueChange = { pokemonViewModel.searchQuery = it },
            label = { Text("Buscar Pokémon") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(5.dp, 12.dp)
        ) {

            items(pokemons){ pokemon ->
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    ListItem(
                        headlineContent = {Text("${pokemon.name} - Lvl: ${pokemon.level}")},
                        supportingContent = {
                            Text(pokemon.type)
                        },
                        trailingContent = {
                            IconButton(onClick = { pokemonParaEliminar = pokemon }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Liberar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {pokemonViewModel.updateLevel(pokemon)},
                            enabled = pokemon.level < 100
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Text("Subir Nivel")
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
    pokemonParaEliminar?.let { pokemon ->
        AlertDialog(
            onDismissRequest = { pokemonParaEliminar = null },
            title = { Text("Liberar Pokémon") },
            text = { Text("¿Seguro que quieres liberar a ${pokemon.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        pokemonViewModel.deletePokemon(pokemon)
                        pokemonParaEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Liberar") }
            },
            dismissButton = {
                TextButton(onClick = { pokemonParaEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

