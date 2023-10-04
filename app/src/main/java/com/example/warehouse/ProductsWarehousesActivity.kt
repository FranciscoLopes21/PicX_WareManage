package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.ProductViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductsWarehousesActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var warehouseViewModel: WarehouseViewModel
    private lateinit var atividadeViewModel: AtividadeViewModel
    lateinit var idFABAdd : FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter : ProductAdapter


    private lateinit var ibtn_backMain : ImageButton
    private lateinit var txtNomeWarehouse : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_warehouses)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)
        warehouseViewModel =ViewModelProvider(this).get(WarehouseViewModel::class.java)

        var warehouseId = intent.getStringExtra("warehouseID").toString()
        var nomeWarehouse = intent.getStringExtra("warehouseName").toString()

        recyclerView = findViewById(R.id.rcl_products)
        idFABAdd = findViewById(R.id.idFABAdd)
        ibtn_backMain = findViewById(R.id.ibtn_backMain)
        txtNomeWarehouse = findViewById(R.id.txtNomeWarehouse)

        recyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductAdapter(ArrayList()){ product ->
            // Aqui, o código para abrir o BottomSheetDialog e editar o produto
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheetdialog_product, null)

            // Configurar elementos no BottomSheetDialog com base no produto clicado
            val titulo = bottomSheetView.findViewById<TextView>(R.id.txtNewProduct)
            val nomeProdutoEditText = bottomSheetView.findViewById<TextInputEditText>(R.id.EdtProduct)
            val EdtRefProduct = bottomSheetView.findViewById<TextInputEditText>(R.id.EdtRefProduct)
            val quantProdutoEditText = bottomSheetView.findViewById<TextInputEditText>(R.id.EdtQuantProd)
            val quantMinProdEditText = bottomSheetView.findViewById<TextInputEditText>(R.id.EdtQuantMinProd)
            val saveButton = bottomSheetView.findViewById<MaterialButton>(R.id.Btn_Confirm)

            // Preencher os campos com os dados do produto
            titulo.text = "Edit Product"
            saveButton.text = "Save changes"
            nomeProdutoEditText.setText(product.nomeProd)
            EdtRefProduct.setText(product.referenciaProd.toString())
            quantProdutoEditText.setText(product.quantProd.toString())
            quantMinProdEditText.setText(product.quantMinProd.toString())

            // Configurar o botão de salvar
            saveButton.setOnClickListener {
                // Aqui, você pode obter os valores dos campos de edição e salvar as alterações no produto
                val novoNomeProduto = nomeProdutoEditText.text.toString()
                val novoRefProduto = EdtRefProduct.text.toString()
                val novaQuantProduto = quantProdutoEditText.text.toString().toInt()
                val novaQuantMinProd = quantMinProdEditText.text.toString().toInt()

                // Atualizar o produto com os novos valores
                productViewModel.updateProduct(product.idProduct, novoNomeProduto, novoRefProduto, novaQuantProduto, novaQuantMinProd)

                // Fechar o BottomSheetDialog após salvar
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
        recyclerView.adapter = productAdapter

        productViewModel.products.observe(this) { products ->

            Log.d("ProductViewModel", "Product updated: ${products.size}")

            // Atualizar diretamente a lista do adaptador
            productAdapter.productList.clear()
            productAdapter.productList.addAll(products)
            productAdapter.notifyDataSetChanged()
        }

        productViewModel.loadUserProducts(warehouseId)
        warehouseViewModel.getWarehouseNameById(warehouseId,txtNomeWarehouse)
        //txtNomeWarehouse.text = nomeWarehouse

        ibtn_backMain.setOnClickListener {
            val activity = Intent(this, MainActivity::class.java)
            startActivity(activity)
        }

        idFABAdd.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_product, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
        var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputProductName)

        var campoWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtProduct)
        val CampoRefProduct = bottomView.findViewById<TextInputEditText>(R.id.EdtRefProduct)
        var campoDescWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtQuantProd)
        var campoMinProd = bottomView.findViewById<TextInputEditText>(R.id.EdtQuantMinProd)

        var warehouseId = intent.getStringExtra("warehouseID").toString()


        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            var nomeProduct = campoWarehouse.text.toString()
            var refProduct = CampoRefProduct.text.toString()
            var quatProduct = campoDescWarehouse.text.toString()
            val MinProd = campoMinProd.text.toString()
            if(nomeProduct.isEmpty()){
                layoutWarehouse.error = "Campo vazio"
            }else {
                showCreateProductDialog(nomeProduct, refProduct, quatProduct, MinProd, warehouseId)
                bottomDialog.dismiss()
            }


        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.

        // on below line we are setting
        // content view to our view.
        bottomDialog.setContentView(bottomView)

        // on below line we are calling
        // a show method to display a dialog.
        bottomDialog.show()

    }

    private fun showCreateProductDialog(nomeProduct: String, refProduct: String, quatProduct: String, quatMinProduct: String, warehouseId: String) {

        val nameWarehouse = nomeProduct.toString()

        val  quant : Int = quatProduct.toInt()
        val quantmin : Int = quatMinProduct.toInt()
        productViewModel.createProducts(nomeProduct, refProduct, quant, quantmin, warehouseId)


    }

    private fun showDialogActivity(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_atividade, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
        var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputActivityName)
        var campoAtividae = bottomView.findViewById<TextInputEditText>(R.id.EdtAtividade)

        var warehouseId = intent.getStringExtra("warehouseID").toString()


        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            var nomeAtividade = campoAtividae.text.toString()
            if(nomeAtividade.isEmpty()){
                layoutWarehouse.error = "Campo vazio"
            }else {
                showCreateActivityDialog(nomeAtividade)
                bottomDialog.dismiss()
            }


        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.

        // on below line we are setting
        // content view to our view.
        bottomDialog.setContentView(bottomView)

        // on below line we are calling
        // a show method to display a dialog.
        bottomDialog.show()

    }

    private fun showCreateActivityDialog(nomeAtividade: String) {

        atividadeViewModel.createAtividades(nomeAtividade)


    }

}