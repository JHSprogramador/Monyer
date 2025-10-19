package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendiendo.databinding.FragmentConversationHistoryBinding
import com.example.aprendiendo.ui.adapters.ConversationAdapter
import com.example.aprendiendo.ui.viewmodel.AIAssistantViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConversationHistoryFragment : Fragment() {

    private var _binding: FragmentConversationHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AIAssistantViewModel
    private lateinit var conversationAdapter: ConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConversationHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AIAssistantViewModel::class.java]

        setupRecyclerView()
        observeConversations()
    }

    private fun setupRecyclerView() {
        conversationAdapter = ConversationAdapter(
            onConversationClick = { conversation ->
                // Mostrar la conversación completa en un diálogo
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("💬 ${conversation.message}")
                    .setMessage("🤖 ${conversation.response}")
                    .setPositiveButton("Cerrar", null)
                    .show()
            },
            onFavoriteClick = { conversation ->
                // Toggle favorito
                viewModel.toggleFavorite(conversation.id, !conversation.isFavorite)
            },
            onDeleteClick = { conversation ->
                // Confirmar eliminación
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Eliminar conversación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta conversación?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        viewModel.deleteConversation(conversation)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        binding.rvConversations.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeConversations() {
        viewModel.savedConversations.observe(viewLifecycleOwner) { conversations ->
            if (conversations.isEmpty()) {
                binding.rvConversations.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.rvConversations.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                conversationAdapter.submitList(conversations)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

