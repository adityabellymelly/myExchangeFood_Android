package com.exchange.user.module.order_info_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderItemsBinding
import com.exchange.user.module.order_info_module.model.ItemList
import com.exchange.user.module.utility_module.BuilderManager

class OrderInfoItemsAdapter(var context: Context, var itemList: List<ItemList>):
    RecyclerView.Adapter<OrderInfoItemsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterOrderItemsbinding : AdapterOrderItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_order_items, parent, false)
        return ViewHolder(adapterOrderItemsbinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterOrderItemsbinding.itemsName.text = itemList.get(holder.adapterPosition).name
        holder.adapterOrderItemsbinding.qty.text = "X${itemList[holder.adapterPosition].qty}"

        if(itemList.get(holder.adapterPosition).portionName.isEmpty().not()){
            holder.adapterOrderItemsbinding.positionName.text = itemList[holder.adapterPosition].portionName

        }else{
            holder.adapterOrderItemsbinding.positionName.visibility=View.GONE
        }


        val amount  = calculateAmount(itemList[holder.adapterPosition])
        holder.adapterOrderItemsbinding.actualprice.text = BuilderManager.getFormat()
            .format((amount))
        holder.adapterOrderItemsbinding.aditionalPrice.text = BuilderManager.getFormat()
            .format((amount * (itemList[holder.adapterPosition].qty?:"1").toInt()))

        Handler(Looper.getMainLooper()).postDelayed({
            if (!itemList.get(holder.adapterPosition).itemAddOnList.isNullOrEmpty()) {
                holder.adapterOrderItemsbinding.addon.visibility = View.VISIBLE
                holder.adapterOrderItemsbinding.addonitems.adapter = OrderAddonItemsAdapter(context, itemList.get(holder.adapterPosition).itemAddOnList!!)
            }else{
                holder.adapterOrderItemsbinding.addon.visibility = View.GONE
            }
        },300)

    }

    private fun calculateAmount(itemList: ItemList): Double {
        var amt = itemList.price ?:0.0
        itemList.itemAddOnList?.forEach { orderItemsAddOnList ->
        orderItemsAddOnList.addOnOptions?.forEach { addOnOption ->
            amt += (addOnOption.price ?: 0.0)
        }
        }
        return amt
    }
    fun addItems(itemList: List<ItemList>) {
        this.itemList =  itemList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterOrderItemsbinding : AdapterOrderItemsBinding) : RecyclerView.ViewHolder(adapterOrderItemsbinding.root)

}