

package com.example.android.animalsdb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        AnimalViewModelFactory((application as AnimalsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AnimalListAdapter(wordViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val editTextAnimal = findViewById<EditText>(R.id.editTextAnimal)
        val editTextContinent = findViewById<EditText>(R.id.editTextContinent)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        buttonAdd.setOnClickListener{
            val animalText = editTextAnimal.text.toString().trim()
            val continentText = editTextContinent.text.toString().trim().lowercase()

            val continentList = listOf("africa", "antarctica", "asia", "australia", "europe", "north america", "south america")

            if (animalText.isEmpty()) {
                Toast.makeText(this, "Please enter an animal name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (continentText.isEmpty()) {
                Toast.makeText(this, "Please enter a continent", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!continentList.contains(continentText)) {
                Toast.makeText(this, "Please enter a valid continent", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val animal = Animal(animalText, continentText)
            wordViewModel.insert(animal)

            editTextAnimal.text = null
            editTextContinent.text = null
        }

        
        // Add an observer on the LiveData returned by getAlphabetizedAnimals
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allAnimals.observe(owner = this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }
    }

}
