package com.example.warehouse

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.warehouse.Models.Atividade
import java.text.SimpleDateFormat

class ListAtividadesAdapter (val atividadeList: ArrayList<Atividade>) :
    RecyclerView.Adapter<ListAtividadesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNomeAtividade: TextView = itemView.findViewById(R.id.txtNomeAtividade)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
        val txtDesc: TextView = itemView.findViewById(R.id.txtDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_atividades, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return atividadeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val atividade = atividadeList[position]
        holder.txtNomeAtividade.text = atividadeList[position].nomeAtividade.toString()
        holder.txtDesc.text = atividadeList[position].descricao.toString()
        // Adicionar formatação da data se a data não for nula
        val dateTimestamp = atividade.dataFinalizacao
        val formattedDate = if (dateTimestamp != null) {
            val date = dateTimestamp.toDate() // Converte Timestamp em Date
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            dateFormat.format(date) // Formata a data
        } else {
            "" // Trate o caso em que a data seja nula
        }

        holder.txtData.text = formattedDate
        /*holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductsWarehousesActivity::class.java)
            intent.putExtra("warehouseID", warehouse.id)
            intent.putExtra("warehouseName", warehouse.name)
            intent.putExtra("warehouseDesc", warehouse.desc)
            holder.itemView.context.startActivity(intent)
        }*/
    }

    fun getAtividadeAt(position: Int): Atividade {
        return atividadeList[position]
    }

    fun deleteItem(position: Int) {
        val deleteWarehouse = atividadeList.removeAt(position)
        notifyItemRemoved(position)

        Log.d("WarehouseAdapter", "Deleted warehouse ID: ${deleteWarehouse.idAtividade}")

    }
}