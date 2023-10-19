package com.example.warehouse.ViewModels

import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Models.Warehouse
import com.example.warehouse.Models.Atividade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

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

    fun getWarehouseNameById(atividadeId: String, txtNomeAtividade: TextView) {

        db.collection("atividade")
            .document(atividadeId)
            .get()
            .addOnSuccessListener { documentSnapshot  ->

                txtNomeAtividade.text = documentSnapshot.getString("nomeAtividade")
                // Faça o que desejar com o número de armazéns
            }
            .addOnFailureListener { e ->
                // Trate o erro, se ocorrer algum
                println("Erro ao buscar armazéns: $e")
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finalizarAtividade(atividadeId: String) {
        val map = atividade.toFinalizarMap()

        db.collection("atividade")
            .document(atividadeId)
            .update(map)
            .addOnSuccessListener {
                // A atualização foi bem-sucedida.
            }
            .addOnFailureListener { e ->
                // Trate o erro de atualização.
            }
    }

    // ...

    fun loadUserAtividades() {
        db.collection("atividade")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Trate o erro
                    return@addSnapshotListener
                }
                val atividadesList = mutableListOf<Atividade>()
                for (document in querySnapshot!!) {
                    val atividade = document.toObject(Atividade::class.java)
                    atividade?.let {
                        it.idAtividade = document.id
                        atividadesList.add(it)
                    }
                }
                _atividade.value = atividadesList
            }
    }
}