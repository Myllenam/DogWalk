package com.example.dogwalk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogwalk.data.local.dao.PetDao
import com.example.dogwalk.data.local.dao.WalkDao
import com.example.dogwalk.data.model.Walk
import com.example.dogwalk.model.Pet

@Database(
    entities = [Pet::class, Walk::class],
    version = 3,
    exportSchema = false
)
abstract class DogWalkDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun walkDao(): WalkDao

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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()

                INSTANCE = instance

                instance
            }
        }

        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS walks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        petName TEXT NOT NULL,
                        startedAt INTEGER NOT NULL,
                        durationMinutes INTEGER NOT NULL,
                        distanceKm REAL NOT NULL,
                        steps INTEGER NOT NULL,
                        durationSeconds INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE walks ADD COLUMN petId INTEGER")
            }
        }
    }
}