package com.exchange.user.module.restaurent_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAboutServiceItemsBinding
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Service
import com.exchange.user.module.utility_module.BuilderManager

class AboutServiceAdapter(var context: Context, var itemList: ArrayList<Service>):
    RecyclerView.Adapter<AboutServiceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapteraboutserviceitemsbinding: AdapterAboutServiceItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_about_service_items, parent, false
        )
        return ViewHolder(adapteraboutserviceitemsbinding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterpaymentItemsBinding.minCharge.text =
            "${BuilderManager.getFormat().format(itemList[holder.adapterPosition].minCharges)}"

        holder.adapterpaymentItemsBinding.minorder.text =
            "${BuilderManager.getFormat().format(itemList[holder.adapterPosition].minOrder)}"

        holder.adapterpaymentItemsBinding.servicename.text = itemList[holder.adapterPosition].name

        if ((itemList[holder.adapterPosition].readyInMin?:"0").contains("min")) {
            holder.adapterpaymentItemsBinding.readyin.text =
                itemList[holder.adapterPosition].readyInMin
        }else{
            holder.adapterpaymentItemsBinding.readyin.text ="${itemList[holder.adapterPosition].readyInMin} min"

        }
    }

    fun addItem(it: ArrayList<Service>) {
        itemList = it
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterpaymentItemsBinding: AdapterAboutServiceItemsBinding) :
        RecyclerView.ViewHolder(adapterpaymentItemsBinding.root)
}