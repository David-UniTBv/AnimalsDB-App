
package com.example.android.animalsdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.animalsdb.AnimalListAdapter.WordViewHolder

class AnimalListAdapter(private val wordViewModel: WordViewModel) : ListAdapter<Animal, WordViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.animal,current.continent)
        holder.itemView.findViewById<ImageButton>(R.id.imageButtonDelete).setOnClickListener {
            wordViewModel.delete(current.animal)
        }
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val animalItemView: TextView = itemView.findViewById(R.id.textViewAnimal)
        private val continentItemView: TextView = itemView.findViewById(R.id.textViewContinent)


        fun bind(animalText: String?, continentText: String?) {
            animalItemView.text = animalText
            continentItemView.text = continentText

        }

        companion object {
            fun create(parent: ViewGroup): WordViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WordViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Animal>() {
            override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
                return oldItem.animal == newItem.animal
            }
        }
    }
}
