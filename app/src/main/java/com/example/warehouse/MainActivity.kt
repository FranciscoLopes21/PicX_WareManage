package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var warehouseViewModel: WarehouseViewModel
    private lateinit var atividadeViewModel: AtividadeViewModel
    lateinit var idFABAdd : FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var warehouseList: ArrayList<Warehouse>
    private lateinit var warehouseAdapter : WarehouseAdapter
    private var db = Firebase.firestore
    private lateinit var cardAtividadeCreate : CardView
    private lateinit var txtTotalWarehouses: TextView

    private val staticWarehouseList = listOf(
        Warehouse("1", "Armazém 1", "user1"),
        Warehouse("2", "Armazém 2", "user1"),
        Warehouse("3", "Armazém 3", "user2"),
        // Adicione mais itens aqui...
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        warehouseViewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)

        recyclerView = findViewById(R.id.rcl_warehouses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cardAtividadeCreate = findViewById(R.id.cardAtividadeCreate)
        txtTotalWarehouses = findViewById(R.id.txtTotalWarehouses)
        idFABAdd = findViewById(R.id.idFABAdd)

        val currentUser = FirebaseAuth.getInstance().currentUser
        var id = currentUser?.uid
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()

        warehouseAdapter = WarehouseAdapter(ArrayList())
        recyclerView.adapter = warehouseAdapter

        val swipeToDeleteCallback = SwipeToDeleteCallback(warehouseAdapter, warehouseViewModel)
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        Log.d("WarehouseViewModel", warehouseViewModel.getNWarehouses(txtTotalWarehouses).toString())

        warehouseViewModel.warehouses.observe(this) { warehouses ->

            Log.d("WarehouseViewModel", "Warehouses updated: ${warehouses.size}")

            // Atualizar diretamente a lista do adaptador
            warehouseAdapter.warehouseList.clear()
            warehouseAdapter.warehouseList.addAll(warehouses)
            warehouseAdapter.notifyDataSetChanged()
        }

        warehouseViewModel.loadUserWarehouses()

        //idFABAdd = findViewById(R.id.idFABAdd)
        warehouseViewModel.getNWarehouses(txtTotalWarehouses)

        idFABAdd.setOnClickListener{

            showDialog()

        }

        cardAtividadeCreate.setOnClickListener {
            showDialogAtividade()
        }

    }

    private fun showDialog(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_warehouse, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
        var campoWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtWareHouse)
        var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputWareHouseName)
        var campoDescWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtDescWareHouse)



        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            var nomeWarehouse = campoWarehouse.text.toString()
            var descWarehouse = campoDescWarehouse.text.toString()
            if(nomeWarehouse.isEmpty()){
                layoutWarehouse.error = "Campo vazio"
            }else {
                showCreateWarehouseDialog(nomeWarehouse, descWarehouse)
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

    private fun showCreateWarehouseDialog(campoWarehouse: String, campoDescWarehouse: String) {

        val nameWarehouse = campoWarehouse
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            warehouseViewModel.createWarehouse(nameWarehouse,campoDescWarehouse, userId)
        }

    }

    private fun checkAuthentication(): Boolean {
        var auth = firebaseAuth.currentUser.toString()

        return auth.isNotEmpty()
    }


    private fun showDialogAtividade(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_atividade, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
        var campoAtividade = bottomView.findViewById<TextInputEditText>(R.id.EdtAtividade)
        var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputActivityName)



        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            var nomeAtividade = campoAtividade.text.toString()
            if(nomeAtividade.isEmpty()){
                layoutWarehouse.error = "Campo vazio"
            }else {
                showCreateAtividadeDialog(nomeAtividade)
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

    private fun showCreateAtividadeDialog(nomeAtividade: String) {

        val nameAt = nomeAtividade
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            atividadeViewModel.createAtividades(nameAt)
        }

        atividadeViewModel.atividadeId.observe(this, Observer { atividadeId ->
            val intent = Intent(this, AtividadeProduto::class.java)
            intent.putExtra("atividadeId", atividadeId)
            intent.putExtra("nomeAtividade", nomeAtividade)
            startActivity(intent)
        })

    }

}