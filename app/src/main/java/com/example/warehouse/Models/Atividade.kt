package com.example.warehouse.Models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Atividade (
    var idAtividade : String = "",
    var nomeAtividade : String = "",
    var descricao : String = "",
    var ativo : Boolean = true,
    var dataFinalizacao: LocalDate? = null
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
        val dataFinalizacao = LocalDate.now()
        val formattedDate = dataFinalizacao.toString()
        return mapOf(
            "dataFinalizacao" to formattedDate
        )
    }

}