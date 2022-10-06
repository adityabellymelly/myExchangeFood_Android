package com.exchange.user.module.coupon_module.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterCoupenItemsBinding
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Discount
import com.exchange.user.module.utility_module.BuilderManager
import java.util.*
import kotlin.math.ceil

class DiscountListAdapter(var context: Context, var discount: ArrayList<Discount>): RecyclerView.Adapter<DiscountListAdapter.ViewHolder>(){
    private lateinit var coupenapplied :AppliedCoupen

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adaptercoupenitemsbinding.nameofcoopen.text =  discount[holder.adapterPosition].description
        discount[holder.adapterPosition].service?.let {
            holder.adaptercoupenitemsbinding.spacific.text = "For ${it.serviceName}"

        }
        holder.adaptercoupenitemsbinding.discription.visibility = View.GONE

        if (discount[holder.adapterPosition].minAmt>0.0){
            holder.adaptercoupenitemsbinding.abovemessage.text  = "On orders of ${BuilderManager.getFormat().format(discount[holder.adapterPosition].minAmt)} and above "
        }else{
            holder.adaptercoupenitemsbinding.abovemessage.text  = ""
        }
        if (discount[holder.adapterPosition].type.equals("%")) {
            val discountobj = discount[holder.adapterPosition].disc
            val persent = (100 - ceil((1 - discountobj) * 100).toInt())
            val off = StringBuilder()
            off.append("<strong>${persent}% Off</strong>")

            if (discount[holder.adapterPosition].maxCap>0.0){
                off.append(" up to ${BuilderManager.getFormat().format(discount[holder.adapterPosition].maxCap)}")
            }
            val htmlAsSpanned = HtmlCompat.fromHtml(off.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.adaptercoupenitemsbinding.persentoff.text = htmlAsSpanned
        }
        else if (discount[holder.adapterPosition].type.equals("$")){
            val off = StringBuilder()
            off.append("<strong>${BuilderManager.getFormat().format(discount[holder.adapterPosition].disc)} Off</strong>")

            if (discount[holder.adapterPosition].maxCap>0.0){
                off.append(" up to ${BuilderManager.getFormat().format(discount[holder.adapterPosition].maxCap)}")
            }
            val htmlAsSpanned = HtmlCompat.fromHtml(off.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.adaptercoupenitemsbinding.persentoff.text = htmlAsSpanned
        }
        if(isCondition(discount[holder.adapterPosition])){
            holder.itemView.setOnClickListener {
                if(discount[holder.adapterPosition].preTax.equals("T")){
                    CartDataInt.cartdata.discountAmt = getCoupenPreTaxAmount(CartDataInt.cartdata,discount[holder.adapterPosition])
                    CartDataInt.cartdata.discountType =   discount[holder.adapterPosition].id
                }else{
                    CartDataInt.cartdata.discountAmt = getCoupenPostTaxAmount(CartDataInt.cartdata,discount[holder.adapterPosition])
                    CartDataInt.cartdata.discountType =   discount[holder.adapterPosition].id
                }
                coupenapplied.onAppliedCoupon()
            }


            holder.adaptercoupenitemsbinding.applied.setOnClickListener {
                if(discount[holder.adapterPosition].preTax.equals("T")){
                    CartDataInt.cartdata.discountAmt = getCoupenPreTaxAmount(CartDataInt.cartdata,discount[holder.adapterPosition])
                    CartDataInt.cartdata.discountType =   discount[holder.adapterPosition].id
                }else{
                    CartDataInt.cartdata.discountAmt = getCoupenPostTaxAmount(CartDataInt.cartdata,discount[holder.adapterPosition])
                    CartDataInt.cartdata.discountType =   discount[holder.adapterPosition].id
                }
//                CartDataInt.cartdata.couponAmt = null
//                CartDataInt.cartdata.custCouponId = null
//                CartDataInt.cartdata.restChainCouponId = null
//                CartDataInt.cartdata.couponType = null
                coupenapplied.onAppliedCoupon()
            }

        }else{
            holder.adaptercoupenitemsbinding.spacific.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
            holder.adaptercoupenitemsbinding.nameofcoopen.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
            holder.adaptercoupenitemsbinding.applied.visibility = View.GONE
            val message = getMessage(discount[holder.adapterPosition])
            holder.adaptercoupenitemsbinding.persentoff.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
            holder.itemView.setOnClickListener {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adaptercoupenitemsbinding : AdapterCoupenItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_coupen_items, parent, false)
        return ViewHolder(adaptercoupenitemsbinding)
    }
    override fun getItemCount(): Int {
        return discount.size
    }
    private fun isCondition(coupen: Discount): Boolean {
        var condition = true
        if (coupen.preTax.equals("T")){
            if (coupen.minAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minAmt){
                    condition =  false
                }
            }
        }else{
            if (coupen.minAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minAmt){
                    condition =  false
                }
            }
        }
        coupen.service?.let {
            if (CartDataInt.cartdata.serviceId!= it.serviceId){
                condition = false
            }
        }
        coupen.paymentTypes?.let { it ->
            condition = it.any { it.Id?:0L==CartDataInt.cartdata.paymentTypeId}
        }
        try{
            val startdate = BuilderManager.getDateFromString(coupen.startDate)
            val enddate =  BuilderManager.getDateFromString(coupen.endDate)

            val nowdate = Date()

            if ((nowdate.after(startdate) && nowdate.before(enddate)).not()){
                condition = false
            }
        }catch (e: Exception){}
        return condition
    }

    private fun getMessage(coupen: Discount): String  {
        var condition = ""
        if (coupen.preTax.equals("T")){
            if (coupen.minAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minAmt){
                    condition = "Coupan can applied  up to ${BuilderManager.getFormat().format(coupen.minAmt)} amount."
                }
            }
        }else{
            if (coupen.minAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minAmt){
                    condition = "Coupan can applied  up to ${BuilderManager.getFormat().format(coupen.minAmt)} amount."
                }
            }
        }
        coupen.service?.let {
            if (CartDataInt.cartdata.serviceId!=it.serviceId){
                condition = "Available only for ${it.serviceName} option."
            }
        }
        coupen.paymentTypes?.let { it ->
            if (it.any { it.Id?:0L==CartDataInt.cartdata.paymentTypeId}.not()){
                val index = it.indexOfFirst { it.Id?:0L!=CartDataInt.cartdata.paymentTypeId}
                if (index>=0){
                    condition = if (it[index].Name!!.contains("Mltry")){
                        "Available only for Military Start Card payment option."
                    }else {
                        "Available only for ${it[index].Name} payment option."
                    }
                }
            }
        }
        val startdate = BuilderManager.getDateFromString(coupen.startDate)
        val enddate =  BuilderManager.getDateFromString(coupen.endDate)
        val nowdate = Date()
        if ((nowdate.after(startdate) && nowdate.before(enddate)).not()){
            condition = "You can applied this coupan in available dates."
        }
        return condition
    }

    private fun getCoupenPostTaxAmount(cartdata: CartData, coupen : Discount): Double {
        return when {
            coupen.type.equals("%") -> {
                val persent = coupen.disc*100
                var discount =  (persent * (cartdata.preTaxAmt+cartdata.taxAmt))/100   // cartdata.preTaxAmt - discountpersent
                if(coupen.maxCap > 0.0){
                    discount =  minOf(discount,coupen.maxCap)
                }
                discount
            }
            coupen.type.equals("$") -> coupen.disc
            else -> 0.0
        }
    }

    private fun getCoupenPreTaxAmount(cartdata: CartData, coupen : Discount): Double {
        return when {
            coupen.type.equals("%") -> {
                val persent = (100 - ceil((1 - coupen.disc) * 100).toInt())
                var discount = (persent * cartdata.preTaxAmt)/100
                if(coupen.maxCap > 0.0){
                    discount =  minOf(discount,coupen.maxCap)
                }
                discount
            }
            coupen.type.equals("$") -> coupen.disc
            else -> 0.0
        }
    }

    inner class ViewHolder(val adaptercoupenitemsbinding : AdapterCoupenItemsBinding) : RecyclerView.ViewHolder(adaptercoupenitemsbinding.root)


    fun setListener(coupenapplied:AppliedCoupen){
        this.coupenapplied = coupenapplied
    }

    fun addItems(discounts: ArrayList<Discount>) {
        this.discount = discounts
        notifyDataSetChanged()
    }

    interface AppliedCoupen{
        fun onAppliedCoupon()
    }


}