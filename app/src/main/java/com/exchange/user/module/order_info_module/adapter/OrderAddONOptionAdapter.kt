package com.exchange.user.module.order_info_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderAddonoptionItemsBinding
import com.exchange.user.module.order_info_module.model.AddOnOption
import com.exchange.user.module.utility_module.BuilderManager
import java.lang.StringBuilder

class OrderAddONOptionAdapter(var context: Context, var addOnOptions: List<AddOnOption>) : RecyclerView.Adapter<OrderAddONOptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):ViewHolder {
        val adapterOrderAddonoptionItemsbinding : AdapterOrderAddonoptionItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_order_addonoption_items, parent, false)
        return ViewHolder(adapterOrderAddonoptionItemsbinding)
    }
    override fun getItemCount(): Int {
        return addOnOptions.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name =  StringBuilder()
        name.append("• ${addOnOptions[holder.adapterPosition].name}")
        addOnOptions[holder.adapterPosition].addOnOptionModifier1?.let {

            if (it.label.isNullOrEmpty().not()){
                name.append(" (${it.label})")
            }

        }
        addOnOptions[holder.adapterPosition].addOnOptionModifier2?.let {
            if (it.label.isNullOrEmpty().not()){
                name.append(" (${it.label})")
            }
        }

        holder.adapterOrderAddonoptionItemsbinding.itemsName.text = name.toString()

        if (addOnOptions.get(holder.adapterPosition).price.toString().toDouble() > 0 ){
            holder.adapterOrderAddonoptionItemsbinding.actualprice.text = BuilderManager.getFormat()
                .format(addOnOptions[holder.adapterPosition].price.toString().toDouble())
        }

    }

    inner class ViewHolder(val adapterOrderAddonoptionItemsbinding : AdapterOrderAddonoptionItemsBinding) : RecyclerView.ViewHolder(adapterOrderAddonoptionItemsbinding.root)

}