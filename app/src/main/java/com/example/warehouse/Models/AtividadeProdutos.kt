package com.example.warehouse.Models

import java.time.LocalDate

class AtividadeProdutos(
    var idListProd: String = "",
    var idAtividade: String = "",
    var atividadeNome: String = "",
    var idProd: String = "",
    var prodNome: String = "",
    var quantProd: Int = 0,
    var idWarehouse: String = "",
    var warehouseNome: String = ""
){

    // Construtor vazio necessário para a desserialização do Firestore
    constructor() : this("", "", "")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idListProd" to idListProd,
            "idAtividade" to idAtividade,
            "atividadeNome" to atividadeNome,
            "idProd" to idProd,
            "prodNome" to prodNome,
            "quantProd" to quantProd,
            "idWarehouse" to idWarehouse,
            "warehouseNome" to warehouseNome
        )
    }

    fun toUpdateQuantMap(quantUpdate: Int): Map<String, Any?> {
        return mapOf(
            "quantProd" to quantUpdate
        )
    }
}