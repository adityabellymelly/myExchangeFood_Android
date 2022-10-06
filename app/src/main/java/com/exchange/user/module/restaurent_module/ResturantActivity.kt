package com.exchange.user.module.restaurent_module

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.view.ActionMode
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.exchange.user.R
import com.exchange.user.databinding.ActivityRestaurentBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.action.AddItemsToCart
import com.exchange.user.module.cart_module.model.card_model.ItemList
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.home_module.HomeActivity
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.restaurant_module.adapter.MenuAdapter
import com.exchange.user.module.restaurent_module.adapter.AboutPaymentAdapter
import com.exchange.user.module.restaurent_module.adapter.AboutServiceAdapter
import com.exchange.user.module.restaurent_module.model.LatLongModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.ImageRepository
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.OkButton
import com.google.gson.Gson
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ResturantActivity : BaseActivity<ActivityRestaurentBinding, ResturentViewModel>(),ResturentNavigator,
    AddItemsToCart {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    @Inject
    lateinit var aboutPaymentAdapter: AboutPaymentAdapter
    @Inject
    lateinit var aboutServiceAdapter: AboutServiceAdapter
    private lateinit var restaurantViewModel:ResturentViewModel
    private lateinit var restaurantActivityBinding : ActivityRestaurentBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_restaurent
    }
    override fun getViewModel(): ResturentViewModel {
        restaurantViewModel =  ViewModelProvider(this, factory).get(ResturentViewModel::class.java)
        return restaurantViewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.resturentViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurantActivityBinding = getViewDataBinding()
        restaurantViewModel.setNavigator(this)
        initData()
    }
    override fun onActionModeStarted(mode: ActionMode?) {
            val menu = mode?.menu
            menu?.clear()
            menu?.removeItem(android.R.id.copy)
            menu?.removeItem(android.R.id.paste)
            menu?.removeItem(android.R.id.selectAll)
            super.onActionModeStarted(mode)
    }
    override fun initData() {
        val restLocationId: LocList = intent.getSerializableExtra(ConstantCommand.DATA) as LocList
        restaurantActivityBinding.title.text= restLocationId.LocName
        restaurantActivityBinding.name.text = restLocationId.LocName
        restaurantViewModel.getToken(restLocationId)
    }

    override fun moreAbout(b: Boolean) {
        if (b){
            restaurantActivityBinding.aboutimage.setBackgroundResource(R.drawable.ic_info)
            restaurantActivityBinding.expandOption.visibility = View.VISIBLE
        }else{
            restaurantActivityBinding.aboutimage.setBackgroundResource(R.drawable.ic_info_red)
            restaurantActivityBinding.expandOption.visibility = View.GONE
        }
    }

    override fun viewDiscount() {

    }

    @SuppressLint("SetTextI18n")
    override fun onLocation(resturentata: RestrurentModel) {
        if(resturentata.header.isNullOrEmpty().not()){
           // BuilderManager.LoadImage(restaurentactivityBinding.restrologo,resturentata.logoImg)
            BuilderManager.LoadImage(restaurantActivityBinding.headerImage,resturentata.header)
            BuilderManager.LoadImage(restaurantActivityBinding.headerbackgroundImage,resturentata.headerBackgroundImage)
        }
        else{
            restaurantActivityBinding.headerImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.image))
           restaurantActivityBinding.headerbackgroundImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.image))
        }

        restaurantActivityBinding.aboutpaymentrecy.adapter = aboutPaymentAdapter
        restaurantActivityBinding.aboutservicerecy.adapter =aboutServiceAdapter
        if (resturentata.discounts.isEmpty().not()){
            val htmlAsSpanned :Spanned
            val ammount: String
            val message: String
            if (resturentata.discounts[0].type.equals("%")){
                 ammount = "${resturentata.discounts[0].disc * 100}%"
                 htmlAsSpanned = HtmlCompat.fromHtml( "<strong>${resturentata.discounts[0].disc.times(100)}%<strong> on Your Order.Tap Here!", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }else{
                ammount = BuilderManager.getFormat().format(resturentata.discounts[0].disc)
                htmlAsSpanned = HtmlCompat.fromHtml( "<strong>$${BuilderManager.getFormat().format(resturentata.discounts[0].disc)}<strong> on Your Order.Tap Here!", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            var ispaymentDiscount = false
            resturentata.discounts[0].paymentTypes?.let {paymenttype->
                if (paymenttype.isNotEmpty()){
                    ispaymentDiscount = resturentata.paymentTypes?.any { it.id== paymenttype[0].Id}?:false
                }
            }

            if (ispaymentDiscount) {
                var paymentname: String
                val paymenttype = resturentata.paymentTypes?.find {
                    it.id == resturentata.discounts[0].paymentTypes?.get(0)?.Id
                }
                if (paymenttype?.type.equals("CC", true)) {
                    paymentname = getString(R.string.debit_credit_card)
                } else if (paymenttype?.type.equals("STAR", true)) {
                    paymentname = getString(R.string.miltry_star_card)
                } else {
                    paymentname = paymenttype?.desc ?: ""
                }
                message = "Get ${ammount} off when you pay using ${paymentname}"
            }else{
                message = "Discount ${htmlAsSpanned}"
            }

            restaurantActivityBinding.discountlay.visibility = View.VISIBLE
            restaurantActivityBinding.discount.visibility = View.VISIBLE
            restaurantActivityBinding.discount.text = htmlAsSpanned
            restaurantActivityBinding.discountMessage.text="Save $htmlAsSpanned"


            restaurantActivityBinding.discountMessage.setOnClickListener {
                BuilderManager.AlertMessageWihoutImage(this,message,resturentata.discounts[0].description?:"",
                    getString(R.string.okay),object :OkButton{
                        override fun onClickOkey() {

                        }

                    })
            }
            restaurantActivityBinding.discountlay.setOnClickListener {
                BuilderManager.AlertMessageWihoutImage(this,message,resturentata.discounts[0].description?:"",
                    getString(R.string.okay),object :OkButton{
                        override fun onClickOkey() {

                        }

                    })
            }
        }else{
            restaurantActivityBinding.discountMessage.visibility = View.GONE
            restaurantActivityBinding.discountlay.visibility = View.INVISIBLE
        }
      // val htmlAsSpanneddis = HtmlCompat.fromHtml(resturentata.description?:"", HtmlCompat.FROM_HTML_MODE_LEGACY)
        restaurantActivityBinding.moreabout.loadDataWithBaseURL(null, resturentata.description?:"", "text/html", "utf-8", null)

        val open = BuilderManager.isResturentOpen(
            resturentata.schedule!!,
            resturentata.calendar!!.openOrCloseYearMask!!,
            resturentata.timeZone!!
        )
        if (open) {
            restaurantActivityBinding.openandCloseTime.text = "Open ".plus(BuilderManager.getOpenCloseTime())
            restaurantActivityBinding.curentOpenStatus.text = "Open ".plus(BuilderManager.getOpenCloseTime())
            restaurantActivityBinding.curentOpenStatus.setTextColor(ContextCompat.getColor(this, R.color.color_green))
            restaurantActivityBinding.restroimageopen.setBackgroundResource(R.drawable.ic_open_green)
        }
        else {
            restaurantActivityBinding.openandCloseTime.text =if (BuilderManager.getOpenDateTime()!=null){
                  "Closed till ${BuilderManager.getOpenDateTime()?.format(DateTimeFormatter.ofPattern("hh:mm a"))}"
            }else{
                "Closed"
            }
            restaurantActivityBinding.curentOpenStatus.setTextColor(ContextCompat.getColor(this, R.color.color_red))
            restaurantActivityBinding.curentOpenStatus.text =
            if (BuilderManager.getOpenDateTime()!=null){
                  "Closed till ${BuilderManager.getOpenDateTime()?.format(DateTimeFormatter.ofPattern("hh:mm a"))}"
            }else{
                "Closed"
            }
            restaurantActivityBinding.restroimageopen.setBackgroundResource(R.drawable.ic_open_red)
            if (resturentata.name.toString().contains("- Exchange")) {
                val sub = resturentata.name.toString()
                restaurantActivityBinding.name.text = sub
                    .substring(0, resturentata.name.toString().lastIndexOf("-"))
                BuilderManager.AlertMessage(this, sub, "Sorry, We are currently Closed.", "Okay", object : OkButton {
                        override fun onClickOkey() {}
                })
            } else {
                restaurantActivityBinding.name.text = resturentata.name
                BuilderManager.AlertMessage(this,resturentata.name!!, "Sorry, We are currently Closed.", "Okay", object : OkButton {
                        override fun onClickOkey() {}
                })
            }
        }
        restaurantActivityBinding.aboutimage.setBackgroundResource(R.drawable.ic_info_red)
        resturentata.paymentTypes?.let {
            aboutPaymentAdapter.addItem(it)
        }
        resturentata.services?.let {
            aboutServiceAdapter.addItem(it)
        }
        if (resturentata.address!= null) {
            val restaddress = StringBuilder()
            if (!resturentata.address!!.addressLine1.isNullOrEmpty()) {
                restaddress.append("${resturentata.address!!.addressLine1},")
            }
            restaurantActivityBinding.address.text  = restaddress
        } else {
            restaurantActivityBinding.address.text  = ""
        }

        restaurantActivityBinding.tellno.text= resturentata.tel
        restaurantActivityBinding.picuptime.text = BuilderManager.pickuptime
        restaurantActivityBinding.deliverytime.text = getString(R.string.not_available)
        try {

            resturentata.services?.let {
                val point = LatLongModel()
                point.lat = CartDataInt.restrurent.lat?:0.0
                point.log = CartDataInt.restrurent.lon?:0.0
                if (resturentata.services.any { it.name.equals("On-base Delivery",true) ||
                            it.name.equals("Off-base Delivery",true)}.not()){
                    restaurantActivityBinding.deliverytime.text = getString(R.string.not_available)
                }else{
                    restaurantActivityBinding.deliverytime.text = getString(R.string.available)
                }
            }
        } catch (e:Exception) {e.printStackTrace() }

        restaurantActivityBinding.resturentdetaillay.visibility = View.VISIBLE
        resturentata.alert?.let {
            it.forEach { items->
                val startdate = BuilderManager.getDateFromString(items.startDate)
                val enddate =  BuilderManager.getDateFromString(items.endDate)
                val  nowdate  = BuilderManager.getDate(resturentata.timeZone!!)?:Date()
                if ((nowdate.after(startdate) && nowdate.before(enddate))){
                    BuilderManager.AlertMessageWihoutImage(this,items.text?:"","",
                        getString(R.string.okay),object :OkButton{
                            override fun onClickOkey() {

                            }
                        })
                }
            }
        }

    }
    override fun onUpdateMenu(menudata: MenuModel) {
        val data2 = intent.getStringExtra(ConstantCommand.DATA_2)
        if (data2!=null){
            val orderHistory  = Gson().fromJson(data2, OrderInfoModel::class.java)
            restaurantViewModel.addReOrderItems(menudata,orderHistory)
        }
        when {
            (menudata.asap?:"F") =="T" -> CartDataInt.cartdata.timeSelection = 0
            (menudata.lt?:"F")=="T" -> BuilderManager.AlertMessage(this,getString(R.string.app_name),
                getString(R.string.msg_later),
                getString(R.string.okay),
                object : OkButton { override fun onClickOkey() {}
                })
            (menudata.fo?:"F")=="T" -> BuilderManager.AlertMessage(this,getString(R.string.app_name),
                getString(R.string.msg_future),
                getString(R.string.okay),
                object : OkButton { override fun onClickOkey() {}
                })
        }
        initRecycle(menudata.catList,menudata.imageRepository)
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(restaurantActivityBinding.root,message)
    }

    private fun initRecycle(
        location: List<CatList>?,
        imageRepository: List<ImageRepository>?
    ) {
        if (location!=null) {
            restaurantActivityBinding.menurecycle.layoutManager = LinearLayoutManager(this)
            //val catagoury =   BuilderManager.removeCatagoury(location,CartDataInt.restrurent.timeZone!!)
            restaurantActivityBinding.menurecycle.adapter = MenuAdapter(this, location,this,
                restaurantActivityBinding.searchItems,CartDataInt.cartdata,CartDataInt.suggestion,
                CartDataInt.restrurent,imageRepository)
            calculateTotal()
        }
    }


    override fun onClosedSearch() {
        hideKeyboard()
        val autoTransition = AutoTransition()
        autoTransition.duration = 100
        TransitionManager.beginDelayedTransition(restaurantActivityBinding.container,autoTransition)
        restaurantActivityBinding.back.visibility  = View.VISIBLE
        restaurantActivityBinding.searchItems.clearFocus()
        restaurantActivityBinding.searchItems.setText("")
        restaurantActivityBinding.serchview.visibility  = View.GONE
        restaurantActivityBinding.searchicon.visibility  = View.VISIBLE
        restaurantActivityBinding.title.visibility = View.VISIBLE

    }

    override fun onOpenSearch() {
        val autoTransition = AutoTransition()
        autoTransition.duration = 100
        TransitionManager.beginDelayedTransition(restaurantActivityBinding.container,autoTransition)
        restaurantActivityBinding.searchItems.requestFocus()
        restaurantActivityBinding.back.visibility  = View.GONE
        restaurantActivityBinding.serchview.visibility  = View.VISIBLE
        restaurantActivityBinding.searchicon.visibility = View.GONE
        restaurantActivityBinding.title.visibility = View.GONE
    }

    override fun onBack() {
        finish()
    }

    override fun moveToCart() {
        if (CartDataInt.cartdata.itemList.isEmpty())
            showBaseToast(restaurantActivityBinding.root, "Please add items into cart.")
        else if (restaurantViewModel.getUserID().isNullOrEmpty().not() && CartDataInt.profiledata.id!=null){
            CartDataInt.cartdata.custId=CartDataInt.profiledata.id
            restaurantViewModel.saveAddress()
            restaurantViewModel.getAvailableCoupon()
        }else{
            startActivity(Intent(applicationContext,SignInActivity::class.java))
        }
    }


    @SuppressLint("SetTextI18n")
    fun calculateTotal(){
        if(CartDataInt.cartdata.itemList.isNotEmpty()) {
            restaurantActivityBinding.cordinate.visibility = View.VISIBLE
            var amount = 0.0
            var items = 0
            for (x in 0 until CartDataInt.cartdata.itemList.size) {
                var amountvalue = 0.0
                if (!CartDataInt.cartdata.itemList[x].havespicialoffer) {
                    amountvalue += CartDataInt.cartdata.itemList[x].unitPrice
                    CartDataInt.cartdata.itemList[x].itemAddOnList.forEach { addonItmes->
                        addonItmes.addOnOptions.forEach { addonOption->
                            amountvalue += addonOption.amt
                        }
                    }

                }
                amount += amountvalue.times(CartDataInt.cartdata.itemList[x].qty.toInt())
                items += CartDataInt.cartdata.itemList[x].qty.toInt()
            }
            CartDataInt.cartdata.preTaxAmt = amount
            restaurantActivityBinding.totleamount.text = BuilderManager.getFormat().format(amount)
            restaurantActivityBinding.noofItems.text = items.toString().plus(" Items")
            restaurantViewModel.saveData(true)
        }
        else{
            restaurantActivityBinding.cordinate.visibility = View.GONE
            restaurantActivityBinding.totleamount.text =""
            restaurantActivityBinding.noofItems.text = "0 Items"
            restaurantViewModel.saveData(false)
        }
    }


    override fun onAddItemToCart(items: ItemList) {
        CartDataInt.cartdata.itemList.add(items)
        calculateTotal()
    }

    override fun onAddItemToCartPopUp(items: ItemList) {
        CartDataInt.cartdata.itemList.add(items)
        calculateTotal()
    }

    override fun openImage(img: String?, name: String?) {
        BuilderManager.ViewImage(this@ResturantActivity,img,false)
    }


    override fun onsetItemToCart(items: ItemList) {
        for (x in 0 until CartDataInt.cartdata.itemList.size){

            if (CartDataInt.cartdata.itemList.get(x).id == items.id)
            {
                CartDataInt.cartdata.itemList.set(x,items)
            }
        }
        calculateTotal()    }

    override fun onRemoveItemToCart(items: ItemList) {
        try {
            for (x in 0 until CartDataInt.cartdata.itemList.size) {
                if (CartDataInt.cartdata.itemList.get(x).id == items.id) {
                    CartDataInt.cartdata.itemList.removeAt(x)
                }
            }
            calculateTotal()
        }catch (e : Exception){
            e.stackTrace
        }
    }

    override fun showProgress(b: Boolean) {
        if (b){
            restaurantActivityBinding.menurecycle.visibility = View.GONE
            restaurantActivityBinding.animation.visibility = View.VISIBLE
        }else{

            restaurantActivityBinding.animation.visibility = View.GONE
            restaurantActivityBinding.menurecycle.visibility = View.VISIBLE
        }
    }


    override fun oncartProgress(b: Boolean) {
        val autoTransition = AutoTransition()
        autoTransition.duration = 100
        TransitionManager.beginDelayedTransition(restaurantActivityBinding.totelcontener, autoTransition)
        if (b){
            restaurantActivityBinding.animationView.visibility = View.VISIBLE
            restaurantActivityBinding.btnViewCart.visibility = View.GONE
        }else{
            restaurantActivityBinding.animationView.visibility = View.GONE
            restaurantActivityBinding.btnViewCart.visibility = View.VISIBLE
        }
    }


    override fun onCoupon() {
        HomeActivity.selectTab(R.id.mycart)
        finish()
    }

}