package com.exchange.user.module.order_history.order_history_fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityOrderHistoryBinding
import com.exchange.user.databinding.ViewTrackingOrderBinding
import com.exchange.user.module.base_module.BaseFragment
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.order_history.OrderHistoryNavigator
import com.exchange.user.module.order_history.OrderHistoryViewModel
import com.exchange.user.module.order_history.adapter.OrderHistoryListAdapter
import com.exchange.user.module.order_info_module.OrderInfoActivity
import com.exchange.user.module.order_info_module.adapter.QuickMessageAdapter
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.profile_module.model.OrderHistory
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.signin_module.SignInActivity
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.CallApi
import com.exchange.user.module.utility_module.dialog_commands.TrackOrderCallBack
import com.google.gson.GsonBuilder
import com.rey.material.app.BottomSheetDialog
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class OrderHistoryFragment : BaseFragment<ActivityOrderHistoryBinding, OrderHistoryViewModel>(),
    OrderHistoryNavigator,CallApi{
    @Inject
    lateinit var factory : ViewModelProviderFactory
    @Inject
    lateinit var orderHistoryAdapter : OrderHistoryListAdapter
    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var activityOrderHistoryBinding : ActivityOrderHistoryBinding
    override val bindingVariable: Int get() = BR.orderHistoryViewModel

    override val layoutId: Int get() = R.layout.activity_order_history

    override val viewModel: OrderHistoryViewModel
        get() {
            orderHistoryViewModel = ViewModelProvider(this, factory).get(OrderHistoryViewModel::class.java)
            return orderHistoryViewModel
        }

    private val incommingNotification: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConstantCommand.INCOMING_NOTIFICATION) {
                orderHistoryViewModel.getOrderHistory()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(getContaxt(), R.color.white_color)
        orderHistoryViewModel.setNavigator(this)
        registerBroadcast()
    }

    private fun registerBroadcast() {
        val filter = IntentFilter()
        filter.addAction(ConstantCommand.INCOMING_NOTIFICATION)
         getContaxt().registerReceiver(incommingNotification, IntentFilter(filter))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityOrderHistoryBinding = viewDataBinding
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        getContaxt().unregisterReceiver(incommingNotification)
    }


    override fun initData() {
        if (orderHistoryViewModel.getUserID()!=null){
            orderHistoryViewModel.getOrderHistory()
            updateHistory(CartDataInt.profiledata)
            activityOrderHistoryBinding.data.visibility = View.GONE
            activityOrderHistoryBinding.view.visibility = View.VISIBLE
            activityOrderHistoryBinding.recycleOrder.adapter = orderHistoryAdapter
            orderHistoryViewModel.setNavigator(this)
        }else{
            activityOrderHistoryBinding.data.visibility = View.VISIBLE
            activityOrderHistoryBinding.view.visibility = View.GONE
        }
    }

    override fun openSiginActivity() {
        startActivity(Intent(getContaxt(), SignInActivity::class.java))
    }

    override fun onBack() {

    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    override fun updateHistory(profiledata: ProfileModel) {
        if (profiledata.orderHistory.isNullOrEmpty().not()) {
            activityOrderHistoryBinding.message.visibility = View.GONE
            activityOrderHistoryBinding.recycleOrder.visibility = View.VISIBLE
            orderHistoryAdapter.addItems(profiledata.orderHistory)
            orderHistoryAdapter.setApiCallBack(this)
            CartDataInt.profiledata.orderHistory = profiledata.orderHistory
            orderHistoryViewModel.saveProfile(CartDataInt.profiledata)
        } else {
            activityOrderHistoryBinding.message.visibility = View.VISIBLE
            activityOrderHistoryBinding.recycleOrder.visibility = View.GONE
        }
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(activityOrderHistoryBinding.root, message)
    }

    override fun callFunction(orderHistory: OrderHistory, trackOrderCallBack: TrackOrderCallBack) {
        when(orderHistory.restService){
            "Pickup" -> {
                orderHistoryViewModel.getTokenDemo(orderHistory.orderId, trackOrderCallBack, null)
            }
            "Curbside" -> {
                orderHistoryViewModel.getOrderDeliveryStatusForCustomer(
                    orderHistory.orderId, trackOrderCallBack
                )
            }
            "On-base Delivery" -> {
                orderHistoryViewModel.getOrderDeliveryStatusForCustomer(
                    orderHistory.orderId, trackOrderCallBack
                )
            }
            "Off-base Delivery" -> {
                orderHistoryViewModel.getOrderDeliveryStatusForCustomer(
                    orderHistory.orderId, trackOrderCallBack
                )
            }
        }
    }

    override fun trackHistory(
        orderHistory: OrderHistory,
        orderInfoResponse: OrderInfoModel,
        deliveryStatus: DeliveryStatus?
    ) {
        getTrackingHistory(orderHistory, orderInfoResponse, deliveryStatus)
    }

    private fun getTrackingHistory(
        orderHistory: OrderHistory,
        orderInfoResponse: OrderInfoModel,
        deliveryStatus: DeliveryStatus?
    ) {

        val dialog = BottomSheetDialog(getContaxt(), R.style.SheetDialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)
        val viewTrackingOrderBinding : ViewTrackingOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                getContaxt()
            ), R.layout.view_tracking_order, null, false
        )
        dialog.setContentView(viewTrackingOrderBinding.root)
        viewTrackingOrderBinding.orderdetails.visibility = View.VISIBLE
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

                orderInfoResponse.quickMsgList?.sortByDescending { it.dateValue }

                viewTrackingOrderBinding.todayDate.text = BuilderManager.getProperDay(
                    BuilderManager.getDateFromStringSecond(
                        orderInfoResponse.quickMsgList!![0].createdOn
                    ) ?: Date()

                )
            }
            viewTrackingOrderBinding.quickMessage.adapter= QuickMessageAdapter(
                getContaxt(),
                orderInfoResponse.quickMsgList ?: ArrayList()
            )
        }
        changeStatus(orderHistory, orderInfoResponse, deliveryStatus, viewTrackingOrderBinding)
        viewTrackingOrderBinding.closse.setOnClickListener {
            dialog.dismiss()
        }
        viewTrackingOrderBinding.orderdetails.setOnClickListener {
            val intent = Intent(getContaxt(), OrderInfoActivity::class.java)
            intent.putExtra(ConstantCommand.DATA, GsonBuilder().setPrettyPrinting()
                .create().toJson(orderHistory))
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()


    }

    @SuppressLint("SetTextI18n")
    private fun changeStatus(
        orderHistory: OrderHistory,
        orderInfoResponse: OrderInfoModel,
        deliveryStatus: DeliveryStatus?, viewTrackingOrderBinding: ViewTrackingOrderBinding
    ){
        var actualStatus = if (orderInfoResponse.isOrderConfirmed =="1" && orderInfoResponse.status != "Delivered"){
            "Confirmed"
        }else{
            orderInfoResponse.status?:"Open"
        }
        orderHistory.status= actualStatus


        viewTrackingOrderBinding.orderno.text = "Order#: ${orderHistory.orderNumber}"
        when(orderHistory.restService){
            "Pickup" -> {
                updatePickupStatusUI(viewTrackingOrderBinding)
                statusProgressPickUI("Open", actualStatus, viewTrackingOrderBinding)
            }
            "Curbside" -> {
                updatePickupStatusUI(viewTrackingOrderBinding)
                statusProgressPickUI("Open",actualStatus,viewTrackingOrderBinding)
            }
            "On-base Delivery" -> {
                deliveryStatus?.let {

                    if (actualStatus == "Confirmed" && deliveryStatus.isDriverEnroute == "1") {
                        actualStatus = "Enroute"
                        orderHistory.status = actualStatus
                    }

                    updateDeliveryStatusUI(viewTrackingOrderBinding)
                    statusProgressDeliveryUI("Open", actualStatus, viewTrackingOrderBinding)
                }
            }
            "Off-base Delivery" -> {
                deliveryStatus?.let {

                    if (actualStatus == "Confirmed" && deliveryStatus.isDriverEnroute == "1") {
                        actualStatus = "Enroute"
                        orderHistory.status = actualStatus
                    }

                    updateDeliveryStatusUI(viewTrackingOrderBinding)
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


    private fun updatePickupStatusUI(viewTrackingOrderBinding: ViewTrackingOrderBinding){
        viewTrackingOrderBinding.addressLay.visibility = View.GONE
        viewTrackingOrderBinding.delievryText.text = getString(R.string.picked_up)
        viewTrackingOrderBinding.driverView.visibility = View.GONE
        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE
    }
    private fun updateDeliveryStatusUI(viewTrackingOrderBinding: ViewTrackingOrderBinding){
        viewTrackingOrderBinding.addressLay.visibility = View.VISIBLE
        viewTrackingOrderBinding.delievryText.text = getString(R.string.delivered)
    }
    private fun statusProgressDeliveryUI(
        status: String,
        actualStatus: String,
        viewTrackingOrderBinding: ViewTrackingOrderBinding
    ){
        Handler(Looper.getMainLooper()).postDelayed({
            when (status) {
                "Open" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.we_recived_your_order)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val progress =
                            if (actualStatus == "Delivered" || actualStatus == "Enroute" ||
                                actualStatus == "Canceled" ||
                                actualStatus == "Confirmed"
                            ) {
                                10
                            } else {
                                5
                            }
                        viewTrackingOrderBinding.placesView.setBackgroundResource(R.drawable.circle_mark_red)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.placeOrderProgress.setProgress(progress, true)
                        } else {
                            viewTrackingOrderBinding.placeOrderProgress.progress = progress
                        }
                        if (actualStatus == "Confirmed" || actualStatus == "Delivered" || actualStatus == "Enroute") {
                            statusProgressDeliveryUI(
                                "Confirmed",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        } else if (actualStatus == "Canceled") {
                            statusProgressDeliveryUI(
                                "Canceled",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        }
                    }, 200)
                }
                "Canceled" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_canceled)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.visibility = View.GONE
                        viewTrackingOrderBinding.prosessingText.visibility = View.GONE
                        viewTrackingOrderBinding.prosseingOrderProgress.visibility = View.GONE

                        viewTrackingOrderBinding.driverView.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE

                        viewTrackingOrderBinding.delievryText.text =
                            getString(R.string.order_canceled_new_line)
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red_second)
                    }, 300)
                }
                "Delivered" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_has_been_delivered)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red)
                    }, 300)
                }
                "Confirmed" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_has_been_confirmed)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.setBackgroundResource(R.drawable.circle_mark_red)
                        val progress =
                            if (actualStatus == "Delivered" || actualStatus == "Enroute") {
                                10
                            } else {
                                5
                            }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.prosseingOrderProgress.setProgress(
                                progress,
                                true
                            )
                        } else {
                            viewTrackingOrderBinding.prosseingOrderProgress.progress = progress
                        }
                        if (actualStatus == "Delivered" || actualStatus == "Enroute") {
                            statusProgressDeliveryUI(
                                "Enroute",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        }
                    }, 300)
                }
                "Enroute" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.your_order_out_for_delivery)
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
                            statusProgressDeliveryUI(
                                "Delivered",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        }
                    }, 300)
                }
            }
        }, 300)
    }


    private fun statusProgressPickUI(
        status: String,
        actualStatus: String,
        viewTrackingOrderBinding: ViewTrackingOrderBinding
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            when (status) {
                "Open" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.we_recived_your_order)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val progress = if (actualStatus == "Delivered" ||
                            actualStatus == "Canceled" ||
                            actualStatus == "Confirmed"
                        ) {
                            10
                        } else {
                            5
                        }
                        viewTrackingOrderBinding.placesView.setBackgroundResource(R.drawable.circle_mark_red)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.placeOrderProgress.setProgress(progress, true)
                        } else {
                            viewTrackingOrderBinding.placeOrderProgress.progress = progress
                        }
                        if (actualStatus == "Confirmed" || actualStatus == "Delivered") {
                            statusProgressPickUI(
                                "Confirmed",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        } else if (actualStatus == "Canceled") {
                            statusProgressPickUI("Canceled", actualStatus, viewTrackingOrderBinding)
                        }
                    }, 200)
                }
                "Canceled" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_canceled)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.visibility = View.GONE
                        viewTrackingOrderBinding.prosessingText.visibility = View.GONE
                        viewTrackingOrderBinding.prosseingOrderProgress.visibility = View.GONE

                        viewTrackingOrderBinding.driverView.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewText.visibility = View.GONE
                        viewTrackingOrderBinding.driverViewProgress.visibility = View.GONE

                        viewTrackingOrderBinding.delievryText.text =
                            getString(R.string.order_canceled_new_line)
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red_second)
                    }, 300)
                }
                "Confirmed" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_has_been_confirmed)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.prosseingView.setBackgroundResource(R.drawable.circle_mark_red)
                        val progress = if (actualStatus == "Delivered") {
                            10
                        } else {
                            5
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            viewTrackingOrderBinding.prosseingOrderProgress.setProgress(
                                progress,
                                true
                            )
                        } else {
                            viewTrackingOrderBinding.prosseingOrderProgress.progress = progress
                        }
                        if (actualStatus == "Delivered") {
                            statusProgressPickUI(
                                "Delivered",
                                actualStatus,
                                viewTrackingOrderBinding
                            )
                        }
                    }, 300)
                }
                "Delivered" -> {
                    viewTrackingOrderBinding.delievryStatus.text =
                        getString(R.string.order_has_been_picked_up_by_you)
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewTrackingOrderBinding.deliveryView.setBackgroundResource(R.drawable.circle_mark_red)
                    }, 300)

                }
            }


        }, 300)
    }

}