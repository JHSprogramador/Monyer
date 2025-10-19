package com.example.aprendiendo.ui.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aprendiendo.R
import com.example.aprendiendo.databinding.ItemConversationBinding
import com.example.aprendiendo.data.entities.AIConversation

class ConversationAdapter(
    private val onConversationClick: (AIConversation) -> Unit,
    private val onFavoriteClick: (AIConversation) -> Unit,
    private val onDeleteClick: (AIConversation) -> Unit
) : ListAdapter<AIConversation, ConversationAdapter.ConversationViewHolder>(ConversationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ConversationViewHolder(
        private val binding: ItemConversationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: AIConversation) {
            binding.tvQuestion.text = conversation.message
            binding.tvResponse.text = conversation.response

            // Formatear la fecha relativa
            val timeAgo = DateUtils.getRelativeTimeSpanString(
                conversation.timestamp.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
            binding.tvDate.text = timeAgo

            // Icono de favorito
            val favoriteIcon = if (conversation.isFavorite) {
                android.R.drawable.star_big_on
            } else {
                android.R.drawable.star_big_off
            }
            binding.btnFavorite.setIconResource(favoriteIcon)

            // Listeners
            binding.root.setOnClickListener {
                onConversationClick(conversation)
            }

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(conversation)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(conversation)
            }
        }
    }

    class ConversationDiffCallback : DiffUtil.ItemCallback<AIConversation>() {
        override fun areItemsTheSame(oldItem: AIConversation, newItem: AIConversation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AIConversation, newItem: AIConversation): Boolean {
            return oldItem == newItem
        }
    }
}

