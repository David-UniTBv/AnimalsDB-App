
package com.example.android.animalsdb

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class AnimalRepository(private val animalDao: AnimalDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allAnimals: Flow<List<Animal>> = animalDao.getAlphabetizedAnimals()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(animal: Animal) {
        animalDao.insert(animal)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(animal: String) {
        animalDao.deleteFromName(animal)
    }
}
