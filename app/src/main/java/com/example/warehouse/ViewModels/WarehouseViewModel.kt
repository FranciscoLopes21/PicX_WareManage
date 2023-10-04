package com.example.ViewModels

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.Models.Warehouse
import com.google.firebase.auth.FirebaseAuth

class WarehouseViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _warehouses = MutableLiveData<List<Warehouse>>()
    val warehouses: LiveData<List<Warehouse>> get() = _warehouses



    fun createWarehouse(warehouseName: String, DescWarehouse: String, userId: String) {



        val warehouse = Warehouse("", warehouseName, DescWarehouse, userId) // Usando a classe Warehouse
        val warehouseData = warehouse.toMap()

        db.collection("warehouses")
            .add(warehouseData) // Usando o método toMap() da classe Warehouse
            .addOnSuccessListener {
            // O armazém foi criado com sucesso
            }
            .addOnFailureListener { e ->
                // Trate o erro de criação do armazém
            }
    }

    fun loadUserWarehouses() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            db.collection("warehouses")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        // Trate o erro
                        return@addSnapshotListener
                    }
                    val warehousesList = mutableListOf<Warehouse>()
                    for (document in querySnapshot!!) {
                        val warehouse = document.toObject(Warehouse::class.java)
                        warehouse?.let {
                            it.id = document.id // Salvar o ID do documento no objeto Warehouse
                            warehousesList.add(it)
                        }
                    }
                    _warehouses.value = warehousesList
                }
        }
    }

    fun deleteWarehouse(warehouse: Warehouse) {
        val warehouseRef = db.collection("warehouses").document(warehouse.id)

        warehouseRef.delete()
            .addOnSuccessListener {
                Log.d("WarehouseViewModel", "Warehouse deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("WarehouseViewModel", "Error deleting warehouse: ${e.message}", e)
            }
    }

    fun updateWarehouseName(warehouseId: String, newName: String) {
        val warehouseRef = db.collection("warehouses").document(warehouseId)

        val updates = hashMapOf<String, Any>(
            "name" to newName
        )

        warehouseRef
            .update(updates)
            .addOnSuccessListener {
                // A atualização foi bem-sucedida
                Log.d("WarehouseViewModel", "Nome da Warehouse atualizado com sucesso")
            }
            .addOnFailureListener { e ->
                // Trate o erro de atualização
                Log.e("WarehouseViewModel", "Erro ao atualizar nome da Warehouse: ${e.message}", e)
            }
    }

    fun getWarehouseNameById(warehouseId: String, txtNomeWarehouse: TextView) {

        db.collection("warehouses")
            .document(warehouseId)
            .get()
            .addOnSuccessListener { documentSnapshot  ->

                txtNomeWarehouse.text = documentSnapshot.getString("name")
                // Faça o que desejar com o número de armazéns
            }
            .addOnFailureListener { e ->
                // Trate o erro, se ocorrer algum
                println("Erro ao buscar armazéns: $e")
            }

    }

    fun getNWarehouses(txtTotalWarehouses: TextView): Int {


        var numberOfWarehouses = 0
        // O usuário está autenticado, você pode usar user.uid para obter o ID do usuário

        // Consulta para buscar armazéns associados ao ID do usuário autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            db.collection("warehouses")
                .whereEqualTo(
                    "userId",
                    userId
                )// Substitua "userId" pelo nome do campo que armazena o ID do usuário em seus documentos de armazém
                .get()
                .addOnSuccessListener { documents ->
                    // A consulta foi bem-sucedida, você pode contar o número de documentos
                    numberOfWarehouses = documents.size()
                    txtTotalWarehouses.text = numberOfWarehouses.toString()
                    // Faça o que desejar com o número de armazéns
                    println("Número de armazéns: $numberOfWarehouses")
                }
                .addOnFailureListener { e ->
                    // Trate o erro, se ocorrer algum
                    println("Erro ao buscar armazéns: $e")
                }

        }
        return numberOfWarehouses
    }
}