package ma.ensa.bankgrpc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.bankgrpc.databinding.ItemCompteBinding
import ma.ensa.bankgrpc.stubs.Compte

class CompteAdapter : ListAdapter<Compte, CompteAdapter.CompteViewHolder>(CompteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompteViewHolder {
        val binding = ItemCompteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CompteViewHolder(private val binding: ItemCompteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(compte: Compte) {
            binding.apply {
                textViewId.text = "ID: ${compte.id}"
                textViewSolde.text = "Solde: ${compte.solde}€"
                textViewType.text = "Type: ${compte.type.name}"
                textViewDate.text = "Date: ${compte.dateCreation}"
            }
        }
    }

    class CompteDiffCallback : DiffUtil.ItemCallback<Compte>() {
        override fun areItemsTheSame(oldItem: Compte, newItem: Compte): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Compte, newItem: Compte): Boolean {
            return oldItem == newItem
        }
    }
}