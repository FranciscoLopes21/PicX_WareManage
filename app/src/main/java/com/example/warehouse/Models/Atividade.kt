package com.example.warehouse.Models

class Atividade (
    var idAtividade : String = "",
    var nomeAtividade : String = "",
    var descricao : String = "",
    var ativo : Boolean = true
){
    // Construtor vazio necessário para a desserialização do Firestore
    constructor() : this("", "", "",  true)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idAtividade" to idAtividade,
            "nomeAtividade" to nomeAtividade,
            "descricao" to descricao,
            "ativo" to ativo
        )
    }
}