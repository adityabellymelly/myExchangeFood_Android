package com.exchange.user.module.order_info_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterMessageItemsBinding
import com.exchange.user.module.order_info_module.model.Message
import com.exchange.user.module.utility_module.BuilderManager
import java.util.*

class QuickMessageAdapter (var context: Context, var itemList: List<Message>):
    RecyclerView.Adapter<QuickMessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): QuickMessageAdapter.ViewHolder {
        val adapterMessageItemsBinding : AdapterMessageItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_message_items, parent, false)
        return ViewHolder(adapterMessageItemsBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: QuickMessageAdapter.ViewHolder, position: Int) {
        holder.adapterMessageItemsBinding.time.text =
            BuilderManager.getProperTime(
                BuilderManager.getDateFromStringSecond(
                    itemList[holder.adapterPosition].createdOn
                ) ?: Date()

            )

        holder.adapterMessageItemsBinding.message.text = itemList[holder.adapterPosition].msgText
    }
    inner class ViewHolder(val adapterMessageItemsBinding : AdapterMessageItemsBinding) : RecyclerView.ViewHolder(adapterMessageItemsBinding.root)

}