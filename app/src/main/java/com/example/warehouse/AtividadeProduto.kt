package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.AtividadeListViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.example.warehouse.ViewModels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AtividadeProduto : AppCompatActivity() {

    private lateinit var ibtn_backMain: ImageButton
    private lateinit var Spin_warehouseName: Spinner
    private lateinit var warehouseViewModel: WarehouseViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var atividadeViewModel: AtividadeViewModel
    private lateinit var atividadeListViewModel: AtividadeListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var warehouseAdapter: ArrayAdapter<Warehouse>
    private lateinit var productAdapterAtividade: ProductAdapterAtividade
    private lateinit var txt_qunatProd: TextView
    private lateinit var btn_add_atividade: Button
    private var isConfirmExit = false
    var atividadeId = ""
    var nomeAtividade = ""
    var warehouseId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade_produto)

        warehouseViewModel = ViewModelProvider(this)[WarehouseViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)
        atividadeListViewModel = ViewModelProvider(this).get(AtividadeListViewModel::class.java)

        ibtn_backMain = findViewById(R.id.ibtn_backMain)
        Spin_warehouseName = findViewById(R.id.Spin_warehouseName)
        recyclerView = findViewById(R.id.rcl_products)
        txt_qunatProd = findViewById(R.id.txt_qunatProd)
        btn_add_atividade = findViewById(R.id.btn_add_atividade)

        atividadeId = intent.getStringExtra("atividadeId").toString()
        nomeAtividade = intent.getStringExtra("nomeAtividade").toString()
        if (atividadeId != null) {


            Toast.makeText(this, "$atividadeId", Toast.LENGTH_SHORT).show()
            // Faça o que quiser com o ID da atividade aqui na AtividadeActivity
        }

        productAdapterAtividade = ProductAdapterAtividade(ArrayList(), atividadeListViewModel, productViewModel, txt_qunatProd, atividadeId, nomeAtividade) { /* onItemClick logic here */ }

        warehouseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        warehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spin_warehouseName.adapter = warehouseAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapterAtividade

        atividadeViewModel.atividadeId.observe(this, Observer { atividadeId ->
            // Aqui você tem o ID da atividade, agora configure o adaptador
            initializeAdapter(atividadeId)
        })



        /*if (atividadeId != null) {
            atividadeViewModel.buscarNomeAtividade(atividadeId, txtNomeAtividade)
        }*/

        ibtn_backMain.setOnClickListener {
            // Mostrar um diálogo de confirmação antes de sair
            showExitConfirmationDialog()
        }

        // Configure um listener para o Spinner
        Spin_warehouseName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Aqui você pode lidar com a seleção do usuário, por exemplo, buscar produtos da warehouse selecionada
                val selectedWarehouse = warehouseAdapter.getItem(position)
                if (selectedWarehouse != null) {
                    // Faça algo com a warehouse selecionada, como buscar produtos
                    warehouseId = selectedWarehouse.id
                    // Chame o método no ViewModel para buscar produtos
                    // productViewModel.getProductsByWarehouseId(warehouseId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Implemente se necessário
            }
        }



        // Observar as warehouses no ViewModel e atualizar o Adapter quando elas mudarem
        warehouseViewModel.warehouses.observe(this, Observer { warehouses ->
            warehouseAdapter.clear()
            warehouseAdapter.addAll(warehouses)
            warehouseAdapter.notifyDataSetChanged()
        })

        // Carregue as warehouses do usuário logado
        warehouseViewModel.loadUserWarehouses()

        // Configurar o listener de seleção para o Spinner
        Spin_warehouseName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Obtenha o ID da warehouse selecionada com base na posição selecionada no Spinner
                val selectedWarehouseId = warehouseViewModel.warehouses.value?.get(position)?.id
                selectedWarehouseId?.let {
                    productViewModel.loadProductsByWarehouse(selectedWarehouseId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Nada foi selecionado
            }
        }

        // Observar os produtos no ViewModel e atualizar o Adapter quando eles mudarem
        productViewModel.products.observe(this) { products ->
            productAdapterAtividade.updateData(products)
        }


        btn_add_atividade.setOnClickListener {
            showDialog()
        }

    }

    private fun initializeAdapter(atividadeId: String) {
        productAdapterAtividade =
            ProductAdapterAtividade(ArrayList(), atividadeListViewModel, productViewModel, txt_qunatProd, atividadeId, nomeAtividade) { /* onItemClick logic here */ }
        recyclerView.adapter = productAdapterAtividade
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        // Defina o título e a mensagem do diálogo
        builder.setTitle(R.string.dialogTitle)
            .setMessage(R.string.dialogMessage)
            .setIcon(android.R.drawable.ic_dialog_alert)

        // Configurar a ação positiva (Yes)
        builder.setPositiveButton("Yes") { _, _ ->
            DeleteAtividade()
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
        }

        // Configurar ação negativa (No)
        builder.setNegativeButton("No") { _, _ ->
            // Nada a fazer, o diálogo será fechado
        }

        // Crie e exiba o diálogo
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun DeleteAtividade() {
        // Exclua a atividade com base no ID
        if (atividadeId != null) {
            atividadeViewModel.deleteWarehouse(atividadeId)
            atividadeListViewModel.deleteWarehouse(atividadeId)
        }
    }

    override fun onBackPressed() {
        // Mostrar o diálogo de confirmação antes de sair
        showExitConfirmationDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Certifique-se de excluir a atividade quando a Activity for destruída
        DeleteAtividade()
    }

    override fun onStop(){
        super.onStop()
        DeleteAtividade()
    }

    private fun showDialog(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_listaprodutos, null)
        var recyclerView: RecyclerView
        var productAtividadeListAdapter : ProductAtividadeListAdapter


        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        recyclerView = bottomView.findViewById(R.id.rcl_productslista)
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAtividadeListAdapter = ProductAtividadeListAdapter(ArrayList())
        recyclerView.adapter = productAtividadeListAdapter

        atividadeListViewModel.atividades.observe(this){atividades ->

            // Atualizar diretamente a lista do adaptador
            productAtividadeListAdapter.productatividadeList.clear()
            productAtividadeListAdapter.productatividadeList.addAll(atividades)
            productAtividadeListAdapter.notifyDataSetChanged()

        }

        atividadeListViewModel.loadUserProducts(atividadeId)

        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {

                bottomDialog.dismiss()

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

}