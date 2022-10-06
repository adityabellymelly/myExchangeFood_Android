package com.exchange.user.module.country_module.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.databinding.AdapterCountryItemsBinding
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.utility_module.BuilderManager

class CountyAdapter (val context: Context, private var arrayList:ArrayList<Country>) : RecyclerView.Adapter<CountyAdapter.ViewHolder>(){
    private lateinit var countryChooice : CountryChooice
    override fun getItemCount(): Int = arrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterCountyItemsBinding : AdapterCountryItemsBinding = AdapterCountryItemsBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(adapterCountyItemsBinding)
    }
    fun addItems(it: ArrayList<Country>) {
        this.arrayList = it
        notifyDataSetChanged()
    }
    private fun setselected(country: String?) {
        for (i in 0 until arrayList.size){
            arrayList[i].isSelected = country.equals(arrayList[i].name)
            notifyItemChanged(i)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterCountyItemsBinding.name.text = arrayList[holder.adapterPosition].name
        BuilderManager.loadCountryImage(holder.adapterCountyItemsBinding.image,
            arrayList[holder.adapterPosition].image)
        if (arrayList[holder.adapterPosition].isSelected){
            holder.adapterCountyItemsBinding.card.setCardBackgroundColor(Color.parseColor("#A71931"))
            holder.adapterCountyItemsBinding.selectedic.visibility =View.VISIBLE

        }else {
            holder.adapterCountyItemsBinding.card.setCardBackgroundColor(Color.parseColor("#000000"))
            holder.adapterCountyItemsBinding.selectedic.visibility =View.GONE
        }
        holder.itemView.setOnClickListener {
            arrayList[holder.adapterPosition].isSelected = !arrayList[holder.adapterPosition].isSelected
            setselected(arrayList[holder.adapterPosition].name)
            countryChooice.onCountryChooes(arrayList[holder.adapterPosition],arrayList)
        }
    }

    inner class ViewHolder(val adapterCountyItemsBinding : AdapterCountryItemsBinding) : RecyclerView.ViewHolder(adapterCountyItemsBinding.root)

    fun setListner(countryChooice : CountryChooice) {
        this.countryChooice = countryChooice
    }

    interface CountryChooice{
        fun onCountryChooes(
            get: Country,
            countrydata: ArrayList<Country>
        )

    }

}