package morales.jose.basiclocalstore.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import morales.jose.basiclocalstore.data.PokemonEntity
import morales.jose.basiclocalstore.data.PokemonRepository

class PokemonViewModel (private val repository: PokemonRepository): ViewModel(){

    var searchQuery by mutableStateOf("")
    val availablePokemons = listOf(
        PokemonEntity(name = "Bulbasaur", number = "0001", type = "Planta/Veneno"),
        PokemonEntity(name = "Charmander", number = "0004", type = "Fuego"),
        PokemonEntity(name = "Squirtle", number = "0007", type = "Agua"),
        PokemonEntity(name = "Pikachu", number = "0025", type = "Eléctrico"),
        PokemonEntity(name = "Gengar", number = "0094", type = "Fantasma/Veneno"),
        PokemonEntity(name = "Lucario", number = "0448", type = "Lucha/Acero"),
        PokemonEntity(name = "Greninja", number = "0658", type = "Agua/Siniestro"),
        PokemonEntity(name = "Mimikyu", number = "0778", type = "Fantasma/Hada")
    )
    var wildPokemon by mutableStateOf<PokemonEntity?>(null)
        private set
    var capturedPokemons by mutableStateOf(listOf<PokemonEntity>())
    private set
    var pokemonSeEscapo by mutableStateOf(false)
        private set
    val filteredPokemons: StateFlow<List<PokemonEntity>> = snapshotFlow { searchQuery }
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allPokemons
            } else {
                repository.searchPokemon(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val pokemonsState: StateFlow<List<PokemonEntity>> = repository.allPokemons
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun searchPokemon(){
        wildPokemon = availablePokemons.random()
    }
    fun releaseCapturedPokemon(){
        capturedPokemons = emptyList()
    }
    fun capturarPokemon(){
        wildPokemon?.let {
            val success = (1..100).random()
            if (success > 50){
                capturedPokemons = capturedPokemons + it
                pokemonSeEscapo = false
                wildPokemon = null
            }else{
                pokemonSeEscapo = true
                wildPokemon = null
            }
        }
    }
    fun saveCapturedPokemons(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insertAll(capturedPokemons)
            releaseCapturedPokemon()
            onComplete()
        }
    }
    fun updateLevel(pokemon: PokemonEntity) {
        if (pokemon.level < 100) {
            val success = (1..100).random() > 50
            if (success) {
                viewModelScope.launch {
                    repository.update(pokemon.copy(level = pokemon.level + 1))
                }
            }
        }
    }
    fun addPokemon(name: String, number: String, type: String, level: Int = 1){
        viewModelScope.launch {
            repository.insert(
                PokemonEntity(
                    name = name,
                    number = number,
                    type = type,
                    level = level
                )
            )
        }
    }
    fun deletePokemon(pokemon: PokemonEntity){
        viewModelScope.launch {
            repository.delete(pokemon)
        }
    }
}