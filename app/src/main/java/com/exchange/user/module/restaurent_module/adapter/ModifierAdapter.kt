package com.exchange.user.module.restaurent_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterModifierListRadioBinding
import com.exchange.user.module.cart_module.model.action.ModifierCallback
import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier

class ModifierAdapter(
    var context: Context,
    var modifierlist: ArrayList<AddOnOptionModifier>,
    var modifercallback : ModifierCallback
) : RecyclerView.Adapter<ModifierAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):ViewHolder {
        val adapteraddOnListradioBinding : AdapterModifierListRadioBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_modifier_list_radio, parent, false)
        return ViewHolder(adapteraddOnListradioBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapteraddOnListradioBinding.selectedradio.text = modifierlist[holder.adapterPosition].label
        holder.adapteraddOnListradioBinding.selectedradio.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked){
                    unselectedAll(modifierlist[holder.adapterPosition].label?:"0")
                    modifercallback.onAddModufier(modifierlist[holder.adapterPosition])
                }
                modifierlist[holder.adapterPosition].isSelected= isChecked
            }
        }
        holder.adapteraddOnListradioBinding.selectedradio.isChecked = modifierlist[holder.adapterPosition].isSelected
    }

    private fun unselectedAll(text : String) {
        for (i in modifierlist.indices){
            if (modifierlist[i].label.equals(text,true).not()){
                modifierlist[i].isSelected = false
                try {
                    notifyItemChanged(i)
                }catch (e:Exception){e.printStackTrace()}
            }
        }
    }

    override fun getItemCount(): Int  = modifierlist.size
    fun getSelectedItem(): AddOnOptionModifier? {
        return modifierlist.find { it.isSelected }
    }

    inner class ViewHolder(val adapteraddOnListradioBinding : AdapterModifierListRadioBinding) : RecyclerView.ViewHolder(adapteraddOnListradioBinding.root)

}