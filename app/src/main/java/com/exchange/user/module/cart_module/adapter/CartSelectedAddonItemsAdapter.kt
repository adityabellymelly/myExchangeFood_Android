package com.exchange.user.module.cart_module.model.action.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderAddonItemsBinding
import com.exchange.user.module.cart_module.adapter.CartSelectedAddonOptionAdapter
import com.exchange.user.module.cart_module.model.card_model.ItemsAddonList


class CartSelectedAddonItemsAdapter(var context: Context, private var itemAddOnList: ArrayList<ItemsAddonList>) : RecyclerView.Adapter<CartSelectedAddonItemsAdapter.ViewHolder>() {

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
        holder.adapterOrderAddonItemsbinding.itemsName.text = "■ ${itemAddOnList[holder.adapterPosition].name}"

        if (!itemAddOnList[holder.adapterPosition].addOnOptions.isNullOrEmpty()) {
            holder.adapterOrderAddonItemsbinding.addonOptionitems.layoutManager = LinearLayoutManager(context)
            holder.adapterOrderAddonItemsbinding.addonOptionitems.adapter = CartSelectedAddonOptionAdapter(context,
                itemAddOnList[holder.adapterPosition].addOnOptions)
        }


    }
    inner class ViewHolder(val adapterOrderAddonItemsbinding : AdapterOrderAddonItemsBinding) : RecyclerView.ViewHolder(adapterOrderAddonItemsbinding.root)
}