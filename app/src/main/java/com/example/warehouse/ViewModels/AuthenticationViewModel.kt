package com.example.warehouse.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun sigIn(){

    }

    fun signOut(){
        auth.signOut()
    }

}