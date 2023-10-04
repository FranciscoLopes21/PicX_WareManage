package com.example.warehouse.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse.Models.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products
    private val produto = Product()


    fun createProducts(nomeProd: String, referenciaProd: String, quantProd: Int, quantMinProd: Int, warehouseId: String) {

        val product = Product("", nomeProd, referenciaProd, quantProd, quantMinProd, warehouseId) // Usando a classe Warehouse
        val productData = product.toMap()

        db.collection("products")
            .add(productData) // Usando o método toMap() da classe Warehouse
            .addOnSuccessListener {
                // O armazém foi criado com sucesso
            }
            .addOnFailureListener { e ->
                // Trate o erro de criação do armazém
            }
    }

    fun loadUserProducts(warehouseId: String) {
        db.collection("products")
            .whereEqualTo("warehouseId", warehouseId)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Trate o erro
                    return@addSnapshotListener
                }
                val productsList = mutableListOf<Product>()
                for (document in querySnapshot!!) {
                    val product = document.toObject(Product::class.java)
                    product?.let {
                        it.idProduct = document.id // Salvar o ID do documento no objeto Warehouse
                        productsList.add(it)
                    }
                }
                _products.value = productsList
            }
    }

    fun updateProduct(productId: String, nameUpdate :String, referenciaProd: String, quantUpdate : Int, quantMinProd: Int){

        val map = produto.toUpdateMap(nameUpdate, referenciaProd, quantUpdate, quantMinProd)

        db.collection("products")
            .document(productId)
            .update(map)
            .addOnSuccessListener {
                // A atualização foi bem-sucedida.
            }
            .addOnFailureListener { e ->
                // Trate o erro de atualização.
            }

    }

    fun loadProductsByWarehouse(warehouseId: String) {
        // Use uma consulta para obter os produtos com base na warehouse selecionada
        db.collection("products")
            .whereEqualTo("warehouseId", warehouseId)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Trate o erro
                    return@addSnapshotListener
                }
                val productsList = mutableListOf<Product>()
                for (document in querySnapshot!!) {
                    val product = document.toObject(Product::class.java)
                    product?.let {
                        it.idProduct = document.id
                        productsList.add(it)
                    }
                }
                _products.value = productsList
            }
    }

    fun updateQuantProduct(productId: String, quantUpdate : Int){

        val map = produto.toUpdateQuantMap(quantUpdate)

        db.collection("products")
            .document(productId)
            .update(map)
            .addOnSuccessListener {
                // A atualização foi bem-sucedida.
            }
            .addOnFailureListener { e ->
                // Trate o erro de atualização.
            }

    }

}

