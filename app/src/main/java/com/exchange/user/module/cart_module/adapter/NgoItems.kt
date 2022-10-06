package com.bellymelly.user.module.cart_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exchange.user.R
import com.exchange.user.databinding.AdapterNgoItemsBinding
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Ngo

class NgoItems(var context: Context, private var ngoList: ArrayList<Ngo>, var listener : OnNgoSelectClickListener) : RecyclerView.Adapter<NgoItems.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.adapterNgoItemsBinding.nameofitems.text = ngoList[position].name
      holder.adapterNgoItemsBinding.codeTV.text = ngoList[position].donateCode
        Glide.with(context)
            .load(ngoList[holder.adapterPosition].photo)
            .into(holder.adapterNgoItemsBinding.itemImage)
        if (ngoList[position].isSelected){
            holder.adapterNgoItemsBinding.codeTV.setBackgroundResource(R.drawable.dot_shape_green)
            holder.adapterNgoItemsBinding.codeTV.setTextColor(ContextCompat.getColor(context, R.color.color_green))
        }else{
            holder.adapterNgoItemsBinding.codeTV.setBackgroundResource(R.drawable.dot_shape_yellow)
            holder.adapterNgoItemsBinding.codeTV.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
        }
        holder.itemView.setOnClickListener {
            val isSelected = !ngoList[holder.adapterPosition].isSelected
            ngoList.forEach { t -> t.isSelected = false }
            ngoList[holder.adapterPosition].isSelected = isSelected
            listener.onNgoSelect(ngoList[holder.adapterPosition])
            notifyDataSetChanged()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterNgoItemsBinding : AdapterNgoItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_ngo_items, parent, false)
        return  ViewHolder(adapterNgoItemsBinding)
    }



    override fun getItemCount(): Int {
        return ngoList.size
    }

    fun getSelectedNgo(): Ngo?{
        return ngoList.find { t -> t.isSelected }
    }

    inner class ViewHolder(val adapterNgoItemsBinding: AdapterNgoItemsBinding) : RecyclerView.ViewHolder(adapterNgoItemsBinding.root)

    interface OnNgoSelectClickListener{
        fun onNgoSelect(ngo: Ngo)
    }
}