package com.exchange.user.module.order_history

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_model.ResponceModelSecond
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.feedback_module.model.OrderInfoData
import com.exchange.user.module.feedback_module.model.OrderInfoRequest
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.utility_module.dialog_commands.TrackOrderCallBack
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class OrderHistoryViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                            applicationutility : ApplicationUtility,
                            applicationcontext : Context,
                            validations  : Validations,
                            applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest) {


    fun onBack(){
        (getNavigator() as OrderHistoryNavigator).onBack()
    }

    fun  goToSignIn(){
        (getNavigator() as OrderHistoryNavigator).openSiginActivity()
    }

    fun getUserID(): String?=getApplicationSharePref().getUserID()


    fun getOrderHistory(){
        val request = SignInRequest()
        val dataValue = SignInRequest.Data()
        dataValue.custId = getApplicationSharePref().getUserID()?.toLong()
        request.data  = dataValue
        if (CartDataInt.orderdata.tId.isNullOrEmpty()){
            getToken(request)
        }else{
            request.tId = CartDataInt.orderdata.tId?:""
            orderHistory(request)
        }
    }
    private fun getToken(signInRequest: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            showProgress()
            val token = TokenRequest()
            token.appCode = ConstantCommand.APP_TOKEN
            token.mobUrl = ConstantCommand.URl.plus("=").plus(ConstantCommand.urlName)
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = ConstantCommand.urlName
            data.traceId = ""
            token.data = data
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(BuilderManager.encodeString(GsonBuilder().create().toJson(token)))).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        hideProgress()
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let { it ->
                                    val tokendata: TokenResponce = Gson().fromJson(
                                        BuilderManager.decodeString(it),
                                        TokenResponce::class.java
                                    )
                                    tokendata.tId?.let {
                                        signInRequest.tId = it
                                        CartDataInt.orderdata.tId =it
                                        orderHistory(signInRequest)
                                    }
                                }
                            } else {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                            }
                        }

                    )
            )

        } else {
            (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    private fun orderHistory(request: SignInRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            getCompositeDisposable().add(
                getApplicationRequest().getCustOrderHistory(PostData( BuilderManager.encodeString(GsonBuilder().create().toJson(request)))).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ response ->
                        if (response.serviceStatus.equals("S", true)) {
                            if (response.data != null) {
                                response.data?.let {
                                    val profileData : ProfileModel = Gson().fromJson(BuilderManager.decodeString(it),ProfileModel::class.java)
                                    (getNavigator() as OrderHistoryNavigator).updateHistory(profileData)
                                }
                            } else {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            response.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    fun getOrderDeliveryStatusForCustomer(orderId: Long?,trackOrderCallBack: TrackOrderCallBack){
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
                                    getTokenDemo(orderId,trackOrderCallBack,deliveryStatus)
                                }
                            }else{
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? ->

                            throwable?.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)} }

                    ))


        }else{ (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }

    fun getTokenDemo(orderId: Long?, trackOrderCallBack: TrackOrderCallBack,deliveryStatus: DeliveryStatus?) {
        if(CartDataInt.orderdata.tId.isNullOrEmpty().not()){
            getOrderDetail(orderId,CartDataInt.orderdata.tId?:"",trackOrderCallBack,deliveryStatus)
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
                                            getOrderDetail(orderId,it,trackOrderCallBack,deliveryStatus)

                                        }
                                    }
                                }else{
                                    (getNavigator() as OrderHistoryNavigator).showFeedbackMessage("Something went wrong!")
                                }
                            }else{
                                response.message?.let {
                                    (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)}
                            }

                        },
                            { throwable : Throwable? ->
                                throwable?.message?.let {
                                    (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                                } }))
            }
            else{ (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
        }
    }
    private fun getOrderDetail(orderId: Long?, tId: String,trackOrderCallBack:TrackOrderCallBack,deliveryStatus: DeliveryStatus?) {
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
                                    val orderInfoModel: OrderInfoModel =
                                        Gson().fromJson(BuilderManager.decodeString(it), OrderInfoModel::class.java)
                                    trackOrderCallBack.onResult(orderInfoModel,deliveryStatus)

                                }
                            } else {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        {
                                throwable: Throwable? ->
                            throwable?.message?.let {
                                (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(it)
                            }
                        }))
        }
        else {
            (getNavigator() as OrderHistoryNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }

    fun saveProfile(profileData: ProfileModel) {
        getApplicationSharePref().setProfile(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(profileData))
    }


}