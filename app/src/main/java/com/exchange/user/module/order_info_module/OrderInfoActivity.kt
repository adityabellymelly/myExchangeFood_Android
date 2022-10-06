package com.exchange.user.module.order_info_module

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityOrderInfoBinding
import com.exchange.user.databinding.ViewEreceiptBinding
import com.exchange.user.databinding.ViewHelpBinding
import com.exchange.user.databinding.ViewTrackingOrderBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.*
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.feedback_module.FeedbackActivity
import com.exchange.user.module.order_history.OrderHistoryActivity
import com.exchange.user.module.order_info_module.adapter.OrderInfoItemsAdapter
import com.exchange.user.module.order_info_module.adapter.QuickMessageAdapter
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.profile_module.model.OrderHistory
import com.exchange.user.module.restaurent_module.ResturantActivity
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.CodeScanerInit
import com.exchange.user.module.utility_module.dialog_commands.TrackOrderCallBack
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rey.material.app.BottomSheetDialog
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OrderInfoActivity : BaseActivity<ActivityOrderInfoBinding, OrderInfoViewModel>(),OrderInfoNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    @Inject
    lateinit var orderInfoItemsAdapter : OrderInfoItemsAdapter
    var trackorder : TrackOrderCallBack? = null
    private lateinit var timer: Timer
    private lateinit var orderInfoViewModel: OrderInfoViewModel
    private lateinit var activityOrderInfoBinding : ActivityOrderInfoBinding
    private lateinit var orderHistory : OrderHistory
    lateinit var location : RestrurentModel
    private lateinit var orderinforesonce : OrderInfoModel
    private  lateinit var deliveryStatus : DeliveryStatus
    private var dialog: BottomSheetDialog? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_order_info
    }
    override fun getViewModel(): OrderInfoViewModel {
        orderInfoViewModel = ViewModelProvider(this, factory).get(OrderInfoViewModel::class.java)
        return orderInfoViewModel
    }
    override fun getBindingVariable(): Int {
        return BR.orderInfoViewModel
    }
    private fun startTimer() {
        val timerTask = object : TimerTask() {
            override fun run() {
                when(orderHistory.restService){
                    "Pickup" -> {
                        activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                        orderInfoViewModel.getRepetTokenDemo(orderHistory.orderId)
                    }
                    "Curbside" -> {
                        activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                        orderInfoViewModel.getRepetOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                    "On-base Delivery" -> {
                        orderInfoViewModel.getRepetOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                    "Off-base Delivery" -> {
                        orderInfoViewModel.getRepetOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                }
            }}
        try {
            timer.schedule(timerTask,1000,1*10*1000)
        }catch (e:Exception){
            onStart()
            e.toString()
        }

    }


    override fun showFeedbackMessage(message: String) {
        showBaseToast(activityOrderInfoBinding.root, message)

    }
    private val incomingNotification: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConstantCommand.INCOMING_NOTIFICATION) {
                when(orderHistory.restService){
                    "Pickup" -> {
                        activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                        orderInfoViewModel.getTokenDemo(orderHistory.orderId)
                    }
                    "Curbside" -> {
                        activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                        orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                    "On-base Delivery" -> {
                        orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                    "Off-base Delivery" -> {
                        orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                            orderHistory.orderId)
                    }
                }
            }
        }
    }

    private fun registerBroadcast() {
        val filter = IntentFilter()
        filter.addAction(ConstantCommand.INCOMING_NOTIFICATION)
        registerReceiver(incomingNotification, IntentFilter(filter))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOrderInfoBinding = getViewDataBinding()
        orderInfoViewModel.setNavigator(this)
        orderInfoViewModel.initProgress(this)
        activityOrderInfoBinding.orderitems.adapter = orderInfoItemsAdapter
        registerBroadcast()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(incomingNotification)
        timer.cancel()
    }


    override fun onStart() {
        timer = Timer()
        startTimer()
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
       /// timer.cancel()
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        orderHistory  = Gson().fromJson(
            intent.getStringExtra(ConstantCommand.DATA),
            OrderHistory::class.java
        )
        activityOrderInfoBinding.restroname.text = orderHistory.locationName
        activityOrderInfoBinding.restroaddress.text = orderHistory.locationAddress
        activityOrderInfoBinding.orderno.text = "Order#: ${orderHistory.orderNumber}"
        activityOrderInfoBinding.orderdate.text = orderHistory.createdOn

        if ((orderHistory.status?:"Open")=="Delivered"){
            activityOrderInfoBinding.phonecall.visibility = View.GONE
        }
        when(orderHistory.restService){
            "Pickup" -> {
                activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                orderInfoViewModel.getTokenDemo(orderHistory.orderId)
            }
            "Curbside" -> {
                activityOrderInfoBinding.driverBioLay.visibility = View.GONE
                orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                    orderHistory.orderId)
            }
            "On-base Delivery" -> {
                orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                    orderHistory.orderId)
            }
            "Off-base Delivery" -> {
                orderInfoViewModel.getOrderDeliveryStatusForCustomer(
                     orderHistory.orderId)
            }
        }

    }

    override fun reOrder() {
        try {
            setCartData()
        }catch (e: Exception){
            Log.e("Error-  ", e.message ?: "")
            e.printStackTrace()
        }
    }

    override fun getEReceipt() {
       getEReceiptUI()
    }

    override fun getHelp() {
        getHelpUI()
    }

    override fun trackingHistory() {
        if (::deliveryStatus.isInitialized){
            getTrackingHistory(orderHistory,orderinforesonce,deliveryStatus)
        }else{
            getTrackingHistory(orderHistory,orderinforesonce,null)
        }
    }

    override fun callToDriver() {
        BuilderManager.requestPhonecallPermission(this@OrderInfoActivity, object :
            CodeScanerInit {
            @SuppressLint("MissingPermission")
            override fun allowPermition() {
                startActivity(
                    Intent(Intent.ACTION_CALL,Uri.parse("tel:" + "${deliveryStatus.driverPhone?.trim()}"))
                )
            }

        })
    }
    override fun onBack() {
        onBackPressed()
    }

    override fun onLocMobileUrl(mobileUrl: String) {

    }

    override fun previewParcelImage() {
        BuilderManager.ViewImage(this@OrderInfoActivity,
            orderinforesonce.deliveryParcelImg,true)
    }

    override fun onOrderInfoTimer(resturentata: OrderInfoModel) {
//        if (resturentata.deliveryParcelImg.isEmpty()){
//            resturentata.deliveryParcelImg = "https://s3.amazonaws.com/v1.0/images/menuitemimages/266_182291_123021031413.png"
//        }
        orderinforesonce  =  resturentata
        var orderstatus = orderHistory.status?:"Open"
        when(orderHistory.restService){
            "Pickup" -> {
                orderstatus = if (resturentata.isOrderConfirmed =="1" && resturentata.status != "Delivered"){
                    "Confirmed"
                }else{
                    resturentata.status?:"Open"
                }
                if (orderstatus!= orderHistory.status){
                    trackorder?.onResult(orderinforesonce, deliveryStatus,orderHistory)
                }

            }
            "Curbside" -> {
                orderstatus = if (resturentata.isOrderConfirmed =="1" && resturentata.status != "Delivered"){
                    "Confirmed"
                }else{
                    resturentata.status?:"Open"
                }
                if (orderstatus!= orderHistory.status){
                    trackorder?.onResult(orderinforesonce, deliveryStatus,orderHistory)
                }
            }
            "On-base Delivery" -> {
                orderstatus = if (resturentata.isOrderConfirmed =="1" &&
                    deliveryStatus.isDriverEnroute == "1"
                    && resturentata.status != "Delivered"){
                    "Enroute"
                } else if (resturentata.isOrderConfirmed =="1" && resturentata.status != "Delivered"){
                    "Confirmed"
                } else{
                    resturentata.status?:"Open"
                }
                if (orderstatus!= orderHistory.status){
                    trackorder?.onResult(orderinforesonce, deliveryStatus,orderHistory)
                }
            }
            "Off-base Delivery" -> {
                orderstatus = if (resturentata.isOrderConfirmed =="1" &&
                    deliveryStatus.isDriverEnroute == "1"
                    && resturentata.status != "Delivered"){
                    "Enroute"
                } else if (resturentata.isOrderConfirmed =="1" && resturentata.status != "Delivered"){
                    "Confirmed"
                } else{
                    resturentata.status?:"Open"
                }
                if (orderstatus!= orderHistory.status){
                    trackorder?.onResult(orderinforesonce, deliveryStatus,orderHistory)
                }
            }
        }
    }

    override fun onUpdateDeliveryStatusTimer(delievryStatusData: DeliveryStatus) {
         deliveryStatus = delievryStatusData
        if ((orderinforesonce.isOrderConfirmed =="1"
                    && orderinforesonce.status != "Delivered")
            && deliveryStatus.isDriverEnroute == "1" && orderHistory.status !="Enroute"){
              trackorder?.onResult(orderinforesonce, deliveryStatus,orderHistory)
          }
    }

    @SuppressLint("SetTextI18n")
    override fun onOrderInfo(resturentata: OrderInfoModel) {
//        if (resturentata.deliveryParcelImg.isEmpty()){
//            resturentata.deliveryParcelImg = "https://s3.amazonaws.com/v1.0/images/menuitemimages/266_182291_123021031413.png"
//        }
        orderinforesonce = resturentata
        activityOrderInfoBinding.progresslay.visibility = View.GONE
        activityOrderInfoBinding.viewLay.visibility = View.VISIBLE
        val readdress = StringBuilder()
        if (resturentata.restAddr1.isNullOrEmpty().not()){
            readdress.append("${resturentata.restAddr1}, ")
        }
        if (resturentata.restAddr2.isNullOrEmpty().not()){
            readdress.append("${resturentata.restAddr2}")
        }
        activityOrderInfoBinding.restroaddress.text  = readdress
        BuilderManager.LoadImage(activityOrderInfoBinding.headerImage, resturentata.restHeader)
        BuilderManager.LoadImage(
            activityOrderInfoBinding.headerbackgroundImage,
            resturentata.headerBackgroundImage
        )
        if (resturentata.totalAmt.isNotEmpty()){
            activityOrderInfoBinding.gradtotle.text = BuilderManager.getFormat().format(resturentata.totalAmt.toDouble())
        }
        if (resturentata.preTaxAmt.isNotEmpty()) {
            activityOrderInfoBinding.itemstotel.text =
                BuilderManager.getFormat().format(resturentata.preTaxAmt.toDouble())
        } else {
            activityOrderInfoBinding.itemstotel.text = BuilderManager.getFormat().format(0.0)
        }
        if (resturentata.taxAmt.isNotEmpty()) {
            activityOrderInfoBinding.gstapplied.text =
                BuilderManager.getFormat().format(resturentata.taxAmt.toDouble())
        } else {
            activityOrderInfoBinding.gstapplied.text = BuilderManager.getFormat().format(0.0)
        }
        if (resturentata.tipAmt.isNotEmpty()) {
            activityOrderInfoBinding.tipadd.text =
                BuilderManager.getFormat().format(resturentata.tipAmt.toDouble())
        } else {
            activityOrderInfoBinding.tipadd.text = BuilderManager.getFormat().format(0.0)
        }
        if (resturentata.srvcFee.isEmpty().not()) {
            activityOrderInfoBinding.deliverycharge.text =
                BuilderManager.getFormat().format(resturentata.srvcFee.toDouble())
        } else {
            activityOrderInfoBinding.deliverycharge.text = BuilderManager.getFormat().format(0.0)
        }

        if (resturentata.totalAmt.isNotEmpty()) {
            activityOrderInfoBinding.gradtotle.text =
                BuilderManager.getFormat().format(resturentata.totalAmt.toDouble())

        } else {
            activityOrderInfoBinding.gradtotle.text = BuilderManager.getFormat().format(0.0)
        }

        if (resturentata.couponAmt.isNotEmpty()){
            activityOrderInfoBinding.couponlay.visibility = View.VISIBLE
            activityOrderInfoBinding.copendiscount.text = "-${BuilderManager.getFormat().format(
                resturentata.couponAmt.toDouble()
            )}"

        }
        if (resturentata.discountAmt.isNotEmpty()){
            activityOrderInfoBinding.discountlay.visibility = View.VISIBLE
            activityOrderInfoBinding.discount.text =  "-${BuilderManager.getFormat().format(
                resturentata.discountAmt.toDouble()
            )}"
        }
        if (resturentata.discount1Amt.isNotEmpty()){
            activityOrderInfoBinding.discountlay2.visibility = View.VISIBLE
            activityOrderInfoBinding.discount2.text =  "-${BuilderManager.getFormat().format(
                resturentata.discount1Amt.toDouble()
            )}"
        }
        activityOrderInfoBinding.priceCalcuLay.visibility = View.VISIBLE

        resturentata.itemList?.let {
            activityOrderInfoBinding.countLay.visibility = View.VISIBLE
            activityOrderInfoBinding.itemsCount.text =  if(it.size > 1){
                "${it.size} Items"
            }else{
                "${it.size} Item"
            }
            orderInfoItemsAdapter.addItems(it)
        }

        if (resturentata.isSubmittedFeedback == "0" && resturentata.status.equals("Delivered")){
            activityOrderInfoBinding.feedback.visibility =  View.VISIBLE
            activityOrderInfoBinding.feedback.setOnClickListener {
                if (resturentata.status.equals("Delivered")){
                    orderinforesonce.locationName = location.name
                    orderinforesonce.restService = orderHistory.restService
                    orderinforesonce.paymentType = orderHistory.paymentType
                    orderinforesonce.questionList = deliveryStatus.QuestionList
                    orderinforesonce.locationTel = location.tel
                    orderinforesonce.address = location.finalAddress
                    val intent = Intent(this@OrderInfoActivity, FeedbackActivity::class.java)
                    intent.putExtra(
                        ConstantCommand.DATA, GsonBuilder().setPrettyPrinting().create().toJson(
                            orderinforesonce
                        )
                    )
                    startActivity(intent)
                }
            }

        }else{
            activityOrderInfoBinding.feedback.visibility =  View.GONE

        }


        trackorder?.onResult(resturentata,deliveryStatus,orderHistory)
    }

    override fun onUpdateDeliveryStatus(delievryStatusData: DeliveryStatus) {
        deliveryStatus  = delievryStatusData
        delievryStatusData.let { deliveryStatusObject->


            delievryStatusData.QuestionList.forEach { questionList ->


                when(questionList.feedbackQuestion?.QuestionName.toString()){
                    "Friendly service"->{
                        activityOrderInfoBinding.friendlyServiceCount.text = questionList.feedbackQuestion?.DriverfeedbackCnt
                    }
                    "Perfect Handoff"->{
                        activityOrderInfoBinding.perfectHandOff.text = questionList.feedbackQuestion?.DriverfeedbackCnt
                    }
                    "Quick and Efficient"->{
                        activityOrderInfoBinding.quickEfficency.text = questionList.feedbackQuestion?.DriverfeedbackCnt
                    }
                    "Delivered with care"->{
                        activityOrderInfoBinding.deliveryWithCare.text = questionList.feedbackQuestion?.DriverfeedbackCnt
                    }
                }

            }


          if (deliveryStatusObject.driverProfilePic.isNullOrEmpty().not()){
                BuilderManager.LoadImageWithOutBaseUrl(
                    activityOrderInfoBinding.restroimage2,
                    deliveryStatusObject.driverProfilePic
                )
                activityOrderInfoBinding.restroimage2.visibility = View.VISIBLE
            }else{
                activityOrderInfoBinding.restroimage2.visibility = View.GONE
            }
            if (deliveryStatusObject.driverName.isNullOrEmpty()){
                activityOrderInfoBinding.driverBioLay.visibility = View.GONE
            }else{
                activityOrderInfoBinding.driverName.text = deliveryStatusObject.driverName
                activityOrderInfoBinding.driverBio.text = deliveryStatusObject.DriverBioInfo
                activityOrderInfoBinding.driverBioLay.visibility = View.VISIBLE
            }
        }
    }

    private fun getTrackingHistory(
        orderHistory: OrderHistory,
        orderInfoResponse: OrderInfoModel,
        deliveryStatus: DeliveryStatus?
    ) {
        dialog = BottomSheetDialog(this, R.style.SheetDialog)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)?.inDuration(400)
         val trackorder =  object : TrackOrderCallBack{
             override fun onResult(orderInfoResponse: OrderInfoModel, deliveryStatus: DeliveryStatus?) {

             }
             override fun onResult(
                orderInfoResponse: OrderInfoModel,
                deliveryStatus: DeliveryStatus?,
                orderHistory: OrderHistory
            ) {
                val viewTrackingOrderBinding : ViewTrackingOrderBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(
                        this@OrderInfoActivity
                    ), R.layout.view_tracking_order, null, false
                )
                dialog?.setContentView(viewTrackingOrderBinding.root)

                 if(orderInfoResponse.deliveryParcelImg.isEmpty()){
                     viewTrackingOrderBinding.preview.visibility = View.GONE

                 }else{
                     viewTrackingOrderBinding.preview.visibility = View.VISIBLE
                 }

                 viewTrackingOrderBinding.orderdetails.visibility = View.GONE
                viewTrackingOrderBinding.closse.setOnClickListener {
                    dialog?.dismiss()
                }
                 viewTrackingOrderBinding.preview.setOnClickListener {
                     previewParcelImage()
                }
                if (orderInfoResponse.quickMsgList.isNullOrEmpty()){
                    viewTrackingOrderBinding.messageList.visibility = View.GONE
                }else{
                    if ((orderInfoResponse.quickMsgList?:ArrayList()).isNotEmpty()){
                        for (i in 0 until (orderInfoResponse.quickMsgList?:ArrayList()).size ){
                                    (orderInfoResponse.quickMsgList?:ArrayList())[i].dateValue =
                                        BuilderManager.getDateFromStringSecond(
                                        orderInfoResponse.quickMsgList!![i].createdOn
                                    ) ?: Date()
                        }

                        orderInfoResponse.quickMsgList?.sortByDescending { it.dateValue}
                        viewTrackingOrderBinding.todayDate.text = BuilderManager.getProperDay(
                            BuilderManager.getDateFromStringSecond(
                                orderInfoResponse.quickMsgList!![0].createdOn
                            ) ?: Date()

                        )
                    }
                    viewTrackingOrderBinding.quickMessage.adapter= QuickMessageAdapter(this@OrderInfoActivity,orderInfoResponse.quickMsgList?:ArrayList())
                }
                changeStatus(orderHistory,orderInfoResponse,deliveryStatus,viewTrackingOrderBinding)
            }

        }
         this.trackorder= trackorder
        trackorder.onResult(orderInfoResponse, deliveryStatus,orderHistory)
        if (dialog!=null && dialog?.isShowing?.not() == true){
            dialog?.show()
        }
    }

   @SuppressLint("SetTextI18n")
   private fun changeStatus(orderHistory: OrderHistory,
                            orderInfoResponse: OrderInfoModel,
                            deliveryStatus: DeliveryStatus?, viewTrackingOrderBinding:ViewTrackingOrderBinding){
       var actualStatus = if (orderInfoResponse.isOrderConfirmed =="1" && orderInfoResponse.status != "Delivered"){
           "Confirmed"
       }else{
           orderInfoResponse.status?:"Open"
       }
       if (actualStatus =="Confirmed"){
           startTimer()
       }else{
           timer.cancel()
       }
       orderHistory.status= actualStatus
       viewTrackingOrderBinding.orderno.text = "Order#: ${orderHistory.orderNumber}"
       when(orderHistory.restService){
           "Pickup" -> {
               updatePickupStatusUI(viewTrackingOrderBinding)
               statusProgressPickUI("Open",actualStatus,viewTrackingOrderBinding)
           }
           "Curbside" -> {
               updatePickupStatusUI(viewTrackingOrderBinding)
               statusProgressPickUI("Open",actualStatus,viewTrackingOrderBinding)
           }
           "On-base Delivery" -> {
               deliveryStatus?.let {
                   if (actualStatus=="Confirmed" && deliveryStatus.isDriverEnroute == "1"){
                       actualStatus =  "Enroute"
                       orderHistory.status= actualStatus
                   }
                   updateDeliveryStatusUI(viewTrackingOrderBinding,actualStatus)
                   statusProgressDeliveryUI("Open", actualStatus, viewTrackingOrderBinding)
               }
           }
           "Off-base Delivery" -> {
               deliveryStatus?.let {
                   if (actualStatus=="Confirmed" && deliveryStatus.isDriverEnroute == "1"){
                       actualStatus =  "Enroute"
                       orderHistory.status= actualStatus
                   }
                   updateDeliveryStatusUI(viewTrackingOrderBinding, actualStatus)
                   statusProgressDeliveryUI("Open", actualStatus, viewTrackingOrderBinding)
               }
           }
       }
       deliveryStatus?.let { deliveryStatusObject->
           viewTrackingOrderBinding.delievryTitle.text = "Delivering to you :"
           viewTrackingOrderBinding.estimatetime.text= "ETA : ${deliveryStatusObject.EstimatedDeliveryTime} Minute"
           val deliveryAddress = StringBuilder()
           if (deliveryStatusObject.OrderDelAddr1.isNotEmpty()) {
               deliveryAddress.append("${deliveryStatusObject.OrderDelAddr1},")
           }
           if (deliveryStatusObject.OrderDelAddr2.isNotEmpty()) {
               deliveryAddress.append("${deliveryStatusObject.OrderDelAddr2},")
           }
           if (deliveryStatusObject.OrderDelCity.isNotEmpty()) {
               deliveryAddress.append(deliveryStatusObject.OrderDelCity)
           }
           viewTrackingOrderBinding.delievryAddress.text =deliveryAddress.toString()

       }
   }



    private fun updateCurbsideStatusUI(viewTrackingOrderBinding:ViewTrackingOrderBinding){
        viewTrackingOrderBinding.addressLay.visibility = View.GONE
        viewTrackingOrderBinding.delievryText.text = getString(R.string.delivered)
    }

    private fun updatePickupStatusUI(viewTrackingOrderBinding:ViewTrackingOrderBinding){
        viewTrackingOrderBinding.addressLay.visibility = View.GONE
        viewTrackingOrderBinding.delievryText.text = getString(R.string.picked_up)
        viewTrackingOrderBinding.driverView.visibility = View.GONE
        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE
    }
    private fun updateDeliveryStatusUI(
        viewTrackingOrderBinding: ViewTrackingOrderBinding,
        actualStatus: String
    ){
        if (actualStatus != "Delivered" ){
            viewTrackingOrderBinding.addressLay.visibility = View.VISIBLE
        }else{
            viewTrackingOrderBinding.addressLay.visibility = View.GONE
        }
        viewTrackingOrderBinding.delievryText.text = getString(R.string.delivered)
    }

    @SuppressLint("QueryPermissionsNeeded", "SetTextI18n")
    private fun getHelpUI(){
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)
        val viewHelpBinding : ViewHelpBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.view_help,
            null,
            false
        )
        dialog.setContentView(viewHelpBinding.root)
        viewHelpBinding.orderno.text = "Order#: ${orderHistory.orderNumber}"

        viewHelpBinding.emailrestuent.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${orderinforesonce.restEmailId}")
            }
            startActivity(Intent.createChooser(emailIntent, "Choose:"))
            dialog.dismiss()
        }

        viewHelpBinding.calltoreturent.setOnClickListener {
            BuilderManager.requestPhonecallPermission(this@OrderInfoActivity, object :
                CodeScanerInit {
                @SuppressLint("MissingPermission")
                override fun allowPermition() {
                    startActivity(
                        Intent(
                            Intent.ACTION_CALL,
                            Uri.parse("tel:" + orderinforesonce.restTelephone.trim())
                        )
                    )
                    dialog.dismiss()
                }

            })

        }
        viewHelpBinding.closse.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun getEReceiptUI() {
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)
        val viewEreceiptBinding : ViewEreceiptBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                this
            ), R.layout.view_ereceipt, null, false
        )
        dialog.setContentView(viewEreceiptBinding.root)
        viewEreceiptBinding.orderno.text = "Order#: ${orderHistory.orderNumber}"
        viewEreceiptBinding.emailfield.setText(orderInfoViewModel.getEmail())


        viewEreceiptBinding.submitbtn.setOnClickListener {

        }
        viewEreceiptBinding.closse.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setCartData() {
        val region =  LocList(
            (orderinforesonce.locationId ?: 0).toString(),
            orderinforesonce.locationName ?: "",
            orderinforesonce.URLName
        )
        val intent = Intent(this@OrderInfoActivity, ResturantActivity::class.java)
        intent.putExtra(ConstantCommand.DATA, region)
        intent.putExtra(
            ConstantCommand.DATA_2, GsonBuilder().setPrettyPrinting()
                .create().toJson(orderinforesonce)
        )
        startActivity(intent)
        onBackPressed()
        OrderHistoryActivity.orderHistoryActivity.finish()
    }


    private fun statusProgressDeliveryUI(status: String, actualStatus: String,viewTrackingOrderBinding:ViewTrackingOrderBinding){
        Handler(Looper.getMainLooper()).postDelayed({
            when (status) {
                "Open" -> {
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.we_recived_your_order)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val progress = if (actualStatus == "Delivered" || actualStatus =="Enroute"||
                            actualStatus == "Canceled" ||
                            actualStatus == "Confirmed") {10} else {5}
                        viewTrackingOrderBinding.placesView.setBackgroundResource(R.drawable.circle_mark_red)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.placeOrderProgress.setProgress(progress, true)
                        } else {
                            viewTrackingOrderBinding.placeOrderProgress.progress = progress
                        }
                        if (actualStatus == "Confirmed" || actualStatus == "Delivered" || actualStatus =="Enroute") {
                            statusProgressDeliveryUI("Confirmed", actualStatus,viewTrackingOrderBinding)
                        } else if (actualStatus == "Canceled") {
                            statusProgressDeliveryUI("Canceled", actualStatus,viewTrackingOrderBinding)
                        }},20)
                }
                "Canceled"->{
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_canceled)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.visibility = View.GONE
                        viewTrackingOrderBinding.prosessingText.visibility = View.GONE
                        viewTrackingOrderBinding.prosseingOrderProgress.visibility = View.GONE

                        viewTrackingOrderBinding.driverView.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE

                        viewTrackingOrderBinding.delievryText.text = getString(R.string.order_canceled_new_line)
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red_second)
                    }, 20)
                }
                "Delivered" -> {
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_has_been_delivered)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red)
                    }, 20)
                }
                "Confirmed"->{
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_has_been_confirmed)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.setBackgroundResource(R.drawable.circle_mark_red)
                        val progress = if (actualStatus == "Delivered" ||actualStatus == "Enroute") {10} else {5}
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.prosseingOrderProgress.setProgress(
                                progress,
                                true
                            )
                        } else {
                            viewTrackingOrderBinding.prosseingOrderProgress.progress = progress
                        }
                        if (actualStatus == "Delivered" || actualStatus == "Enroute") {
                            statusProgressDeliveryUI("Enroute", actualStatus,viewTrackingOrderBinding)
                        }
                    },20)
                }
                "Enroute" -> {
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.your_order_out_for_delivery)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.driverView.setBackgroundResource(R.drawable.circle_mark_red)
                        val progress = if (actualStatus == "Delivered") {
                            10
                        } else {
                            5
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.driverViewProgress.setProgress(progress, true)
                        } else {
                            viewTrackingOrderBinding.driverViewProgress.progress = progress
                        }
                        if (actualStatus == "Delivered") {
                            statusProgressDeliveryUI("Delivered", actualStatus,viewTrackingOrderBinding)
                        }
                    }, 20)
                }
            }
        },20)
    }


    private fun statusProgressPickUI(status: String, actualStatus: String,viewTrackingOrderBinding:ViewTrackingOrderBinding) {
        Handler(Looper.getMainLooper()).postDelayed({
            when (status) {
                "Open" -> {
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.we_recived_your_order)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val progress = if (actualStatus == "Delivered" ||
                            actualStatus == "Canceled" ||
                            actualStatus == "Confirmed") {10} else {5}
                        viewTrackingOrderBinding.placesView.setBackgroundResource(R.drawable.circle_mark_red)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.placeOrderProgress.setProgress(progress, true)
                        } else {
                            viewTrackingOrderBinding.placeOrderProgress.progress = progress
                        }
                        if (actualStatus == "Confirmed" || actualStatus == "Delivered") {
                            statusProgressPickUI("Confirmed", actualStatus,viewTrackingOrderBinding)
                        } else if (actualStatus == "Canceled") {
                            statusProgressPickUI("Canceled", actualStatus,viewTrackingOrderBinding)
                        }},20)
                }
                "Canceled"->{
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_canceled)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.visibility = View.GONE
                        viewTrackingOrderBinding.prosessingText.visibility = View.GONE
                        viewTrackingOrderBinding.prosseingOrderProgress.visibility = View.GONE

                        viewTrackingOrderBinding.driverView.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE

                        viewTrackingOrderBinding.delievryText.text = getString(R.string.order_canceled_new_line)
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red_second)
                    }, 20)
                }
                "Confirmed"->{
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_has_been_confirmed)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.setBackgroundResource(R.drawable.circle_mark_red)
                        val progress = if (actualStatus == "Delivered") {10} else {5}
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.prosseingOrderProgress.setProgress(
                                progress,
                                true
                            )
                        } else {
                            viewTrackingOrderBinding.prosseingOrderProgress.progress = progress
                        }
                        if (actualStatus == "Delivered") {
                            statusProgressPickUI("Delivered", actualStatus,viewTrackingOrderBinding)
                        }
                    },20)
                }
                "Delivered" -> {
                    viewTrackingOrderBinding.delievryStatus.text = getString(R.string.order_has_been_picked_up_by_you)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red)
                    }, 20)

                }
            }


        },20)
    }


    override fun onLocation(resturentata: RestrurentModel) {
        location = resturentata
    }

}

