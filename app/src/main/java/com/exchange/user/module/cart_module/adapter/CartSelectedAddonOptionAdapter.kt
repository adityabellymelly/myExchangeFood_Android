package com.exchange.user.module.cart_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderAddonoptionItemsBinding
import com.exchange.user.module.cart_module.model.card_model.AddOnOption
import com.exchange.user.module.utility_module.BuilderManager

class CartSelectedAddonOptionAdapter(var context: Context, var addOnOptions: List<AddOnOption>) : RecyclerView.Adapter<CartSelectedAddonOptionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterOrderAddonoptionItemsbinding : AdapterOrderAddonoptionItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_order_addonoption_items, parent, false)
        return ViewHolder(adapterOrderAddonoptionItemsbinding)
    }
    override fun getItemCount(): Int {
        return addOnOptions.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.adapterOrderAddonoptionItemsbinding.itemsName.text = "•${addOnOptions[holder.adapterPosition].name}"
        if (addOnOptions[holder.adapterPosition].amt >0){
            if (addOnOptions[holder.adapterPosition].amt.toString().toDouble() > 0 ){
                holder.adapterOrderAddonoptionItemsbinding.actualprice.text = BuilderManager.getFormat().format(
                    addOnOptions[holder.adapterPosition].amt.toString().toDouble())
            }

        }
    }

    inner class ViewHolder(val adapterOrderAddonoptionItemsbinding : AdapterOrderAddonoptionItemsBinding) : RecyclerView.ViewHolder(adapterOrderAddonoptionItemsbinding.root)

}