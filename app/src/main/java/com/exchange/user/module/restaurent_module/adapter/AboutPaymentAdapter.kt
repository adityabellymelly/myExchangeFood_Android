package com.exchange.user.module.restaurent_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAboutPaymentItemsBinding
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.PaymentType
import com.exchange.user.module.utility_module.BuilderManager

class AboutPaymentAdapter (var context: Context, var itemList: ArrayList<PaymentType>):
    RecyclerView.Adapter<AboutPaymentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterAboutPaymentItemsBinding: AdapterAboutPaymentItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_about_payment_items, parent, false
        )
        return ViewHolder(adapterAboutPaymentItemsBinding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemList[holder.adapterPosition].icon?.let {
            if (it.startsWith("~/")){
                BuilderManager.LoadImage(holder.adapterpaymentItemsBinding.paymentImage, it.substringAfter("~/"))
            }else{
                BuilderManager.LoadImage(holder.adapterpaymentItemsBinding.paymentImage,it)
            }
        }
    }

    fun addItem(it: ArrayList<PaymentType>) {
        itemList = it
        notifyDataSetChanged()
    }


    inner class ViewHolder(val adapterpaymentItemsBinding: AdapterAboutPaymentItemsBinding) :
        RecyclerView.ViewHolder(adapterpaymentItemsBinding.root)
}