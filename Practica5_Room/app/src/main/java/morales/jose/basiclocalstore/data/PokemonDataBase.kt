package morales.jose.basiclocalstore.data

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)

abstract class PokemonDataBase : RoomDatabase(){
    abstract fun pokemonDao(): PokemonDAO

    companion object {
        @Volatile
        private var INSTANCE: PokemonDataBase? = null

        fun getDataBase(context: Context): PokemonDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDataBase::class.java,
                    "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}