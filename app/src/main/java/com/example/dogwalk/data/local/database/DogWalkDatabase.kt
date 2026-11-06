package com.example.dogwalk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogwalk.data.local.dao.PetDao
import com.example.dogwalk.model.Pet

@Database(
    entities = [Pet::class],
    version = 1,
    exportSchema = false
)
abstract class DogWalkDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    companion object {

        @Volatile
        private var INSTANCE: DogWalkDatabase? = null

        fun getDatabase(
            context: Context
        ): DogWalkDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        DogWalkDatabase::class.java,
                        "dogwalk_database"
                    )
                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}