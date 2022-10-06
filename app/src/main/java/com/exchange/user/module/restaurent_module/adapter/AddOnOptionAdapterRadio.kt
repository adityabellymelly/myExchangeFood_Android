package com.exchange.user.module.restaurent_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAddOnListRadioBinding
import com.exchange.user.module.cart_module.model.action.ListCallback
import com.exchange.user.module.cart_module.model.action.ModifierCallback
import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnOption
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.utility_module.BuilderManager

class AddOnOptionAdapterRadio(
    var context: Context,
    val addOnOptions: List<AddOnOption>,
    val call: ListCallback,
    val catlist: CatList,
    val quantityCount: TextView,
    val positionotion: String,
    val modifierlist: ArrayList<AddOnOptionModifier>,
    val modifierlist2: ArrayList<AddOnOptionModifier>
)
    : RecyclerView.Adapter<AddOnOptionAdapterRadio.ViewHolder>() {
    private var holderlist : ArrayList<ViewHolder>

    init {

        val size = addOnOptions.size
        val holderlistdemo = ArrayList<ViewHolder>(size)
        holderlist = holderlistdemo
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int):ViewHolder {
        val adapteraddOnListradioBinding : AdapterAddOnListRadioBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_add_on_list_radio, parent, false)
        return ViewHolder(adapteraddOnListradioBinding)
    }

    override fun getItemCount(): Int {
        return addOnOptions.size
    }
    override fun onBindViewHolder(viewholder : ViewHolder, position: Int) {
        var price = addOnOptions.get(viewholder.adapterPosition).p1
        var modifierprice = 1.0
        var modifierAdapter2 : ModifierAdapter? = null
        var positionid  = catlist.p1!!.id
        if (positionotion.equals("p1")) {
            price = addOnOptions.get(viewholder.adapterPosition).p1
            positionid  = catlist.p1!!.id
        } else if (positionotion.equals("p2")) {
            price = addOnOptions.get(viewholder.adapterPosition).p2
            positionid  = catlist.p2!!.id
        } else if (positionotion.equals("p3")) {
            price = addOnOptions.get(viewholder.adapterPosition).p3
            positionid  = catlist.p3!!.id
        } else if (positionotion.equals("p4")) {
            price = addOnOptions.get(viewholder.adapterPosition).p4
            positionid  = catlist.p4!!.id
        } else if (positionotion.equals("p5")) {
            price = addOnOptions.get(viewholder.adapterPosition).p5
            positionid  = catlist.p5!!.id
        }
        if (price > 0.0) {
            viewholder.adapteraddOnListradioBinding.selectedradio.text = addOnOptions.get(viewholder.adapterPosition).name.plus(" " + BuilderManager.getFormat().format(price))
        } else {
            price = 0.0
            viewholder.adapteraddOnListradioBinding.selectedradio.text = addOnOptions.get(viewholder.adapterPosition).name
        }
        viewholder.adapteraddOnListradioBinding.selectedlay.visibility = View.GONE
        viewholder.adapteraddOnListradioBinding.selectedlay2.visibility = View.GONE
        viewholder.adapteraddOnListradioBinding.selectedradio.setOnCheckedChangeListener { _, isChecked ->
            val  list = com.exchange.user.module.cart_module.model.card_model.AddOnOption()
            list.id = addOnOptions.get(viewholder.adapterPosition).id
            list.portionId =  positionid
            list.unitPrice = price
            list.qty = 1
            list.amt = price
            list.dflt = addOnOptions.get(viewholder.adapterPosition).dflt
            list.isSelected = true
            list.displayType = null
            list.name = addOnOptions.get(viewholder.adapterPosition).name
            list.addOnOptionModifier1 = null
            list.addOnOptionModifier2 = null
            if (isChecked){
                RemoveallOther(viewholder.adapterPosition)
                if (modifierlist.isNullOrEmpty().not()){
                    modifierlist[0].isSelected = true
                    viewholder.adapteraddOnListradioBinding.selectedlay.visibility = View.VISIBLE
                    viewholder.adapteraddOnListradioBinding.addonmodifier.adapter = ModifierAdapter(context,modifierlist, object :
                        ModifierCallback {
                        override fun onAddModufier(addOnOptionModifier: AddOnOptionModifier) {
                            list.addOnOptionModifier1 = addOnOptionModifier
                            modifierprice = price.times(addOnOptionModifier.factor?:0.0)
                            if (modifierAdapter2!=null){
                                val modifire2 = modifierAdapter2.getSelectedItem()
                                modifire2?.let {
                                    list.addOnOptionModifier2 =it
                                    list.amt =   modifierprice.times(it.factor?:0.0)
                                }
                            }else{
                                list.amt =  modifierprice

                            }
                            call.OnCheckAdd(list)
                        }
                    })

                }else{
                    call.OnCheckAdd(list)
                }

                if (modifierlist2.isNullOrEmpty().not()){
                    modifierlist2[0].isSelected = true
                    viewholder.adapteraddOnListradioBinding.selectedlay2.visibility = View.VISIBLE
                    viewholder.adapteraddOnListradioBinding.addonmodifier2.adapter = ModifierAdapter(context,modifierlist2, object :
                        ModifierCallback {
                        override fun onAddModufier(addOnOptionModifier: AddOnOptionModifier) {
                            list.addOnOptionModifier2 = addOnOptionModifier
                            list.amt =  modifierprice.times(addOnOptionModifier.factor?:0.0)
                            call.OnCheckAdd(list)
                        }
                    })

                }else{
                    call.OnCheckAdd(list)
                }

            }
            else{
                viewholder.adapteraddOnListradioBinding.selectedlay.visibility = View.GONE
                viewholder.adapteraddOnListradioBinding.selectedlay2.visibility = View.GONE
                if (modifierlist.isNullOrEmpty().not()){
                    call.OnCheckRemove(list)
                }else {
                    call.OnCheckRemove(list)
                }
            }
        }

        if (position>=holderlist.size){
            holderlist.add(position,viewholder)
        }
        else{
            holderlist.set(position,viewholder)
        }


        if (addOnOptions.get(viewholder.adapterPosition).isSelected){
            viewholder.adapteraddOnListradioBinding.selectedradio.isChecked=true
        }

    }

    private fun RemoveallOther(adapterPosition: Int) {
        for (i in 0 until holderlist.size ){
            if (i!= adapterPosition){
                if (holderlist.get(i).adapteraddOnListradioBinding.selectedradio.isChecked){
                    holderlist.get(i).adapteraddOnListradioBinding.selectedradio.isChecked = false
                }
            }
        }
    }


    inner class ViewHolder(val adapteraddOnListradioBinding : AdapterAddOnListRadioBinding) : RecyclerView.ViewHolder(adapteraddOnListradioBinding.root)
}