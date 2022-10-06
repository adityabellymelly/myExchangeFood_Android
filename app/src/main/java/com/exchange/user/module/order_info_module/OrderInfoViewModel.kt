package com.exchange.user.module.order_info_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_model.ResponceModelSecond
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.feedback_module.model.OrderInfoData
import com.exchange.user.module.feedback_module.model.OrderInfoRequest
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.utility_module.dialog_commands.TrackOrderCallBack
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class OrderInfoViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                          applicationutility : ApplicationUtility,
                          applicationcontext : Context,
                          validations  : Validations,
                          applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) {


    fun  previewParcelImage(){
        (getNavigator() as OrderInfoNavigator).previewParcelImage()
    }

    fun getHelp(){
        (getNavigator() as OrderInfoNavigator).getHelp()
    }

    fun getEReceipt(){
        (getNavigator() as OrderInfoNavigator).getEReceipt()
    }

    fun trackingHistory(){
        (getNavigator() as OrderInfoNavigator).trackingHistory()
    }

    fun onBack(){
        (getNavigator() as OrderInfoNavigator).onBack()
    }
    fun reOrder(){
        (getNavigator() as OrderInfoNavigator).reOrder()
    }

    fun callToDriver(){
        (getNavigator() as OrderInfoNavigator).callToDriver()
    }

    fun getEmail(): String?  = getApplicationSharePref().getEmail()


    fun getRepetTokenDemo(orderId: Long?) {
        if(CartDataInt.orderdata.tId.isNullOrEmpty().not()){
            getRepetOrderDetail(orderId,CartDataInt.orderdata.tId?:"")
        }
        else{
            if(getApplicationUtility().isInternetConnected()){
                val token =  TokenRequest()
                token.appCode= ConstantCommand.APP_TOKEN
                token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
                val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
                data.mobUrl=  ConstantCommand.URl
                data.traceId = ""
                token.data=data
                val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
                getCompositeDisposable().add(
                    getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({response: ResponceModel ->
                            if (response.serviceStatus.equals("S",true)){
                                if (response.data!=null){
                                    response.data?.let {
                                        val tokendata : TokenResponce = Gson().fromJson(
                                            BuilderManager.decodeString(it),
                                            TokenResponce::class.java)
                                        tokendata.tId?.let {
                                            CartDataInt.orderdata.tId=tokendata.tId
                                            CartDataInt.orderdata.chainId = tokendata.chainId
                                            getRepetOrderDetail(orderId,it)

                                        }
                                    }
                                }else{
                                    (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                                }
                            }else{
                                response.message?.let {
                                    (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)}
                            }

                        },
                            { throwable : Throwable? ->
                                throwable?.message?.let {
                                    (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                                } }))
            }
            else{ (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
        }
    }


    fun getTokenDemo(orderId: Long?) {
        if(CartDataInt.orderdata.tId.isNullOrEmpty().not()){
            getOrderDetail(orderId,CartDataInt.orderdata.tId?:"")
        }
        else{
            if(getApplicationUtility().isInternetConnected()){
            val token =  TokenRequest()
            token.appCode= ConstantCommand.APP_TOKEN
            token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl=  ConstantCommand.URl
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({response: ResponceModel ->
                        if (response.serviceStatus.equals("S",true)){
                            if (response.data!=null){
                                response.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        getOrderDetail(orderId,it)

                                    }
                                }
                            }else{
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            response.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)}
                        }

                    },
                        { throwable : Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            } }))
            }
            else{ (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
        }
    }

    private fun getRepetOrderDetail(orderId: Long?, tId: String) {
        if (getApplicationUtility().isInternetConnected()) {
            val orderrequest = OrderInfoRequest()
            orderrequest.tId = tId
            val datavalue = OrderInfoData()
            datavalue.orderId = orderId
            orderrequest.data = datavalue
            val request = BuilderManager.encodeString(GsonBuilder().setPrettyPrinting().create().toJson(orderrequest))
            getCompositeDisposable().add(
                getApplicationRequest().getOrderInfo(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val resturentata: OrderInfoModel =
                                        Gson().fromJson(BuilderManager.decodeString(it), OrderInfoModel::class.java)
                                    val url = if (resturentata.URLName.isEmpty())
                                        resturentata.locationId ?: "0" else resturentata.URLName
                                    (getNavigator() as OrderInfoNavigator).onOrderInfoTimer(resturentata)
                                }
                            } else {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        {
                                throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            }
                        }))
        }
        else {
            (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }

    private fun getOrderDetail(orderId: Long?, tId: String) {
        if (getApplicationUtility().isInternetConnected()) {
            val orderrequest = OrderInfoRequest()
            orderrequest.tId = tId
            val datavalue = OrderInfoData()
            datavalue.orderId = orderId
            orderrequest.data = datavalue
            val request = BuilderManager.encodeString(GsonBuilder().setPrettyPrinting().create().toJson(orderrequest))
            getCompositeDisposable().add(
                getApplicationRequest().getOrderInfo(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val resturentata: OrderInfoModel =
                                        Gson().fromJson(BuilderManager.decodeString(it), OrderInfoModel::class.java)
                                    val url = if (resturentata.URLName.isEmpty())
                                        resturentata.locationId ?: "0" else resturentata.URLName
                                    (getNavigator() as OrderInfoNavigator).onOrderInfo(resturentata)
                                    getToken(resturentata.locationId?:"0",url,orderId)
                                }
                            } else {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                    {
                            throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            }
                    }))
        }
        else {
            (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }

    fun getToken(restLocationId: String, mobileUrl: String, orderId: Long?) {
        if(getApplicationUtility().isInternetConnected()){
            val token =  TokenRequest()
            token.appCode= ConstantCommand.APP_TOKEN
            token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl= if (mobileUrl.isEmpty().not()){mobileUrl}else{restLocationId}
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            Log.e("Token request", GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
                                        getLocation(it,mobileUrl,tokendata.locationId,orderId)
                                        if (CartDataInt.cartdata.locationId!=null){
                                            if (CartDataInt.cartdata.locationId != (tokendata.locationId?:0).toLong()){
                                                CartDataInt.cartdata =  CartData()
                                                CartDataInt.suggestion = Suggestion()
                                            }
                                        }
                                        else{
                                            CartDataInt.cartdata =  CartData()
                                            CartDataInt.suggestion = Suggestion()
                                        }

                                    }
                                }
                            }else{
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)}
                        }

                    },
                        { throwable : Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            } }

                    )
            )

        }
        else{ (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }

    private fun getLocation(tId: String, mobileUrl: String, locationId: Long?, orderId: Long?) {
        if (getApplicationUtility().isInternetConnected()) {
            val token = TokenRequest()
            token.tId = tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = mobileUrl
            data.locationId = locationId
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getLocationDetail(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val resturentata: RestrurentModel = Gson().fromJson(
                                        BuilderManager.decodeString(it), RestrurentModel::class.java)
                                    resturentata.urlname = mobileUrl
                                    (getNavigator() as OrderInfoNavigator).onLocation(resturentata)
                                }
                            } else {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }


    fun getOrderDeliveryStatusForCustomer(orderId: Long?){
        if(getApplicationUtility().isInternetConnected()) {
            val order =  OrderInfoRequest()
            order.tId = "463b7b6b-7c26-443f-9bda-67cb2a85980a"
            order.OrderId  = orderId
            order.dt = System.currentTimeMillis()
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(order))
            getCompositeDisposable().add(
                getApplicationRequest().getOrderDeliveryStatusForCustomer(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce: ResponceModelSecond ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val deliveryStatus : DeliveryStatus = GsonBuilder().setPrettyPrinting().create().fromJson(BuilderManager.decodeString(it),
                                        DeliveryStatus::class.java)
                                       getTokenDemo(orderId)
                                    (getNavigator() as OrderInfoNavigator).onUpdateDeliveryStatus(deliveryStatus)
                                }
                            }else{
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? ->

                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)} }

                    ))


        }else{ (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }

    fun getRepetOrderDeliveryStatusForCustomer(orderId: Long?){
        if(getApplicationUtility().isInternetConnected()) {
            val order =  OrderInfoRequest()
            order.tId = "463b7b6b-7c26-443f-9bda-67cb2a85980a"
            order.OrderId  = orderId
            order.dt = System.currentTimeMillis()
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(order))
            getCompositeDisposable().add(
                getApplicationRequest().getOrderDeliveryStatusForCustomer(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce: ResponceModelSecond ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val deliveryStatus : DeliveryStatus = GsonBuilder().setPrettyPrinting().create().fromJson(BuilderManager.decodeString(it),
                                        DeliveryStatus::class.java)
                                       getRepetTokenDemo(orderId)
                                    (getNavigator() as OrderInfoNavigator).onUpdateDeliveryStatusTimer(deliveryStatus)
                                }
                            }else{
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? ->

                            throwable?.message?.let {
                                (getNavigator() as OrderInfoNavigator).showFeedbackMessage(it)} }

                    ))


        }else{ (getNavigator() as OrderInfoNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }


}