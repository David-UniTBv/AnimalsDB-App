

package com.example.android.animalsdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class WordViewModel(private val repository: AnimalRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allAnimals: LiveData<List<Animal>> = repository.allAnimals.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(animal: Animal) = viewModelScope.launch {
        val continent = animal.continent.lowercase().replaceFirstChar { it.uppercase() }

        val caseAnimal = Animal(
            animal.animal.lowercase().replaceFirstChar { it.uppercase() },
            animal.continent.lowercase().replaceFirstChar { it.uppercase() }
                .replace(" a"," A")
        )
        repository.insert(caseAnimal)
    }
    fun delete(animal: String) = viewModelScope.launch {
        repository.delete(animal)
    }
}

class AnimalViewModelFactory(private val repository: AnimalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
