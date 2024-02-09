package com.example.chatroomapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class RoomRepository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String): Result<Unit> = try {
        val room = Room(name = name)
        firestore.collection("rooms").add(room).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }
}

class RoomViewModel : ViewModel(){
    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms
    private val roomRepository:RoomRepository
    init{
        roomRepository = RoomRepository(Injection.instance())
        loadRooms()
    }
    fun createRoom(name:String){
        viewModelScope.launch{
            roomRepository.createRoom(name)

        }
    }
    fun loadRooms(){
        viewModelScope.launch {
            when (val result = roomRepository.getRooms()){
                is Result.Success -> _rooms.value = result.data
                is Error->{

                }

                else -> {}
            }
        }
    }


}