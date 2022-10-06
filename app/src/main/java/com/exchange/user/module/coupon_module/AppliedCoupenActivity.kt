package com.exchange.user.module.coupon_module

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityAppliedCoupenBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.coupon_module.adapter.CoupenListAdapter
import com.exchange.user.module.coupon_module.adapter.DiscountListAdapter
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Discount
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.OkButton
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AppliedCoupenActivity: BaseActivity<ActivityAppliedCoupenBinding, AppliedCoupenViewModel>(),AppliedCoupenNavigation,
    CoupenListAdapter.AppliedCoupen, DiscountListAdapter.AppliedCoupen {

    @Inject
    lateinit var factory: ViewModelProviderFactory
    @Inject
    lateinit var couponlistAdapter : CoupenListAdapter
    @Inject
    lateinit var discountlistAdapter : DiscountListAdapter

    private lateinit var applycouponviewModel: AppliedCoupenViewModel
    private lateinit var activitycountryBinding:ActivityAppliedCoupenBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_applied_coupen
    }
    override fun getViewModel(): AppliedCoupenViewModel {
        applycouponviewModel = ViewModelProvider(this, factory).get(AppliedCoupenViewModel::class.java)
        return applycouponviewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.appliedCoupenViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitycountryBinding = getViewDataBinding()
        applycouponviewModel.setNavigator(this)
        activitycountryBinding.recycleDisc.adapter = discountlistAdapter
        activitycountryBinding.recycleCoupen.adapter = couponlistAdapter
        couponlistAdapter.setListener(this,CartDataInt.cartdata)
        discountlistAdapter.setListener(this)
        initData()
    }

    override fun initData() {
        if (CartDataInt.validcoupon.validcoupon.isNullOrEmpty().not()) {
            val aavailbecoupon  =  getAvailbleCoupon(CartDataInt.validcoupon.validcoupon)
            if (aavailbecoupon.isNotEmpty()){
                activitycountryBinding.message.visibility = View.GONE
                activitycountryBinding.recycleCoupen.visibility = View.VISIBLE
                couponlistAdapter.addItems(aavailbecoupon)
            }else{
                activitycountryBinding.message.visibility = View.VISIBLE
                activitycountryBinding.recycleCoupen.visibility = View.GONE

            }
        } else {
            activitycountryBinding.message.visibility = View.VISIBLE
            activitycountryBinding.recycleCoupen.visibility = View.GONE
        }

        if (CartDataInt.restrurent.discounts.isNullOrEmpty().not()) {

            val availblediscount =  getAvailbleDiscount(CartDataInt.restrurent.discounts)

            if (availblediscount.isNotEmpty()){
                activitycountryBinding.dicmessage.visibility = View.GONE
                activitycountryBinding.recycleDisc.visibility = View.VISIBLE
                discountlistAdapter.addItems(availblediscount)
            }else{
                activitycountryBinding.dicmessage.visibility = View.VISIBLE
                activitycountryBinding.recycleDisc.visibility = View.GONE
            }


        } else {
            activitycountryBinding.dicmessage.visibility = View.VISIBLE
            activitycountryBinding.recycleDisc.visibility = View.GONE
        }
    }

    override fun onAppliedCoupon() {
        finish()
    }


    override fun onBacPress() {
        finish()
    }


    override fun searchCoupon() {
        applycouponviewModel.findPromo(activitycountryBinding.promotext.text.toString())
    }

    override fun message(s: String) {
        showFeedbackMessage(s)
    }

    override fun appliyPromo(coupon: Coupon) {
        if (isConditionG(coupon)){
            if(coupon.preTax.equals("T")){
                CartDataInt.cartdata.couponAmt = getCoupenPreTaxAmount(CartDataInt.cartdata,coupon)
                if (coupon.CType.equals("Generic")){
                    CartDataInt.cartdata.restChainCouponId = coupon.id
                    CartDataInt.cartdata.custCouponId = null
                }else{
                    CartDataInt.cartdata.custCouponId = coupon.custCouponId
                    CartDataInt.cartdata.restChainCouponId =null
                }
                CartDataInt.cartdata.couponType = 0
            }else{
                CartDataInt.cartdata.couponAmt = getCoupenPostTaxAmount(CartDataInt.cartdata,coupon)
                if (coupon.CType.equals("Generic")){
                    CartDataInt.cartdata.restChainCouponId = coupon.id
                    CartDataInt.cartdata.custCouponId = null
                }
                else{
                    CartDataInt.cartdata.custCouponId = coupon.custCouponId
                    CartDataInt.cartdata.restChainCouponId =null
                }
                CartDataInt.cartdata.couponType = 1
            }
            onAppliedCoupon()
        }else{
            BuilderManager.AlertMessageWihoutImage(this, "Error", getCouponMessageG(coupon), "OKAY", object :
                OkButton {
                override fun onClickOkey() {}
            })
        }
    }


    override fun showFeedbackMessage(message: String) {
        showBaseToast(activitycountryBinding.root,message)
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

    private fun getAvailbleCoupon(coupons: ArrayList<Coupon>) : ArrayList<Coupon>{
        val availablecoupons = ArrayList<Coupon>()
        coupons.forEach { items->
            if (isCondition(items)){
                availablecoupons.add(items)
            }
        }
        return availablecoupons

    }

    private fun getAvailbleDiscount(discounts: ArrayList<Discount>): ArrayList<Discount> {
        val availblediscounts =  ArrayList<Discount>()
        discounts.forEach { items->
            if (isCondition(items)){
                availblediscounts.add(items)
            }
        }
        return availblediscounts
    }


    private fun isConditionG(coupen: Coupon): Boolean {
        var condition = true
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition =  false
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minOrderAmt){
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

    private fun getCouponMessageG(coupen: Coupon): String  {
        var condition = ""
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition = "Coupon code does not meet the minimum order of ${BuilderManager.getFormat().format(coupen.minOrderAmt)}."
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minOrderAmt){
                    condition = "Coupon code does not meet the minimum order of ${BuilderManager.getFormat().format(coupen.minOrderAmt)}."
                }
            }
        }
        val startdate = BuilderManager.getDateFromString(coupen.issueDate)
        val enddate =  BuilderManager.getDateFromString(coupen.expDate)
        if ((Date().after(startdate) && Date().before(enddate)).not()){
            condition = "Coupon code has expired."
        }

        return condition
    }

    private fun isCondition(coupen: Coupon): Boolean {
        var condition = true
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition =  false
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minOrderAmt){
                    condition =  false
                }
            }
        }
        if ((coupen.usageCountLeft?:0)<= 0){
            condition =  false
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

    private fun getCouponMessage(coupen: Coupon): String  {
        var condition = ""
        if (coupen.preTax.equals("T")){
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.preTaxAmt < coupen.minOrderAmt){
                    condition = "Coupon code does not meet the minimum order of ${BuilderManager.getFormat().format(coupen.minOrderAmt)}."
                }
            }
        }else{
            if (coupen.minOrderAmt > 0.0){
                if (CartDataInt.cartdata.totalAmt < coupen.minOrderAmt){
                    condition = "Coupon code does not meet the minimum order of ${BuilderManager.getFormat().format(coupen.minOrderAmt)}."
                }
            }
        }
        if ((coupen.usageCountLeft?:0)<= 0){
            condition =  "Coupon code has already been used."
        }
        val startdate = BuilderManager.getDateFromString(coupen.issueDate)
        val enddate =  BuilderManager.getDateFromString(coupen.expDate)
        if ((Date().after(startdate) && Date().before(enddate)).not()){
            condition = "Coupon code has expired."
        }

        return condition
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
        coupen.lookup?.let {
            if (it.lookupName.equals("FirstOrder")){
                val orderhistory = CartDataInt.profiledata.orderHistory.find {
                    it.locationId==CartDataInt.cartdata.locationId
                }
                orderhistory?.let {
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

}