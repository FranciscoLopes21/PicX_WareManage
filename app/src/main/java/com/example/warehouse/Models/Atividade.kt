package com.example.warehouse.Models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Atividade (
    var idAtividade : String = "",
    var nomeAtividade : String = "",
    var descricao : String = "",
    var ativo : Boolean = true,
    var dataFinalizacao: Timestamp? = null
){
    // Construtor vazio necessário para a desserialização do Firestore
    constructor() : this("", "", "",  true, null)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idAtividade" to idAtividade,
            "nomeAtividade" to nomeAtividade,
            "descricao" to descricao,
            "ativo" to ativo,
            "dataFinalizacao" to dataFinalizacao
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toFinalizarMap(): Map<String, Any?> {
        val dataAtual = Timestamp.now()
        return mapOf("dataFinalizacao" to dataAtual)
    }

}