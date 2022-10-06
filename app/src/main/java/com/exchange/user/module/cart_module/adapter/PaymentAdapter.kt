package com.exchange.user.module.cart_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterPaymentItemsBinding
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.PaymentType

class PaymentAdapter(var context: Context, var itemList: ArrayList<PaymentType>) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    private lateinit var paymentInteface: PaymentInteface
    val isCoupon: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterpaymentItemsBinding: AdapterPaymentItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_payment_items, parent, false
        )
        return ViewHolder(adapterpaymentItemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterpaymentItemsBinding.paymentname.text =
            when {
                itemList[holder.adapterPosition].type.equals("CC", true) -> context.getString(R.string.debit_credit_card)
                itemList[holder.adapterPosition].type.equals("STAR", true) -> context.getString(R.string.miltry_star_card)
                else -> itemList[holder.adapterPosition].desc
            }

        holder.adapterpaymentItemsBinding.paymentname.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    unselectedAll(itemList[holder.adapterPosition].id ?: 0L)
                    paymentInteface.onSelectPayment(itemList[holder.adapterPosition])
                }
                itemList[holder.adapterPosition].isSelected = isChecked
            }
        }

        if (itemList[holder.adapterPosition].isSelected) {
                unselectedAll(itemList[holder.adapterPosition].id ?: 0L)
                paymentInteface.onSelectPayment(itemList[holder.adapterPosition])
        }

        holder.adapterpaymentItemsBinding.paymentname.isChecked =
            itemList[holder.adapterPosition].isSelected
    }
    private fun unselectedAll(id: Long) {
        for (i in itemList.indices) {
            if (itemList[i].id != id && itemList[i].isSelected) {
                itemList[i].isSelected = false
                try {
                    notifyItemChanged(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun setListner(paymentInteface: PaymentInteface) {
        this.paymentInteface = paymentInteface
    }

    interface PaymentInteface {
        fun onSelectPayment(payment: PaymentType)
        fun onUnSelectedPayment(payment: PaymentType?)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun addItems(payment: ArrayList<PaymentType>) {
        this.itemList = payment
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterpaymentItemsBinding: AdapterPaymentItemsBinding) :
        RecyclerView.ViewHolder(adapterpaymentItemsBinding.root)

}