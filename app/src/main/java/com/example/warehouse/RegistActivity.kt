package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegistActivity : AppCompatActivity() {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPass: TextInputEditText
    private lateinit var edtPassConf: TextInputEditText
    private lateinit var btnRegistry: MaterialButton
    private lateinit var edtEmailLayout: TextInputLayout
    private lateinit var edtPassLayout: TextInputLayout
    private lateinit var edtPassConfLayout: TextInputLayout
    private lateinit var ibtn_backLogin: ImageButton

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        edtEmail = findViewById(R.id.Edt_Email)
        edtPass = findViewById(R.id.Edt_Pass)
        edtPassConf = findViewById(R.id.Edt_PassConf)
        btnRegistry = findViewById(R.id.Btn_Registar)
        ibtn_backLogin = findViewById(R.id.ibtn_backLogin)


        btnRegistry.setOnClickListener {
            var valorEmailChack = checkEmailInput(edtEmail)
            var valorPasswordCheck = checkPasswordInput(edtPass,edtPassConf)

            if (valorEmailChack && valorPasswordCheck){
                checkInputs(edtEmail, edtPass)
            }
        }

        ibtn_backLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkInputs(edtEmail: TextInputEditText, edtPass: TextInputEditText) {

        var campoEmail = this.edtEmail.text.toString()
        var campoPass = this.edtPass.text.toString()
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(campoEmail, campoPass).addOnCompleteListener {
            if (it.isSuccessful){
                val intent = Intent(this, DashBoardActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkEmailInput(edtEmail: TextInputEditText) : Boolean{
        edtEmailLayout = findViewById(R.id.txtInputEmail)
        var campoEmail = edtEmail.text.toString()
        var valor = false
        if(campoEmail.isNotEmpty()){
            if(Patterns.EMAIL_ADDRESS.matcher(campoEmail).matches()){
                edtEmailLayout.error = null
                valor = true
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(campoEmail).matches())
            {
                valor = false
                edtEmailLayout.error= "*Campo email inválido"
            }
        }else{
            valor = false
            edtEmailLayout.error= "*Campo email vazio"
        }

        return valor
    }
    private fun checkPasswordInput(edtPass: TextInputEditText, edtPassConf: TextInputEditText) : Boolean{
        edtPassLayout = findViewById(R.id.txtInputPassword)
        edtPassConfLayout = findViewById(R.id.txtInputConfPassword)
        var campoPass = edtPass.text.toString()
        var campoPassConf = edtPassConf.text.toString()
        var valor = false
        if(campoPass.isNotEmpty() && campoPassConf.isNotEmpty()){
            if(campoPass.length < 8){
                edtPassLayout.error= "*Password tem de ter mais de 8 caracters"
                valor = false
                /*edtPassLayout.error = null
                edtPassConfLayout.error = null
                valor = true*/
            }
            else if (!campoPass.matches(".*[A-Z].*".toRegex())){
                edtPassLayout.error= "*Password não contém letras maiusculas"
                valor = false
            }
            else if (!campoPass.matches(".*[a-z].*".toRegex())){
                edtPassLayout.error= "*Password não contém letras minusculas"
                valor = false
            }
            else if (!campoPass.matches(".*[@#\$%^&+=!].*".toRegex())){
                edtPassLayout.error= "*Password não contém caracteres especiais"
                valor = false
            }
            else if (!campoPass.matches(".*[0-9].*".toRegex())){
                edtPassLayout.error= "*Password não contém números"
                valor = false
            }
            else {
                valor = true
                edtPassLayout.error = null
                edtPassConfLayout.error = null
            }
        }else{
            valor = false
            edtPassLayout.error= "*Campo password vazio"
            edtPassConfLayout.error= "*Campo confirmar password vazio"
        }
        if(!campoPassConf.matches(campoPass.toRegex())){
            valor = false
            edtPassLayout.error= "*Campos password não compativeis"
            edtPassConfLayout.error= "*Campos password não compativeis"
        }

        return valor
    }

}