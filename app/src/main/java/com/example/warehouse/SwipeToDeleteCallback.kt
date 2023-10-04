package com.example.warehouse

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ViewModels.WarehouseViewModel

class SwipeToDeleteCallback (
    private val adapter: WarehouseAdapter,
    private val warehouseViewModel: WarehouseViewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val warehouse = adapter.getWarehouseAt(position)
        val warehouseId = warehouse.id // Certifique-se de que o ID está sendo obtido corretamente
        Log.d("SwipeToDeleteCallback", "Warehouse ID to delete: $warehouseId")

        adapter.deleteItem(position)
        warehouseViewModel.deleteWarehouse(warehouse)
    }
}