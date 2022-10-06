package com.exchange.user.module.change_address_module.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAddressItemsBinding
import com.exchange.user.module.profile_module.model.AddressBook
import java.util.*

class AddressListAdapter(var context: Context, private var addressBook: List<AddressBook>) :
    RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    private lateinit var addressAction : AddressAction

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterAddressItemsBinding : AdapterAddressItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_address_items, parent, false)
        return ViewHolder(adapterAddressItemsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(addressBook[holder.adapterPosition].custAddressName!!.toLowerCase(Locale.US)){
            "home" ->{
                holder.adapterAddressItemsBinding.addresstype.text = context.getString(R.string.home)
                holder.adapterAddressItemsBinding.addresstype.setTextColor(Color.parseColor("#EF9C21"))
                holder.adapterAddressItemsBinding.typeimage.setBackgroundResource(R.drawable.ic_home_address)
                holder.adapterAddressItemsBinding.username.text = addressBook[holder.adapterPosition].fName
                holder.run { adapterAddressItemsBinding.username.setTextColor(Color.parseColor("#EF9C21")) }
                holder.adapterAddressItemsBinding.mobileno.text = addressBook[holder.adapterPosition].telephone
                holder.adapterAddressItemsBinding.mobileno.setTextColor(Color.parseColor("#EF9C21"))
            }
            "office" ->{
                holder.adapterAddressItemsBinding.addresstype.text =context.getString(R.string.office)
                holder.adapterAddressItemsBinding.addresstype.setTextColor(Color.parseColor("#15AD15"))
                holder.adapterAddressItemsBinding.typeimage.setBackgroundResource(R.drawable.ic_offic_address)
                holder.adapterAddressItemsBinding.username.text = addressBook[holder.adapterPosition].fName
                holder.run { adapterAddressItemsBinding.username.setTextColor(Color.parseColor("#15AD15")) }
                holder.adapterAddressItemsBinding.mobileno.text = addressBook[holder.adapterPosition].telephone
                holder.adapterAddressItemsBinding.mobileno.setTextColor(Color.parseColor("#15AD15"))

            }
            "other" ->{
                holder.adapterAddressItemsBinding.addresstype.text =context.getString(R.string.other)
                holder.adapterAddressItemsBinding.addresstype.setTextColor(Color.parseColor("#4A40BA"))
                holder.adapterAddressItemsBinding.typeimage.setBackgroundResource(R.drawable.ic_other_address)
                holder.adapterAddressItemsBinding.username.text = addressBook[holder.adapterPosition].fName
                holder.run { adapterAddressItemsBinding.username.setTextColor(Color.parseColor("#4A40BA")) }
                holder.adapterAddressItemsBinding.mobileno.text = addressBook[holder.adapterPosition].telephone
                holder.adapterAddressItemsBinding.mobileno.setTextColor(Color.parseColor("#4A40BA"))
            }
        }
        holder.adapterAddressItemsBinding.spicialinstruction.text = addressBook[holder.adapterPosition].instructions
        val address = StringBuilder()
        if (!addressBook[holder.adapterPosition].addr1.isNullOrEmpty()){
            address.append("${addressBook[holder.adapterPosition].addr1},")
        }
        if (!addressBook[holder.adapterPosition].addr2.isNullOrEmpty()){
            address.append("${addressBook[holder.adapterPosition].addr2},")
        }
        holder.adapterAddressItemsBinding.completeaddress.text = address.toString()
        holder.adapterAddressItemsBinding.editaddess.setOnClickListener { addressAction.onEditAddress(
            addressBook[holder.adapterPosition],holder.adapterPosition) }
        holder.itemView.setOnClickListener { addressAction.onSelectAddress(addressBook[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int=addressBook.size

    inner class ViewHolder(val adapterAddressItemsBinding : AdapterAddressItemsBinding) : RecyclerView.ViewHolder(adapterAddressItemsBinding.root)

    fun setListner(addressAction :AddressAction){
       this.addressAction = addressAction
    }

    fun addItems(addressBook: ArrayList<AddressBook>) {
        this.addressBook=addressBook
        notifyDataSetChanged()
    }

    interface AddressAction{
         fun onSelectAddress(get: AddressBook)
         fun onEditAddress(get: AddressBook, adapterPosition: Int)
     }
}