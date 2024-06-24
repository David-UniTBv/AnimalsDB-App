

package com.example.android.animalsdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Animal::class], version = 2)
abstract class AnimalRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): AnimalDao

    companion object {
        @Volatile
        private var INSTANCE: AnimalRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AnimalRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimalRoomDatabase::class.java,
                    "animal_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(animalDao: AnimalDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            animalDao.deleteAll()

            val animals = listOf(
                Animal("Elephant", "Africa"),
                Animal("Penguin", "Antarctica"),
                Animal("Panda", "Asia"),
                Animal("Cow", "Europe"),
                Animal("Bison", "North America"),
                Animal("Kangaroo", "Australia"),
                Animal("Jaguar", "South America")
            )

            for (animal in animals) {
                animalDao.insert(animal)
            }
        }
    }
}
