package com.exchange.user.module.order_confirm_module

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityOrderConfirmBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.OrderDataModel
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.home_module.HomeActivity
import com.exchange.user.module.order_info_module.OrderInfoActivity
import com.exchange.user.module.profile_module.model.OrderHistory
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.CodeScanerInit
import com.google.gson.GsonBuilder
import javax.inject.Inject

class OrderConfirmActivity: BaseActivity<ActivityOrderConfirmBinding, OrderConfirmViewModel>(),OrderConfirmNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var orderConfirmViewModel:OrderConfirmViewModel
    private lateinit var orderConfirmActivityBinding : ActivityOrderConfirmBinding
    private  lateinit var lastorderresponce: OrderHistory

    override fun getLayoutId(): Int {
        return R.layout.activity_order_confirm
    }

    override fun getViewModel(): OrderConfirmViewModel {
        orderConfirmViewModel =  ViewModelProvider(this, factory).get(OrderConfirmViewModel::class.java)
        return orderConfirmViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.orderConfirmViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderConfirmActivityBinding = getViewDataBinding()
        orderConfirmViewModel.setNavigator(this)
        initData()

    }
    @SuppressLint("SetTextI18n")
    override fun initData() {
        orderConfirmActivityBinding.msgno.text = "Please Contact ${CartDataInt.restrurent.tel} for further assistance"
        orderConfirmViewModel.getLastOrder()
        if (intent.getStringExtra(ConstantCommand.DATA).isNullOrEmpty().not()){
            orderConfirmActivityBinding.timetaken.text = intent.getStringExtra(ConstantCommand.DATA)
        }
    }

    override fun onBacPress() {
      onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI(lastorderresponce: OrderHistory) {
        lastorderresponce.locationId = CartDataInt.restrurent.id
        lastorderresponce.locationName = CartDataInt.restrurent.name
        if (lastorderresponce.locationName!=null){
            orderConfirmActivityBinding.returentname.text = lastorderresponce.locationName.toString()
        }else{
            orderConfirmActivityBinding.returentname.text = CartDataInt.restrurent.name
        }
        orderConfirmActivityBinding.orderno.text =  "Order No. = ${lastorderresponce.orderNumber}"
        orderConfirmActivityBinding.status.text =  "Status- ${lastorderresponce.status}"
        orderConfirmActivityBinding.amount.text =  BuilderManager.getFormat().format(lastorderresponce.totalAmt)

        if (lastorderresponce.restService.equals("On-base Delivery",true) ||
            lastorderresponce.restService.equals("Off-base Delivery",true)

        ){
            orderConfirmActivityBinding.imagedelievry.visibility = View.VISIBLE
            orderConfirmActivityBinding.imagePickup.visibility = View.GONE
        }
        else{
            orderConfirmActivityBinding.imagedelievry.visibility = View.GONE
            orderConfirmActivityBinding.imagePickup.visibility = View.VISIBLE
        }

        orderConfirmActivityBinding.serviceoption.text = "${lastorderresponce.restService} Order"
        orderConfirmActivityBinding.deliverytext.text ="Time for ${lastorderresponce.restService}"

        if (lastorderresponce.paymentType.equals("$$")){
            orderConfirmActivityBinding.typeofpayment.text ="Cash Payment"
        }else{
            orderConfirmActivityBinding.typeofpayment.text ="Online Payment"
        }
    }

    override fun onLastResonce(lastorderresponce: OrderHistory) {
        lastorderresponce.locationName =  CartDataInt.restrurent.name
        lastorderresponce.locationAddress =  orderConfirmViewModel.getLocationAddress(CartDataInt.restrurent.address)
        this.lastorderresponce = lastorderresponce
        initUI(lastorderresponce)
    }

    override fun startNewOrder() {
        finishPreference()
        val intent = Intent(this, OrderInfoActivity::class.java)
        intent.putExtra(ConstantCommand.DATA, GsonBuilder().setPrettyPrinting()
            .create().toJson(this.lastorderresponce))
        startActivity(intent)
        HomeActivity.selectTab(R.id.orderhistory)
    }

    override fun callToReasturant() {
        BuilderManager.requestPhonecallPermission(this@OrderConfirmActivity,object :
            CodeScanerInit {
            @SuppressLint("MissingPermission")
            override fun allowPermition() {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "${CartDataInt.restrurent.tel}")))
            }

        })
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(orderConfirmActivityBinding.root,message)
    }


    override fun onBackPressed() {
        finishPreference()
        HomeActivity.selectTab(R.id.home)
        super.onBackPressed()
    }

    fun finishPreference(){
        orderConfirmViewModel.clearPrefrence()
        CartDataInt.orderdata = OrderDataModel()
        CartDataInt.cartdata = CartData()
        CartDataInt.restrurent = RestrurentModel()
        CartDataInt.menudata = MenuModel()
        finish()
    }


}