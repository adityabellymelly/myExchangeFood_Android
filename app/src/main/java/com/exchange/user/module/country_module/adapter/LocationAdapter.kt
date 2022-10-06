package com.exchange.user.module.country_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.databinding.AdapterLocationBinding
import com.exchange.user.module.country_module.model.location_model.RegionList

class LocationAdapter(val context: Context, private var arrayList:ArrayList<RegionList>, private var locationlitner: LocationListner) : RecyclerView.Adapter<LocationAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterCountyItemsBinding : AdapterLocationBinding = AdapterLocationBinding.inflate(
            LayoutInflater.from(context),parent,false)
        return ViewHolder(adapterCountyItemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterCountyItemsBinding.locationname.text = arrayList[holder.adapterPosition].RegionName
        holder.itemView.setOnClickListener {
            locationlitner.onSelectLocation(arrayList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = arrayList.size
    inner class ViewHolder(val adapterCountyItemsBinding : AdapterLocationBinding) : RecyclerView.ViewHolder(adapterCountyItemsBinding.root)

    interface LocationListner{
        fun onSelectLocation(region: RegionList)
    }

}