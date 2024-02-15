package com.example.chatroomapp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await






class UserRepository (private val auth:FirebaseAuth,private val firestore: FirebaseFirestore ){
    suspend fun signUp(email:String,password:String,firstname:String,lastname:String):Result<Boolean> =
        try{
            auth.createUserWithEmailAndPassword(email,password).await()
            val user = User(firstname,lastname,email)
            saveUserToFirestore(user)
            // adding user to firestore
            Result.Success(true)
        } catch (e:Exception){
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user:User){
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email:String,password:String):
            Result<Boolean> = try{
        auth.signInWithEmailAndPassword(email,password).await()
        Result.Success(true)
    }catch (e:Exception){
        Result.Error(e)
    }



}

