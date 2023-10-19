package com.example.warehouse

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.AtividadeListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class WarehouseAdapter(
    val warehouseList: ArrayList<Warehouse>,
    private val warehouseViewModel: WarehouseViewModel
) :
    RecyclerView.Adapter<WarehouseAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeWarehouse)
        val desc: TextView = itemView.findViewById(R.id.txtDescWarehouse)
        val ibtn: ImageView = itemView.findViewById(R.id.ibtn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_warehouse, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return warehouseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val warehouse = warehouseList[position]
        holder.name.text = warehouseList[position].name.toString()
        holder.desc.text = warehouseList[position].desc.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductsWarehousesActivity::class.java)
            intent.putExtra("warehouseID", warehouse.id)
            intent.putExtra("warehouseName", warehouse.name)
            intent.putExtra("warehouseDesc", warehouse.desc)
            holder.itemView.context.startActivity(intent)
        }

        val bottomDialog = BottomSheetDialog(holder.itemView.context)

        holder.ibtn.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val context = holder.itemView.context

            // Infla o layout do BottomSheet usando o LayoutInflater do contexto do item
            val bottomView = LayoutInflater.from(context).inflate(R.layout.bottomsheetdialog_warehouse, null)

            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
            var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
            var campoWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtWareHouse)
            var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputWareHouseName)
            var campoDescWarehouse = bottomView.findViewById<TextInputEditText>(R.id.EdtDescWareHouse)

            campoWarehouse.setText(warehouseList[position].name.toString())
            campoDescWarehouse.setText(warehouseList[position].desc.toString())
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
                    showCreateWarehouseDialog(warehouse.id, nomeWarehouse, descWarehouse)
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
    }

    fun getWarehouseAt(position: Int): Warehouse {
        return warehouseList[position]
    }

    fun deleteItem(position: Int) {
        val deleteWarehouse = warehouseList.removeAt(position)
        notifyItemRemoved(position)

        Log.d("WarehouseAdapter", "Deleted warehouse ID: ${deleteWarehouse.id}")

    }

    private fun showCreateWarehouseDialog(idWarehouse: String, campoWarehouse: String, campoDescWarehouse: String) {

        val nameWarehouse = campoWarehouse
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            warehouseViewModel.updateWarehouseName(idWarehouse, nameWarehouse,campoDescWarehouse)
        }

    }

    private fun showDialog(){



    }

}