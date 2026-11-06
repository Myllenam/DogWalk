package com.example.dogwalk.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogwalk.data.local.dao.PetDao
import com.example.dogwalk.data.local.dao.WalkDao
import com.example.dogwalk.model.Pet
import com.example.dogwalk.model.Walk

@Database(
    entities = [Pet::class, Walk::class],
    version = 4,
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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
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

        private val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                database.execSQL("PRAGMA foreign_keys=OFF")
                database.execSQL(
                    """
                    CREATE TABLE walks_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        petId INTEGER,
                        petName TEXT NOT NULL,
                        startedAt INTEGER NOT NULL,
                        durationMinutes INTEGER NOT NULL,
                        distanceKm REAL NOT NULL,
                        steps INTEGER NOT NULL,
                        durationSeconds INTEGER NOT NULL,
                        FOREIGN KEY(petId) REFERENCES pets(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO walks_new (
                        id, petId, petName, startedAt, durationMinutes,
                        distanceKm, steps, durationSeconds
                    )
                    SELECT
                        id, petId, petName, startedAt, durationMinutes,
                        distanceKm, steps, durationSeconds
                    FROM walks
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE walks")
                database.execSQL("ALTER TABLE walks_new RENAME TO walks")
                database.execSQL("CREATE INDEX index_walks_petId ON walks (petId)")
                database.execSQL("PRAGMA foreign_keys=ON")
            }
        }
    }
}