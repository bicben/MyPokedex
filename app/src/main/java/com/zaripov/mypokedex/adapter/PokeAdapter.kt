package com.zaripov.mypokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpDelegate
import com.zaripov.mypokedex.R
import com.zaripov.mypokedex.databinding.PokeListEntryBinding
import com.zaripov.mypokedex.model.PokeListEntry
import io.reactivex.Completable
import io.reactivex.internal.operators.completable.CompletableFromAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokeAdapter(private val clickListener: PokeListClickListener) :
    PagedListAdapter<PokeListEntry, RecyclerView.ViewHolder>(PokeDiffCallback()) {

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_PLACEHOLDER = 0
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            null -> TYPE_PLACEHOLDER
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> PokeViewHolder.from(parent)
            else -> PokePlaceholder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is PokeViewHolder) holder.bind(item!!, clickListener)
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

    class PokePlaceholder private constructor(view: View):
            RecyclerView.ViewHolder(view){
        companion object{
            fun from (parent: ViewGroup): PokePlaceholder{
                val itemView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.poke_list_placeholder, parent, false)

                return PokePlaceholder(itemView)
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