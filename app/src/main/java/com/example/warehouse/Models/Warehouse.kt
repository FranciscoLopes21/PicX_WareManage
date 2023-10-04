package com.example.Models

class Warehouse (
    var id: String = "",
    val name: String = "",
    val desc: String = "",
    val userId: String = ""
) {
    // Construtor vazio necessário para a desserialização do Firestore
    constructor() : this("", "", "", "")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "desc" to desc,
            "userId" to userId
        )
    }

    override fun toString(): String {
        return name
    }
}