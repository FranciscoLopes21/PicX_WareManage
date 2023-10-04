package com.example.warehouse.ViewModels

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse.Models.Atividade
import com.example.warehouse.Models.AtividadeProdutos
import com.example.warehouse.Models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.checker.units.qual.A

class AtividadeListViewModel : ViewModel (){

    private val db = FirebaseFirestore.getInstance()
    private val _atividade = MutableLiveData<List<AtividadeProdutos>>()

    private val atividadeProdutos = AtividadeProdutos()
    val atividades: LiveData<List<AtividadeProdutos>> get() = _atividade
    private val atividade = Atividade()

    fun createAtividades(idAtividade: String, nomeat: String, idProd: String,
                         prodNome: String, quantProd: Int, IdWareouse: String){

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("atividadeList")
        val collectionWarehouse = db.collection("warehouses")

        // Crie a consulta para verificar se um documento com o mesmo idAtividade e idProduto existe.
        val query = collectionRef
            .whereEqualTo("idAtividade", idAtividade)
            .whereEqualTo("idProd", idProd)

        val queryWarehouse = collectionWarehouse
            .whereEqualTo("id", IdWareouse)


        // Execute a consulta e processe os resultados.
        query.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Nenhum documento correspondente encontrado, crie um novo documento.



                        val atividadeProdutos = AtividadeProdutos("", idAtividade, nomeat,
                            idProd, prodNome, quantProd, IdWareouse,"nomeWare")
                        val atividadeData = atividadeProdutos.toMap()
                        collectionRef.add(atividadeData)

                } else {
                    // Um documento correspondente foi encontrado, atualize a quantidade.
                    val documentoEncontrado = documents.first()
                    val quantidadeAtual = documentoEncontrado.get("quantProd") as? Long ?: 0
                    val novaQuantidade = quantidadeAtual + quantProd
                    documentoEncontrado.reference.update("quantProd", novaQuantidade)
                }
            }
            .addOnFailureListener { exception ->
                // Trate os erros, se houver algum.
            }

    }

    fun getNWarehouses(idAtividade: String, txtQuantProd: TextView): Int {


        var numberOfWarehouses = 0


        // Consulta para buscar armazéns associados ao ID do usuário autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            db.collection("atividadeList")
                .whereEqualTo(
                    "idAtividade",
                    idAtividade
                )// Substitua "userId" pelo nome do campo que armazena o ID do usuário em seus documentos de armazém
                .get()
                .addOnSuccessListener { documents ->
                    // A consulta foi bem-sucedida, você pode contar o número de documentos
                    numberOfWarehouses = documents.size()
                    txtQuantProd.text = numberOfWarehouses.toString()
                    // Faça o que desejar com o número de armazéns
                    println("Número de armazéns: $numberOfWarehouses")
                    Log.d("AtividadeListViewModel", "quantida : ${numberOfWarehouses}")
                }
                .addOnFailureListener { e ->
                    // Trate o erro, se ocorrer algum
                    println("Erro ao buscar armazéns: $e")
                }
        }
        return numberOfWarehouses


    }

    fun deleteWarehouse(idAtividade: String) {

        val collectionReference = db.collection("atividadeList")
        val productsRef = db.collection("products")
        val query = collectionReference.whereEqualTo("idAtividade", idAtividade)

        collectionReference.document(idAtividade)

        db.collection("atividadeList")
            .whereEqualTo("idAtividade", idAtividade)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    // Acesse o idProd do documento
                    val idProd = document.getString("idProd")
                    // Consulta para encontrar o documento do produto com base no idProd
                    db.collection("products")
                        .document(idProd!!)
                        .get()
                        .addOnSuccessListener { productDocument ->
                            if (productDocument != null && productDocument.exists()) {
                                // Documento do produto encontrado

                                val quantProd = (document.get("quantProd") as? Long)?.toInt() ?: 0

// Acesse a quantidade de produtos na lista de produtos
                                val quantProdListaProdutos = (productDocument.get("quantProd") as? Long)?.toInt() ?: 0

                                // Calcule a nova quantidade na lista de produtos
                                val novaQuantidadeProdutos = quantProdListaProdutos + quantProd

                                // Atualize a quantidade na lista de produtos
                                db.collection("products")
                                    .document(idProd)
                                    .update("quantProd", novaQuantidadeProdutos)
                                    .addOnSuccessListener {
                                        // Produto atualizado com sucesso

                                        // Agora exclua o documento da lista de atividades
                                        db.collection("atividadeList")
                                            .document(document.id)
                                            .delete()
                                            .addOnSuccessListener {
                                                // Atividade excluída com sucesso
                                            }
                                            .addOnFailureListener { exception ->
                                                // Trate os erros ao excluir a atividade
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        // Trate os erros ao atualizar a quantidade na lista de produtos
                                    }
                            } else {
                                // Produto não encontrado com base no idProd
                            }
                        }
                        .addOnFailureListener { exception ->
                            // Trate os erros ao buscar o documento do produto
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Trate os erros ao buscar a lista de atividades
            }
    }

    private fun updateProductQuantity(atividadeId: String, newQuantProd: Int) {
        // Implemente a lógica para atualizar a quantidade do produto existente aqui
        // Você precisa buscar o documento correspondente usando atividadeId,
        // atualizar o valor da quantidade e salvar o documento atualizado no Firestore.



    }

    fun loadUserProducts(warehouseId: String) {
        db.collection("atividadeList")
            .whereEqualTo("idAtividade", warehouseId)
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    // Trate o erro
                    return@addSnapshotListener
                }
                val productsList = mutableListOf<AtividadeProdutos>()
                for (document in querySnapshot!!) {
                    val product = document.toObject(AtividadeProdutos::class.java)
                    product?.let {
                        it.idListProd = document.id // Salvar o ID do documento no objeto Warehouse
                        productsList.add(it)
                    }
                }
                _atividade.value = productsList
            }
    }


}