package com.mobiverse.nebula.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiverse.nebula.data.MessageRepository
import com.mobiverse.nebula.data.entity.SatelliteMessageEntity
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the ChatScreen.
 * It provides the UI with message data and handles user actions.
 */
class ChatViewModel(private val repository: MessageRepository, private val recipientId: String) : ViewModel() {

    // Holds the state of the conversation, observed by the UI
    val messages: StateFlow<List<SatelliteMessageEntity>> = repository.getConversation(recipientId)
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Start listening for new messages as soon as the ViewModel is created
        repository.startListeningForMessages()
    }

    /**
     * Placeholder for sending a message. 
     * This would be called from the UI.
     */
    fun sendMessage(voiceFilePath: String, text: String) {
        viewModelScope.launch {
            // The actual sending logic is in SatelliteMessenger, 
            // but the ViewModel would trigger it.
        }
    }
}
