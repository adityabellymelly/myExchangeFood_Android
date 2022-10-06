package com.exchange.user.module.home_module.home_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.databinding.AdapterRestaruentBinding
import com.exchange.user.module.country_module.model.location_model.LocList

class RestaruentAdapter (val context: Context, private var arrayList:List<LocList>) : RecyclerView.Adapter<RestaruentAdapter.ViewHolder>(){

    private lateinit var reasturentListner :  ReasturentListner


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterCountyItemsBinding : AdapterRestaruentBinding = AdapterRestaruentBinding.inflate(
            LayoutInflater.from(context),parent,false)
        return ViewHolder(adapterCountyItemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (arrayList[holder.adapterPosition].street.isNullOrEmpty().not()){
            holder.adapterCountyItemsBinding.streetname.visibility = View.VISIBLE
            holder.adapterCountyItemsBinding.streetname.text =arrayList[holder.adapterPosition].street
        }else{
            holder.adapterCountyItemsBinding.streetname.visibility = View.GONE
        }

        holder.adapterCountyItemsBinding.locationname.text = arrayList[holder.adapterPosition].LocName
        holder.itemView.setOnClickListener {
            reasturentListner.onSelectRestaruent(arrayList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = arrayList.size
    inner class ViewHolder(val adapterCountyItemsBinding : AdapterRestaruentBinding) : RecyclerView.ViewHolder(adapterCountyItemsBinding.root)

    fun setReasturentListner(reasturentListner : ReasturentListner){
       this.reasturentListner = reasturentListner
    }

    fun addItems(locList: List<LocList>) {
       this.arrayList = locList
        notifyDataSetChanged()
    }

    interface ReasturentListner{
        fun onSelectRestaruent(region: LocList)
    }

}