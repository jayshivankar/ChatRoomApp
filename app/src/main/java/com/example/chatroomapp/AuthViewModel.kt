package com.example.chatroomapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

object Injection{
    private val instance :FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    fun instance():FirebaseFirestore{
        return instance

    }
}



class AuthViewModel: ViewModel(){
    private var userRepository = UserRepository( FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()  )

    init {
        userRepository = UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )

    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult:LiveData<Result<Boolean>> get() = _authResult


    fun signUp(email:String,password:String,firstname:String,lastname:String){
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email,password,firstname,lastname)
        }
    }
    fun login(email:String,password:String){
        viewModelScope.launch {
            _authResult.value = userRepository.login(email,password)
        }
    }

}