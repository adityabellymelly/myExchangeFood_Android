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
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon
import com.exchange.user.module.utility_module.BuilderManager
import java.util.*
import kotlin.math.ceil

class CoupenListAdapter (
    var context: Context,
    private var coupons: ArrayList<Coupon>
): RecyclerView.Adapter<CoupenListAdapter.ViewHolder>() {
    private lateinit var coupenapplied : AppliedCoupen
    lateinit var cartdata: CartData
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adaptercoupenitemsbinding : AdapterCoupenItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_coupen_items, parent, false)
        return ViewHolder(adaptercoupenitemsbinding)
    }
    override fun getItemCount(): Int {
        return coupons.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adaptercoupenitemsbinding.nameofcoopen.text =  coupons[holder.adapterPosition].couponCode
        holder.adaptercoupenitemsbinding.discription.text =  coupons[holder.adapterPosition].description

        if (coupons[holder.adapterPosition].minOrderAmt>0.0){
            holder.adaptercoupenitemsbinding.abovemessage.text  = "On orders of ${BuilderManager.getFormat().format(coupons[holder.adapterPosition].minOrderAmt)} and above "
        }else{
            holder.adaptercoupenitemsbinding.abovemessage.text  = ""
        }
        if (coupons[holder.adapterPosition].CType.equals("Spacific")){
            holder.adaptercoupenitemsbinding.spacific.text = "Spacific"
            holder.adaptercoupenitemsbinding.spacific.visibility = View.VISIBLE
        }
        else{
            holder.adaptercoupenitemsbinding.spacific.visibility = View.GONE
        }
        if (coupons[holder.adapterPosition].type.equals("%")) {
            val discountobj = coupons[holder.adapterPosition].discount
            val persent = (100 - ceil((1 - discountobj) * 100).toInt())
            val off = StringBuilder()
            off.append("<strong>${persent}% Off</strong>")

            if (coupons[holder.adapterPosition].maxCap>0.0){
                off.append(" up to ${BuilderManager.getFormat().format(coupons[holder.adapterPosition].maxCap)}")
            }

            val htmlAsSpanned = HtmlCompat.fromHtml(off.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.adaptercoupenitemsbinding.persentoff.text = htmlAsSpanned
        }
        else
        {
            val off = StringBuilder()
            off.append("<strong>${BuilderManager.getFormat().format(coupons[holder.adapterPosition].discount)} Off</strong>")

            if (coupons[holder.adapterPosition].maxCap>0.0){
                off.append(" up to ${BuilderManager.getFormat().format(coupons[holder.adapterPosition].maxCap)}")
            }
            val htmlAsSpanned = HtmlCompat.fromHtml(off.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.adaptercoupenitemsbinding.persentoff.text = htmlAsSpanned
        }




        if(isCondition(coupons[holder.adapterPosition])){

            holder.itemView.setOnClickListener {
                if(coupons[holder.adapterPosition].preTax.equals("T")){

                    cartdata.couponAmt = getCoupenPreTaxAmount(cartdata,coupons[holder.adapterPosition])

                    if (coupons[holder.adapterPosition].CType.equals("Generic")){

                        cartdata.restChainCouponId = coupons[holder.adapterPosition].id
                        cartdata.custCouponId = null
                    } else{
                        cartdata.custCouponId = coupons[holder.adapterPosition].custCouponId
                        cartdata.restChainCouponId =null
                    }

                    cartdata.couponType = 0

                }else{

                    cartdata.couponAmt = getCoupenPostTaxAmount(cartdata,coupons[holder.adapterPosition])

                    if (coupons[holder.adapterPosition].CType.equals("Generic")){
                        cartdata.restChainCouponId = coupons[holder.adapterPosition].id
                        cartdata.custCouponId = null
                    } else{
                        cartdata.custCouponId = coupons[holder.adapterPosition].custCouponId
                        cartdata.restChainCouponId =null
                    }
                    cartdata.couponType = 1

                }
                coupenapplied.onAppliedCoupon()
            }

            holder.adaptercoupenitemsbinding.applied.setOnClickListener {
                if(coupons[holder.adapterPosition].preTax.equals("T")){

                    cartdata.couponAmt = getCoupenPreTaxAmount(cartdata,coupons[holder.adapterPosition])

                    if (coupons[holder.adapterPosition].CType.equals("Generic")){

                        cartdata.restChainCouponId = coupons[holder.adapterPosition].id
                        cartdata.custCouponId = null
                    } else{
                        cartdata.custCouponId = coupons[holder.adapterPosition].custCouponId
                        cartdata.restChainCouponId =null
                    }

                    cartdata.couponType = 0

                }else{

                    cartdata.couponAmt = getCoupenPostTaxAmount(cartdata,coupons[holder.adapterPosition])

                    if (coupons[holder.adapterPosition].CType.equals("Generic")){
                        cartdata.restChainCouponId = coupons[holder.adapterPosition].id
                        cartdata.custCouponId = null
                    } else{
                        cartdata.custCouponId = coupons[holder.adapterPosition].custCouponId
                        cartdata.restChainCouponId =null
                    }
                    cartdata.couponType = 1

                }
                coupenapplied.onAppliedCoupon()
            }

        }else{

            holder.adaptercoupenitemsbinding.spacific.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
            holder.adaptercoupenitemsbinding.nameofcoopen.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))
            holder.adaptercoupenitemsbinding.applied.visibility = View.GONE
            val message = getMessage(coupons[holder.adapterPosition])
            holder.adaptercoupenitemsbinding.persentoff.setTextColor(ContextCompat.getColor(context,R.color.dark_gray))

            holder.itemView.setOnClickListener {
                Toast.makeText(context,message, Toast.LENGTH_SHORT).show() }

        }
    }




    private fun getCoupenPostTaxAmount(cartdata: CartData, coupen : Coupon): Double {
        return if(coupen.type.equals("%")){
            val persent = (100 - Math.ceil((1 - coupen.discount) * 100).toInt())
            val persentageAmount = (persent * (cartdata.preTaxAmt+cartdata.taxAmt))/100
            var discount = cartdata.preTaxAmt - persentageAmount
            if(coupen.maxCap > 0.0){
                discount =  minOf(discount,coupen.maxCap)
            }
            discount
        }else if(coupen.type.equals("$")){
            coupen.discount

        }else{
            0.0
        }


    }

    private fun getCoupenPreTaxAmount(cartdata: CartData, coupen : Coupon): Double {
        return if(coupen.type.equals("%")){
            val persent = (100 - Math.ceil((1 - coupen.discount) * 100).toInt())
            var discount = (persent * cartdata.preTaxAmt)/100

            if(coupen.maxCap > 0.0){
                discount =  minOf(discount,coupen.maxCap)
            }
            discount
        }else if(coupen.type.equals("$")){
            coupen.discount

        }else{
            0.0
        }
    }

    private fun isCondition(coupen: Coupon): Boolean {
        var condition = true
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition =  false
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (cartdata.totalAmt < coupen.minOrderAmt){
                    condition =  false
                }
            }
        }
        try{
            val startdate = BuilderManager.getDateFromString(coupen.issueDate)
            val enddate =  BuilderManager.getDateFromString(coupen.expDate)
            if ((Date().after(startdate) && Date().before(enddate)).not()){
                condition = false
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return condition
    }


    private fun getMessage(coupen: Coupon): String  {
        var condition = ""
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition = "Coupon can applied  up to ${BuilderManager.getFormat().format(coupen.minOrderAmt)} amount."
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (cartdata.totalAmt < coupen.minOrderAmt){
                    condition = "Coupon can applied  up to ${BuilderManager.getFormat().format(coupen.minOrderAmt)} amount."
                }
            }
        }
        val startdate = BuilderManager.getDateFromString(coupen.issueDate)
        val enddate =  BuilderManager.getDateFromString(coupen.expDate)
        if ((Date().after(startdate) && Date().before(enddate)).not()){
            condition = "You can applied this coupan in available dates."
        }

        return condition
    }

    fun setListener(coupenapplied: AppliedCoupen,cartdata:CartData){
        this.coupenapplied = coupenapplied
        this.cartdata=cartdata
    }

    fun addItems(coupons: ArrayList<Coupon>) {
       this.coupons = coupons
        notifyDataSetChanged()
    }

    interface AppliedCoupen{
        fun onAppliedCoupon()
    }

    inner class ViewHolder(val adaptercoupenitemsbinding : AdapterCoupenItemsBinding) : RecyclerView.ViewHolder(adaptercoupenitemsbinding.root)

}