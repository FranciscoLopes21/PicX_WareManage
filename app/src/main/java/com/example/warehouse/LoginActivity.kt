package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var btn_regist : TextView
    lateinit var btnLogin : Button
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPass: TextInputEditText

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_regist = findViewById(R.id.btn_regist)
        btnLogin = findViewById(R.id.btn_Login)

        btn_regist.setOnClickListener {
            openActivity()
        }

        btnLogin.setOnClickListener {
            openAvtivityLogin()
        }

    }

    private fun openActivity() {

        val activity = Intent(this, RegistActivity::class.java)
        //intent.putExtra("key", value)
        startActivity(activity)
        finish()
        overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom,
            androidx.appcompat.R.anim.abc_slide_out_top)

    }

    private fun openAvtivityLogin(){

        edtEmail = findViewById(R.id.EdtEmail)
        edtPass = findViewById(R.id.EdtPass)
        var campoEmail = edtEmail.text.toString()
        var campoPass = edtPass.text.toString()

        firebaseAuth = FirebaseAuth.getInstance()
        if (campoEmail.isNotEmpty() && campoPass.isNotEmpty()){

            firebaseAuth.signInWithEmailAndPassword(campoEmail, campoPass).addOnCompleteListener {
                if (it.isSuccessful){

                    val activity = Intent(this, MainActivity::class.java)
                    //intent.putExtra("key", value)
                    startActivity(activity)
                    finish()
                    overridePendingTransition(androidx.appcompat.R.anim.abc_slide_in_bottom,
                        androidx.appcompat.R.anim.abc_slide_out_top)
                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
        }
    }

}