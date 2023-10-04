package com.example.warehouse.ViewModels

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse.Models.Atividade
import com.google.firebase.firestore.FirebaseFirestore

class AtividadeViewModel : ViewModel (){

    private val db = FirebaseFirestore.getInstance()
    private val _atividade = MutableLiveData<List<Atividade>>()
    private val _atividadeId = MutableLiveData<String>()
    val atividadeId: LiveData<String> get() = _atividadeId
    val atividades: LiveData<List<Atividade>> get() = _atividade
    private val atividade = Atividade()

    // Adicione uma variável MutableLiveData para o nome da atividade
    private val _nomeAtividade = MutableLiveData<String>()
    val nomeAtividade: LiveData<String> get() = _nomeAtividade

    fun createAtividades(nomeAt: String) {

        val atividade = Atividade("", nomeAt, "descAt", true) // Usando a classe Warehouse
        val atividadeData = atividade.toMap()

        db.collection("atividade")
            .add(atividadeData) // Usando o método toMap() da classe Warehouse
            .addOnSuccessListener {
                    documentReference ->
                val atividadeId = documentReference.id
                _atividadeId.value = atividadeId
                // O armazém foi criado com sucesso
            }
            .addOnFailureListener { e ->
                // Trate o erro de criação do armazém
            }
    }

    // Método para buscar o nome da atividade
    fun buscarNomeAtividade(atividadeId: String, txtNomeAtividade: TextView) {
        db.collection("atividade")
            .document(atividadeId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nomeAtividade = documentSnapshot.getString("nomeAtividade")
                    txtNomeAtividade.text = nomeAtividade
                    //_nomeAtividade.value = nomeAtividade
                } else {
                    // Trate o caso em que o documento não existe
                }
            }
            .addOnFailureListener { e ->
                // Trate o erro, se houver algum
            }
    }

    fun deleteWarehouse(atividadeId: String) {
        val warehouseRef = db.collection("atividade").document(atividadeId)

        warehouseRef.delete()
            .addOnSuccessListener {
                Log.d("WarehouseViewModel", "Warehouse deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("WarehouseViewModel", "Error deleting warehouse: ${e.message}", e)
            }
    }

}