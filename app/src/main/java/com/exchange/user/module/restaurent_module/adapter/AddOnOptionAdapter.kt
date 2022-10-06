package com.exchange.user.module.restaurent_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAddOnListBinding
import com.exchange.user.module.cart_module.model.action.ListCallback
import com.exchange.user.module.cart_module.model.action.ModifierCallback
import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnOption
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.utility_module.BuilderManager


class AddOnOptionAdapter(
    var context: Context,
    val addOnOptions: List<AddOnOption>,
    val call: ListCallback,
    val catlist: CatList,
    val quantityCount: TextView,
    val positionotion: String,
    val modifierlist: ArrayList<AddOnOptionModifier>,
    val modifierlist2: ArrayList<AddOnOptionModifier>
) : RecyclerView.Adapter<AddOnOptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterAddOnListBinding : AdapterAddOnListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_add_on_list, parent, false)
        return ViewHolder(adapterAddOnListBinding)
    }

    override fun getItemCount(): Int {
        return addOnOptions.size
    }

    override fun onBindViewHolder(viewholder : ViewHolder, position : Int) {
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
        if (price > 0) {
            viewholder.adapterAddOnListBinding.selectedradio.text =
                addOnOptions.get(viewholder.adapterPosition).name.plus(
                    " " + BuilderManager.getFormat().format(price)
                )
        } else {
            price= 0.0
            viewholder.adapterAddOnListBinding.selectedradio.text =
                addOnOptions.get(viewholder.adapterPosition).name
        }
        viewholder.adapterAddOnListBinding.selectedlay.visibility = View.GONE
        viewholder.adapterAddOnListBinding.selectedlay2.visibility = View.GONE
        viewholder.adapterAddOnListBinding.selectedradio.setOnCheckedChangeListener { _, isChecked ->
            val  list = com.exchange.user.module.cart_module.model.card_model.AddOnOption()
            list.id = addOnOptions.get(viewholder.adapterPosition).id
            list.portionId = positionid
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
                if (modifierlist.isNullOrEmpty().not()){
                    modifierlist[0].isSelected = true
                    viewholder.adapterAddOnListBinding.selectedlay.visibility = View.VISIBLE
                    viewholder.adapterAddOnListBinding.addonmodifier.adapter = ModifierAdapter(context,modifierlist, object :
                        ModifierCallback {
                        override fun onAddModufier(addOnOptionModifier: AddOnOptionModifier) {
                            list.addOnOptionModifier1 = addOnOptionModifier
                            modifierprice = price.times(addOnOptionModifier.factor?:0.0)
                            if (modifierAdapter2!=null){
                                val modifire2 =  modifierAdapter2?.getSelectedItem()
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
                    viewholder.adapterAddOnListBinding.selectedlay2.visibility = View.VISIBLE
                    modifierAdapter2 = ModifierAdapter(context,modifierlist2, object : ModifierCallback {
                        override fun onAddModufier(addOnOptionModifier: AddOnOptionModifier) {
                            list.addOnOptionModifier2 = addOnOptionModifier
                            list.amt =  modifierprice.times(addOnOptionModifier.factor?:0.0)
                            call.OnCheckAdd(list)
                        }
                    })
                    viewholder.adapterAddOnListBinding.addonmodifier2.adapter = modifierAdapter2

                }else{
                    call.OnCheckAdd(list)
                }

            }
            else{
                viewholder.adapterAddOnListBinding.selectedlay.visibility = View.GONE
                viewholder.adapterAddOnListBinding.selectedlay2.visibility = View.GONE
                if (modifierlist.isNullOrEmpty().not()){
                    call.OnCheckRemove(list)
                }else {
                    call.OnCheckRemove(list)
                }
            }
        }


        if (addOnOptions.get(viewholder.adapterPosition).isSelected){
            viewholder.adapterAddOnListBinding.selectedradio.isChecked=true
        }
    }

    inner class ViewHolder(val adapterAddOnListBinding : AdapterAddOnListBinding) : RecyclerView.ViewHolder(adapterAddOnListBinding.root)
}