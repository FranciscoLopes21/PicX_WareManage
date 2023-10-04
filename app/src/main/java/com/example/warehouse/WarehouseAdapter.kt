package com.example.warehouse

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import java.time.LocalDate
import java.time.LocalTime

class WarehouseAdapter(val warehouseList: ArrayList<Warehouse>) :
    RecyclerView.Adapter<WarehouseAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeWarehouse)
        val desc: TextView = itemView.findViewById(R.id.txtDescWarehouse)
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
    }

    fun getWarehouseAt(position: Int): Warehouse {
        return warehouseList[position]
    }

    fun deleteItem(position: Int) {
        val deleteWarehouse = warehouseList.removeAt(position)
        notifyItemRemoved(position)

        Log.d("WarehouseAdapter", "Deleted warehouse ID: ${deleteWarehouse.id}")

    }

}