package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendiendo.databinding.FragmentAiAssistantBinding
import com.example.aprendiendo.ui.adapters.ChatMessageAdapter
import com.example.aprendiendo.ui.viewmodel.AIAssistantViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class AIAssistantFragment : Fragment() {

    private var _binding: FragmentAiAssistantBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AIAssistantViewModel
    private lateinit var chatAdapter: ChatMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AIAssistantViewModel::class.java]

        setupRecyclerView()
        setupSuggestedQuestions()
        setupInputArea()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatMessageAdapter()
        binding.rvChatMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
        }
    }

    private fun setupSuggestedQuestions() {
        val suggestions = viewModel.getSuggestedQuestions()

        binding.chipGroupSuggestions.removeAllViews()

        suggestions.forEach { question ->
            val chip = Chip(requireContext()).apply {
                text = question
                isClickable = true
                setOnClickListener {
                    binding.etMessage.setText(question)
                    sendMessage()
                }
            }
            binding.chipGroupSuggestions.addView(chip)
        }
    }

    private fun setupInputArea() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString().trim()
        if (message.isNotEmpty()) {
            viewModel.sendMessage(message)
            binding.etMessage.text?.clear()

            // Ocultar sugerencias después del primer mensaje
            if (binding.cardSuggestedQuestions.visibility == View.VISIBLE) {
                binding.cardSuggestedQuestions.visibility = View.GONE
            }
        }
    }

    private fun observeViewModel() {
        // Observar mensajes del chat
        viewModel.chatMessages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.submitList(messages) {
                // Scroll al último mensaje
                if (messages.isNotEmpty()) {
                    binding.rvChatMessages.smoothScrollToPosition(messages.size - 1)
                }
            }
        }

        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSend.isEnabled = !isLoading
            binding.etMessage.isEnabled = !isLoading
        }

        // Observar errores
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

