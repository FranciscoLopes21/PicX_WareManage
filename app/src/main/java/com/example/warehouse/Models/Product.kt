package com.example.warehouse.Models

class Product(
    var idProduct: String = "",
    var nomeProd: String = "",
    var referenciaProd: String = "",
    var quantProd: Int = 0,
    var quantMinProd: Int = 0,
    var warehouseId: String = "",
){
    // Construtor vazio necessário para a desserialização do Firestore
    constructor() : this("", "", "",0, 0,"")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "idProduct" to idProduct,
            "nomeProd" to nomeProd,
            "referenciaProd" to referenciaProd,
            "quantProd" to quantProd,
            "quantMinProd" to quantMinProd,
            "warehouseId" to warehouseId
        )
    }

    fun toUpdateMap(nomeUpdate: String, referenciaProd: String, quantUpdate: Int, quantMinProd: Int): Map<String, Any?> {
        return mapOf(
            "nomeProd" to nomeUpdate,
            "referenciaProd" to referenciaProd,
            "quantProd" to quantUpdate,
            "quantMinProd" to quantMinProd
        )
    }

    fun toUpdateQuantMap(quantUpdate: Int): Map<String, Any?> {
        return mapOf(
            "quantProd" to quantUpdate
        )
    }
}