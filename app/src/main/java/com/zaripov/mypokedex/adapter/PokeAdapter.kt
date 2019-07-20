package com.zaripov.mypokedex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpDelegate
import com.zaripov.mypokedex.databinding.PokeListEntryBinding
import com.zaripov.mypokedex.model.PokeListEntry
import io.reactivex.Completable
import io.reactivex.internal.operators.completable.CompletableFromAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokeAdapter(private val clickListener: PokeListClickListener) :
    ListAdapter<PokeListEntry, PokeAdapter.PokeViewHolder>(PokeDiffCallback()) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun submitEntries(entries: List<PokeListEntry>) {
        scope.launch {
            withContext(Dispatchers.Main){
                submitList(entries)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        return PokeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class PokeViewHolder private constructor(private val binding: PokeListEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PokeListEntry, clickListener: PokeListClickListener) {
            binding.item = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PokeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PokeListEntryBinding.inflate(inflater, parent, false)

                return PokeViewHolder(binding)
            }
        }
    }

    class PokeDiffCallback : DiffUtil.ItemCallback<PokeListEntry>() {
        override fun areItemsTheSame(oldItem: PokeListEntry, newItem: PokeListEntry): Boolean {
            return oldItem.entryNum == newItem.entryNum
        }

        override fun areContentsTheSame(oldItem: PokeListEntry, newItem: PokeListEntry): Boolean {
            return oldItem == newItem
        }
    }
}