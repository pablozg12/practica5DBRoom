package morales.jose.basiclocalstore.data

import kotlinx.coroutines.flow.Flow

class PokemonRepository(private val PokemonDAO: PokemonDAO){
    val allPokemons = PokemonDAO.getAllPokemon()

    suspend fun insert(pokemon: PokemonEntity){
        PokemonDAO.insertPokemon(pokemon)
    }

    suspend fun insertAll(pokemons: List<PokemonEntity>){
        PokemonDAO.insertAll(pokemons)
    }

    suspend fun update(pokemon: PokemonEntity) {
        PokemonDAO.update(pokemon)
    }

    suspend fun delete(pokemon: PokemonEntity){
        PokemonDAO.delete(pokemon)
    }

    suspend fun searchPokemon(query: String): Flow<List<PokemonEntity>> {
        return PokemonDAO.searchPokemon(query)
    }
}