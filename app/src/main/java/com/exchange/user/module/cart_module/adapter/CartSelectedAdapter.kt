package com.exchange.user.module.cart_module.model.action.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterSelectedItemsBinding
import com.exchange.user.module.cart_module.model.action.CartUpdate
import com.exchange.user.module.cart_module.model.card_model.ItemList
import com.exchange.user.module.utility_module.BuilderManager

class CartSelectedAdapter(var context: Context, var itemList: ArrayList<ItemList>) : RecyclerView.Adapter<CartSelectedAdapter.ViewHolder>(){

    private lateinit var cartupdate:CartUpdate
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterSelectedItemsBinding : AdapterSelectedItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_selected_items, parent, false)
        return ViewHolder(adapterSelectedItemsBinding)
    }
    override fun getItemCount(): Int {
       return itemList.size
    }

    fun setcartupdate(cartupdate :CartUpdate, itemList: ArrayList<ItemList> ){
        this.cartupdate = cartupdate
        this.itemList =itemList
    }

    override fun onBindViewHolder(viewholder :ViewHolder, position: Int) {
        try {
            viewholder.adapterSelectedItemsBinding.actualprice.text = BuilderManager.getFormat().format(itemList[viewholder.adapterPosition].unitPrice)
        } catch (e: Exception) {
        }
        if (itemList[viewholder.adapterPosition].specialInstructions.isNullOrEmpty().not()) {
            viewholder.adapterSelectedItemsBinding.spicilainstuctionlay.visibility = View.VISIBLE
            viewholder.adapterSelectedItemsBinding.spicialinstrusction.text = itemList[viewholder.adapterPosition].specialInstructions
        } else{
            viewholder.adapterSelectedItemsBinding.spicilainstuctionlay.visibility = View.GONE
        }
        viewholder.adapterSelectedItemsBinding.quantityCount.text = itemList[viewholder.adapterPosition].qty.toString()
        viewholder.adapterSelectedItemsBinding.itemsName.text = itemList[viewholder.adapterPosition].name

        viewholder.adapterSelectedItemsBinding.QuantityIncressed.setOnClickListener {
            try {
                var quality = viewholder.adapterSelectedItemsBinding.quantityCount.text.toString().toLong()
                if (quality < itemList[viewholder.adapterPosition].maxQ) {
                    val dec = ++quality
                    viewholder.adapterSelectedItemsBinding.quantityCount.text = dec.toString()
                    var amount = 0.0
                    val addonlist = itemList[viewholder.adapterPosition].itemAddOnList
                    if (addonlist.isNotEmpty()) {
                        for (a in 0 until addonlist.size) {
                            val addonoption = addonlist[a].addOnOptions
                            for (b in 0 until addonoption.size) {
                                amount += addonoption[b].amt
                            }
                        }
                    }
                    itemList[viewholder.adapterPosition].qty = dec.toLong()
                    itemList[viewholder.adapterPosition].amt = itemList[viewholder.adapterPosition].unitPrice.times(dec) + amount.times(dec)
                    cartupdate.onIncresses()
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        viewholder.adapterSelectedItemsBinding.QualityDecreasee.setOnClickListener {
            try {
                var quality  = viewholder.adapterSelectedItemsBinding.quantityCount.text.toString().toLong()
                if (quality > 0) {
                    val dec = -- quality

                    if (dec == 0L){
                        cartupdate.remove(viewholder.adapterPosition)

                    }else {
                        viewholder.adapterSelectedItemsBinding.quantityCount.text = dec.toString()

                        var amount = 0.0

                        val addonlist = itemList[viewholder.adapterPosition].itemAddOnList
                        if (addonlist.isNotEmpty()) {
                            for (a in 0 until addonlist.size) {

                                val addonoption = addonlist[a].addOnOptions
                                for (b in 0 until addonoption.size) {
                                    amount += addonoption[b].amt
                                }
                            }
                        }
                        itemList[viewholder.adapterPosition].amt =  itemList[viewholder.adapterPosition].unitPrice.times(dec)+ amount.times(dec)
                        itemList[viewholder.adapterPosition].qty = dec.toLong()
                        cartupdate.onDecresses()
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        if (!itemList[viewholder.adapterPosition].itemAddOnList.isNullOrEmpty()) {
            viewholder.adapterSelectedItemsBinding.addon.visibility = View.VISIBLE
            viewholder.adapterSelectedItemsBinding.addonitems.layoutManager = GridLayoutManager(context,2)
            viewholder.adapterSelectedItemsBinding.addonitems.adapter = CartSelectedAddonItemsAdapter(context, itemList[viewholder.adapterPosition].itemAddOnList)
        }
        else{
            viewholder.adapterSelectedItemsBinding.addon.visibility = View.GONE
        }

    }
    inner class ViewHolder(val adapterSelectedItemsBinding : AdapterSelectedItemsBinding) : RecyclerView.ViewHolder(adapterSelectedItemsBinding.root)

}

