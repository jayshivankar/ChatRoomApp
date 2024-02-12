package com.example.chatroomapp

import com.google.firebase.firestore.FirebaseFirestore
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MessageRepository (private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(roomId: String, message: Message): Result<Unit> = try {
        firestore.collection("rooms").document(roomId).collection("messages").add(message).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }



    fun getChatMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = firestore.collection("rooms").document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                trySend(it.documents.map { doc->
                        doc.toObject(Message::class.java)!!.copy()
                    }).isSuccess
                }
            }

        awaitClose { subscription.remove() }
    }
}

class MessageViewModel :ViewModel() {

    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepository(FirebaseAuth.getInstance(), Injection.instance())
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    val senderFirstName =MutableLiveData<String>()

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success<*> -> _currentUser.value = result.data
                is Error -> {
                }
            }
        }

    }

    fun loadMessages() {
        viewModelScope.launch {
            if (_roomId.value != null) {
                messageRepository.getChatMessages(_roomId.value.toString()).collect {
                    _messages.value = it
                }
            }
        }
    }

    fun sendMessage(text:String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email, 
                text =text
            )
            viewModelScope.launch {
                when
                        (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is Result.Success -> Unit
                    is Result.Error -> {


                    }
                }
            }
        }
    }
    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }




}







