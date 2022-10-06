package com.exchange.user.module.cart_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterServiceItemsBinding
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Service

class ServiceAdapter(var context: Context, var itemList: ArrayList<Service>) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>(){
    private lateinit var serviceInteface :ServiceInteface
    private var isCoupon:Boolean= false
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterserviceItemsBinding :AdapterServiceItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_service_items, parent, false)
        return ViewHolder(adapterserviceItemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterserviceItemsBinding.service.text = itemList[holder.adapterPosition].name
        holder.adapterserviceItemsBinding.service.setOnCheckedChangeListener { _, isChecked ->
            run {
                  if (isChecked){
                      unselectedAll(itemList[holder.adapterPosition].id?:0L)
                      serviceInteface.onSelectService(itemList[holder.adapterPosition],isCoupon)
                  }
                itemList[holder.adapterPosition].isSelected = isChecked
            }
        }

        if (itemList[holder.adapterPosition].isSelected){
            unselectedAll(itemList[holder.adapterPosition].id?:0L)
            serviceInteface.onSelectService(itemList[holder.adapterPosition],isCoupon)
        }

        holder.adapterserviceItemsBinding.service.isChecked = itemList[holder.adapterPosition].isSelected
    }


    private fun unselectedAll(id: Long) {
        for (i in itemList.indices){
            if (itemList[i].id!=id && itemList[i].isSelected){
                itemList[i].isSelected = false
                try {
                    notifyItemChanged(i)
                }catch (e:Exception){e.printStackTrace()}
            }
        }
    }
    fun setListner(serviceInteface :ServiceInteface){
        this.serviceInteface=serviceInteface
    }
    interface ServiceInteface{
        fun onSelectService(service: Service,isCoupon : Boolean)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItems(service: ArrayList<Service>) {
         this.itemList = service
         notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterserviceItemsBinding : AdapterServiceItemsBinding) : RecyclerView.ViewHolder(adapterserviceItemsBinding.root)

}