package com.app.prvtmsg

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.prvtmsg.ui.screens.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MessageHandlerViewModel (channelId: String) : ViewModel() {
    private val db = Firebase.firestore
    private val messageRef = db.collection(channelId)
    var fetchedMessages = mutableStateOf(emptyList<Message>())
    private var listenerRegistration: ListenerRegistration? = null


    fun startListeningForMessages(channelId: String) {
        listenerRegistration = messageRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error
                return@addSnapshotListener
            }

            val messageList = mutableListOf<Message>()
            snapshot?.documents?.forEach { document ->
                val content = document.getString("content")
                val channel = document.getString("channel")
                val time = document.getTimestamp("time")

                content?.let { c ->
                    channel?.let { ch ->
                        time?.let { t ->
                            messageList.add(
                                Message(
                                    content = c,
                                    channel = ch,
                                    time = t
                                )
                            )
                        }
                    }
                }
            }

            fetchedMessages.value = messageList
            if (messageList.isEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    messageRef.add(
                        hashMapOf(
                            "content" to "initial_text",
                            "time" to Timestamp.now(),
                            "channel" to channelId
                        )
                    ).addOnSuccessListener {
                        this.cancel()
                    }
                }
            }

        }
    }

    fun stopListeningForMessages() {
        listenerRegistration?.remove()
    }
    fun putMessage(message: Message){
        val tempList = mutableListOf<Message>()
        tempList.addAll(fetchedMessages.value)
        tempList.add(message)
        fetchedMessages.value = tempList

        CoroutineScope(Dispatchers.IO).launch {
            if (tempList.isNotEmpty()){
                messageRef.add(
                    hashMapOf(
                        "content" to message.content,
                        "time" to message.time,
                        "channel" to message.channel
                    )
                ).addOnSuccessListener {
                    this.cancel()
                }
            }
        }
    }


}