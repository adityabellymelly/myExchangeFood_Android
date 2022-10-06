package com.exchange.user.module.cart_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.OrderDataModel
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.order_responce.OrderResponce
import com.exchange.user.module.cart_module.model.payment_model.CardRequest
import com.exchange.user.module.cart_module.model.payment_model.Data
import com.exchange.user.module.cart_module.model.payment_model.TempOrderData
import com.exchange.user.module.cart_module.model.payment_model.UniqueIdentifier
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.utility_module.dialog_commands.PaymentDialogCallBack
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class CartFragmentViewModel (
    applicationprefrence: ApplicationSharePrefrence, schedulerProvider: SchedulerProvider,
    applicationutility: ApplicationUtility,
    applicationcontext: Context,
    validations: Validations,
    applicationrequest: ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider,applicationutility,applicationcontext,validations,applicationrequest) {

    fun onReturentMenu(){
        (getNavigator() as CartFragmentNavigator).onReasturentMenu()
    }

    fun appliedCoupen(){(getNavigator() as CartFragmentNavigator).applyCoupon()}
    fun tip(tip : Int){(getNavigator() as CartFragmentNavigator).tip(tip)}
    fun cancelcoupen(){(getNavigator() as CartFragmentNavigator).cancelCoupon()}
    fun cancelDiscount(){(getNavigator() as CartFragmentNavigator).cancelDiscount()}

    fun addorChangeAddress(){(getNavigator() as CartFragmentNavigator).addOrChangeAddress()}
    fun submit(){(getNavigator() as CartFragmentNavigator).submit()}

    fun onScheduleTime(){ (getNavigator() as CartFragmentNavigator).onScheduleTime()}
    fun saveData(issave :Boolean) {
        if (issave){
            getApplicationSharePref().setCart(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.cartdata))
            getApplicationSharePref().setResturent(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.restrurent))

            getApplicationSharePref().setMenuData(GsonBuilder().setPrettyPrinting()
                .serializeNulls().create().toJson(CartDataInt.menudata))

        }else{
            getApplicationSharePref().setCart(null)
            getApplicationSharePref().setResturent(null)
            getApplicationSharePref().setValidCoupon(null)
            getApplicationSharePref().setMenuData(null)
        }
    }

    fun getCountryCode(): String ="US"
    fun getUserID(): String?  =  getApplicationSharePref().getUserID()


    fun loginByCustId(tId: String,custid:Long,restLocationId: LocList){
        if (tId.isEmpty()){
            getToken(restLocationId,custid)
        }else {
            if (getApplicationUtility().isInternetConnected()) {
                val token = TokenRequest()
                token.tId = tId
                val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
                data.custId = custid
                token.data = data
                val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
                getCompositeDisposable().add(
                    getApplicationRequest().DoLoginByCustId(PostData(request)).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce: ResponceModel ->
                            if (responce.serviceStatus.equals("S", true)) {
                                if (responce.data != null) {
                                    responce.data?.let {
                                        val profiledata: ProfileModel = Gson().fromJson(
                                            BuilderManager.decodeString(it),
                                            ProfileModel::class.java
                                        )
                                        CartDataInt.profiledata = profiledata
                                        CartDataInt.cartdata.custId = profiledata.id
                                        saveData(profiledata)
                                    }
                                } else {
                                    (getNavigator() as CartFragmentNavigator).showFeedbackMessage(
                                        "Something went wrong!"
                                    )
                                }
                            } else {
                                responce.message?.let {
                                    (getNavigator() as CartFragmentNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }
                        },
                            { throwable: Throwable? ->
                                throwable?.message?.let {
                                    (getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)
                                }
                            }

                        ))


            } else {
                (getNavigator() as CartFragmentNavigator).showFeedbackMessage(
                    getContext().getString(
                        R.string.warning_no_internet
                    )
                )
            }
        }
    }

    fun getToken(restLocationId: LocList, custid: Long) {
        if(getApplicationUtility().isInternetConnected()){
            val token =  TokenRequest()
            token.appCode=ConstantCommand.APP_TOKEN
            token.mobUrl =ConstantCommand.URl.plus("=").plus(restLocationId.urlName.substringAfter("="))   //"     ${}${restLocationId.urlName}"
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl=restLocationId.urlName
            data.traceId = ""
            token.data=data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            Log.e("Token request",GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getToken(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce:ResponceModel ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    val tokendata : TokenResponce = Gson().fromJson(BuilderManager.decodeString(it),TokenResponce::class.java)
                                    tokendata.tId?.let {
                                        CartDataInt.orderdata.tId=tokendata.tId
                                        CartDataInt.orderdata.chainId = tokendata.chainId
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
                                        loginByCustId(tokendata.tId?:"",custid,restLocationId)
                                    }
                                }
                            }else{
                                (getNavigator() as CartFragmentNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? -> throwable?.message?.let {
                            (getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)
                        } }

                    )
            )

        }
        else{ (getNavigator() as CartFragmentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }


    private fun saveData(it: ProfileModel) {
        if( it.email.isNullOrEmpty()){
            it.username?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }

        }else{
            it.email?.let {
                if (getValidation().isValidEmail(it)) {
                    getApplicationSharePref().setEmail(it)
                }
            }
        }
        val username = StringBuilder()
        it.fName?.let {
            username.append(it)
        }
        it.lName?.let {
            username.append(" $it")
        }

        it.tel?.let {
            getApplicationSharePref().setPhoneNumber(it)
        }
        it.id?.let {
            getApplicationSharePref().setUserID(it.toString())
        }
        getApplicationSharePref().setUserName(username.toString())
        getApplicationSharePref().setReward((it.loyaltyPoints ?: "0").toDouble())


        val profilegson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
        val jsonprofileString = profilegson.toJson(it)
        getApplicationSharePref().setProfile(jsonprofileString)

    }

    fun setOrder(orderdata: OrderDataModel, paymentdialogCallBack : PaymentDialogCallBack?){
        if (orderdata.data?.getmOrderData()?.timeSelection==null){
            (getNavigator() as CartFragmentNavigator).onScheduleTime()
        }else{
            if (getApplicationUtility().isInternetConnected()) {
                (getNavigator() as CartFragmentNavigator).startOrderProgress(true)
                val requestdata = BuilderManager.encodeString(GsonBuilder().serializeNulls().create().toJson(orderdata))
                Log.e("Orderdata - ",GsonBuilder().serializeNulls().create().toJson(orderdata))
                getCompositeDisposable().add(
                    getApplicationRequest().startOrder(PostData(requestdata)).doOnSuccess {  }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce->
                            if (responce.serviceStatus.equals("S",true)){
                                if (responce.data!=null){
                                    responce.data?.let {
                                        val orderresponce : OrderResponce = Gson().fromJson(BuilderManager.decodeString(it), OrderResponce::class.java)
                                        if (orderresponce.orderNumber != null) {
                                            paymentdialogCallBack?.onSucessPayment()
                                            (getNavigator() as CartFragmentNavigator).openConfirmationScreen()
                                        } else {
                                            val error = StringBuilder()
                                            for (i in orderresponce.errorInfo.indices) {
                                                error.append("${i + 1}. ${orderresponce.errorInfo[i].text}\n")
                                            }
                                            (getNavigator() as CartFragmentNavigator).showOrderError(error.toString())
                                            paymentdialogCallBack?.onErrorPayment()
                                        }
                                    }
                                }else{
                                    (getNavigator() as CartFragmentNavigator).showFeedbackMessage("Something went wrong!")
                                }
                            }
                            else{
                                if (responce.data!=null){
                                    responce.data?.let {
                                        val orderresponce : OrderResponce = Gson().fromJson(BuilderManager.decodeString(it), OrderResponce::class.java)
                                        val error = StringBuilder()
                                        for (i in orderresponce.errorInfo.indices) {
                                            error.append("${i + 1}. ${orderresponce.errorInfo[i].text}\n")
                                        }
                                        (getNavigator() as CartFragmentNavigator).showOrderError(error.toString())
                                        paymentdialogCallBack?.onErrorPayment()
                                    }

                                }
                                responce.message?.let {
                                    (getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)}
                            }
                            (getNavigator() as CartFragmentNavigator).startOrderProgress(false)
                        },
                            {
                                    throwable ->
                                (getNavigator() as CartFragmentNavigator).startOrderProgress(false)
                                throwable.message?.let {(getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)}
                            }
                        ))

            }else {
                (getNavigator() as CartFragmentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
            }
        }
    }

    fun setTempOrder(from : Int) {
        if (getApplicationUtility().isInternetConnected()) {
            val pattmrequest = CardRequest()
            pattmrequest.tId =  CartDataInt.orderdata.tId
            val datavalue = Data()
            datavalue.locationId =  CartDataInt.cartdata.locationId
            val temporderdata = TempOrderData()
            temporderdata.custEmail =  CartDataInt.profiledata.email
            temporderdata.custId =  CartDataInt.profiledata.id
            temporderdata.restTimeStamp =  CartDataInt.cartdata.createdOn
            temporderdata.orderJSON = GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson( CartDataInt.cartdata)
            datavalue.tempOrderData = temporderdata
            datavalue.aaFESPayData = Data.AAFESPayData("")
            pattmrequest.data = datavalue
            val requestdata = BuilderManager.encodeString(GsonBuilder().serializeNulls().create().toJson(pattmrequest))
            Log.e("Orderdata - ",requestdata)
            (getNavigator() as CartFragmentNavigator).startOrderProgress(true)
            getCompositeDisposable().add(
                getApplicationRequest().getUniqueIdentifier(PostData(requestdata)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce ->
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val orderresponce: UniqueIdentifier = Gson().fromJson(BuilderManager.decodeString(it),UniqueIdentifier::class.java)
                                    CartDataInt.cartdata.isAAFESPayGateway = "1"
                                    CartDataInt.cartdata.aafesPayTraceId = orderresponce.traceId
                                    val param  =  "\"".plus(ConstantCommand.KEY_AMOUNT).plus(BuilderManager.roundToDigit(CartDataInt.cartdata.totalAmt.toFloat(),2))
                                        .plus(ConstantCommand.KEY_MENUID).plus(CartDataInt.cartdata.menuId)
                                        .plus(ConstantCommand.KEY_LOCATION).plus(CartDataInt.cartdata.locationId)
                                        .plus(ConstantCommand.KEY_MIL_STAR).plus(if (from==ConstantCommand.FROM_MILTRY_STAR)"true" else "false")
                                        .plus(ConstantCommand.KEY_TID).plus(CartDataInt.orderdata.tId)
                                        .plus(ConstantCommand.KEY_TRACEID).plus(orderresponce.traceId).plus("\"")

                                     val link =  if(CartDataInt.restrurent.urlname.equals(ConstantCommand.urlName2)||
                                         CartDataInt.restrurent.urlname.equals(ConstantCommand.urlName)||
                                         CartDataInt.restrurent.urlname.equals(ConstantCommand.urlName3)){
                                         ConstantCommand.PATMENT_GATWAY2
                                        }
                                        else{
                                         ConstantCommand.PATMENT_GATWAY
                                        }

                                      val openLink =  link.plus(ConstantCommand.KEY_DATA)
                                        .plus(BuilderManager.encodeString(param))

                                        Log.e("Link  - ",openLink)
                                       (getNavigator() as CartFragmentNavigator).openPayment(openLink)
                                }
                            } else {
                                (getNavigator() as CartFragmentNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        } else {
                            responce.message?.let {
                                (getNavigator() as CartFragmentNavigator).showFeedbackMessage(it)
                            }
                        }
                        (getNavigator() as CartFragmentNavigator).startOrderProgress(false)
                    },
                        { throwable ->
                            throwable.message?.let {
                                (getNavigator() as CartFragmentNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                            (getNavigator() as CartFragmentNavigator).startOrderProgress(false)
                        }
                    ))

        }else {
            (getNavigator() as CartFragmentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }




    }
}