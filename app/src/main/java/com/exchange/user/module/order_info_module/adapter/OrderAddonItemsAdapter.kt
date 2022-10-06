package com.exchange.user.module.order_info_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderAddonItemsBinding
import com.exchange.user.module.order_info_module.model.OrderItemsAddOnList

class OrderAddonItemsAdapter(var context: Context, var itemAddOnList: List<OrderItemsAddOnList>) :   RecyclerView.Adapter<OrderAddonItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):ViewHolder {
        val adapterOrderAddonItemsbinding : AdapterOrderAddonItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_order_addon_items, parent, false)
        return ViewHolder(adapterOrderAddonItemsbinding)
    }

    override fun getItemCount(): Int {
        return itemAddOnList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterOrderAddonItemsbinding.itemsName.text = "■ ${itemAddOnList.get(holder.adapterPosition).name}"
        if (!itemAddOnList.get(holder.adapterPosition).addOnOptions.isNullOrEmpty()) {
            holder.adapterOrderAddonItemsbinding.addonOptionitems.layoutManager = LinearLayoutManager(context)
            holder.adapterOrderAddonItemsbinding.addonOptionitems.adapter = OrderAddONOptionAdapter(context, itemAddOnList.get(holder.adapterPosition).addOnOptions!!)
        }


    }
    inner class ViewHolder(val adapterOrderAddonItemsbinding : AdapterOrderAddonItemsBinding) : RecyclerView.ViewHolder(adapterOrderAddonItemsbinding.root)

}