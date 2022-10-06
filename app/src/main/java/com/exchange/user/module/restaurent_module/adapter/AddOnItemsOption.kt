package com.bellymelly.user.module.restaurant_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAddOnItemsBinding
import com.exchange.user.module.cart_module.model.action.ListCallback
import com.exchange.user.module.cart_module.model.action.OnAddOnOption
import com.exchange.user.module.cart_module.model.card_model.AddOnOption
import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier
import com.exchange.user.module.cart_module.model.card_model.ItemsAddonList
import com.exchange.user.module.restaurent_module.adapter.AddOnOptionAdapter
import com.exchange.user.module.restaurent_module.adapter.AddOnOptionAdapterRadio
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnList
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.utility_module.BuilderManager

class AddOnItemsOption(
    var context: Context,
    val addOnOptions: List<AddOnList>,
    val onaddoptioncomm: OnAddOnOption,
    val catlist: CatList,
    val quantityCount: TextView,
    var positionotion: String) : RecyclerView.Adapter<AddOnItemsOption.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterAddonItemsBinding : AdapterAddOnItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_add_on_items, parent, false)
        return ViewHolder(adapterAddonItemsBinding)
    }
    override fun getItemCount(): Int {
        return addOnOptions.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewholder : ViewHolder, position : Int) {
        viewholder.adapterAddonItemsBinding.titlename.text = addOnOptions.get(viewholder.adapterPosition).name
        viewholder.adapterAddonItemsBinding.dicrip.text = addOnOptions.get(viewholder.adapterPosition).desc
        if (addOnOptions.get(viewholder.adapterPosition).reqd.equals("T")) {
            viewholder.adapterAddonItemsBinding.requirechoose.text = "Required - Choose 1"
        }
        val addonlist = ItemsAddonList()
        addonlist.itemAddOnId = addOnOptions.get(viewholder.adapterPosition).itemAddOnId
        addonlist.name = addOnOptions.get(viewholder.adapterPosition).name
        val call  = object  : ListCallback {
            override fun OnCheckAdd(list: AddOnOption) {
                if (addOnOptions.get(viewholder.adapterPosition).dispType.equals("S")){
                    if (addonlist.addOnOptions.isNotEmpty()){
                        addonlist.addOnOptions.clear()
                        addonlist.addOnOptions.add(list)
                        onaddoptioncomm.onAddOnOption(addonlist)
                    }else{
                        addonlist.addOnOptions.add(list)
                        onaddoptioncomm.onAddOnOption(addonlist)
                    }
                }else{
                    val items =  addonlist.addOnOptions.find { it.id ==list.id }
                    if (items!=null){
                        addonlist.addOnOptions.remove(items)
                        addonlist.addOnOptions.add(list)
                    }else{
                        addonlist.addOnOptions.add(list)
                    }
                    onaddoptioncomm.onAddOnOption(addonlist)
                }
            }

            override fun OnCheckRemove(list: AddOnOption) {
                val x = addonlist.addOnOptions.indexOfFirst { it.id == list.id}
                if (x==-1)
                {

                }else{
                    addonlist.addOnOptions.removeAt(x)
                    if(!addonlist.addOnOptions.isEmpty()){
                        onaddoptioncomm.onAddOnOption(addonlist)
                    }else{
                        onaddoptioncomm.onAddRemoveOption(addonlist)
                    }
                }
            }

        }
        var modifierlist = ArrayList<AddOnOptionModifier>()
        var modifierlist_2 = ArrayList<AddOnOptionModifier>()
        addOnOptions.get(viewholder.adapterPosition).addOnOptionModifier1?.let{
            modifierlist =  BuilderManager.getModifierList(it)
        }
        addOnOptions.get(viewholder.adapterPosition).addOnOptionModifier2?.let{
            modifierlist_2 =  BuilderManager.getModifierList(it)
        }
        if(addOnOptions.get(viewholder.adapterPosition).dispType.equals("S")){
            addonlist.itemAddOnId = addOnOptions.get(viewholder.adapterPosition).itemAddOnId
            viewholder.adapterAddonItemsBinding.itemsaddrecycle.layoutManager = GridLayoutManager(context, 2)
            addOnOptions.get(viewholder.adapterPosition).addOnOptions?.let {
                if (it.isNotEmpty()) {
                    if (addOnOptions.get(viewholder.adapterPosition).reqd.equals("T")){
                        it[0].isSelected = true
                    }
                }
            }

            val addonadapteradio = AddOnOptionAdapterRadio(context,addOnOptions.get(viewholder.adapterPosition).addOnOptions!!,call,catlist,quantityCount,
                positionotion,modifierlist,modifierlist_2)
            viewholder.adapterAddonItemsBinding.itemsaddrecycle.adapter = addonadapteradio
        }else{
            addonlist.itemAddOnId = addOnOptions.get(viewholder.adapterPosition).itemAddOnId
            viewholder.adapterAddonItemsBinding.itemsaddrecycle.layoutManager = GridLayoutManager(context, 2)
            addOnOptions.get(viewholder.adapterPosition).addOnOptions?.let {
                if (it.isNotEmpty()) {
                    if (addOnOptions.get(viewholder.adapterPosition).reqd.equals("T")){
                        it[0].isSelected = true
                    }
                }
            }
            val addonadapter =  AddOnOptionAdapter(context,addOnOptions.get(viewholder.adapterPosition).addOnOptions!!,call,catlist,quantityCount,
                positionotion,modifierlist,modifierlist_2)
            viewholder.adapterAddonItemsBinding.itemsaddrecycle.adapter = addonadapter
        }
    }


    fun updateAdapter(positionotion: String) {
        this.positionotion = positionotion
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterAddonItemsBinding : AdapterAddOnItemsBinding) : RecyclerView.ViewHolder(adapterAddonItemsBinding.root)

}