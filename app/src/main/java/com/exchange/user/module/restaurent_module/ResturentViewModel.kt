package com.exchange.user.module.restaurent_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.*
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.order_info_module.model.AddOnOptionModifier2
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.order_info_module.model.OrderItemsAddOnList
import com.exchange.user.module.profile_module.model.ProfileModel
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Menus
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class ResturentViewModel (applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                          applicationutility : ApplicationUtility,
                          applicationcontext : Context,
                          validations  : Validations,
                          applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    var more:Boolean=false
    fun closeSearchView(){ (getNavigator() as ResturentNavigator).onClosedSearch() }
    fun openSearchView(){(getNavigator() as ResturentNavigator).onOpenSearch()}
    fun back(){(getNavigator() as ResturentNavigator).onBack()}

    fun GoToCart(){
        (getNavigator() as ResturentNavigator).moveToCart()
    }
    fun shareRestaurant(){}

    fun moreAbout(){
        (getNavigator() as ResturentNavigator).moreAbout(!more)
        more = !more
    }

    fun viewDiscount(){
        (getNavigator() as ResturentNavigator).viewDiscount()
    }

    fun getToken(restLocationId: LocList) {
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
                                        getLocation(it,restLocationId,tokendata.locationId)
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
                                (getNavigator() as ResturentNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(it)}
                        }
                    },
                    { throwable : Throwable? -> throwable?.message?.let {
                        (getNavigator() as ResturentNavigator).showFeedbackMessage(it)
                    } }

                    )
            )

        }
        else{ (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }

    private fun getLocation(tId: String, restLocationId: LocList, locationId: Long?) {
        if (getApplicationUtility().isInternetConnected()) {
            (getNavigator() as ResturentNavigator).showProgress(true)
            val token = TokenRequest()
            token.tId = tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = restLocationId.urlName
            data.locationId = locationId
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getLocationDetail(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        (getNavigator() as ResturentNavigator).showProgress(false)
                        if (responce.serviceStatus.equals("S", true)) {
                            if (responce.data != null) {
                                responce.data?.let {
                                    val resturentata: RestrurentModel = Gson().fromJson(BuilderManager.decodeString(it), RestrurentModel::class.java)
                                    (getNavigator() as ResturentNavigator).onLocation(resturentata)
                                    CartDataInt.restrurent = resturentata
                                    CartDataInt.restrurent.urlname = restLocationId.urlName
                                    CartDataInt.cartdata.placeOrder = "T"
                                    CartDataInt.cartdata.isPrinterMsgOk = "0"
                                    CartDataInt.cartdata.ccInfo = CCInfo()
                                    CartDataInt.cartdata.locationId = (resturentata.id?:0).toLong()
                                    CartDataInt.cartdata.restChainId = resturentata.restChainId
                                    if (resturentata.menus.isNullOrEmpty().not()) {
                                        resturentata.menus?.let { menu ->
                                            getMenus(tId, restLocationId, menu[0],locationId)
                                        }
                                    }
                                }
                            } else {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }
                        else {
                            responce.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(it)
                            }
                        }
                    },
                        { throwable: Throwable? ->
                            (getNavigator() as ResturentNavigator).showProgress(false)
                            throwable?.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }

                    ))


        } else {
            (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }

    }

    private fun getMenus(tId: String, restLocationId: LocList, menu: Menus, locationId: Long?){
        if(getApplicationUtility().isInternetConnected()) {
            (getNavigator() as ResturentNavigator).showProgress(true)
            val token = TokenRequest()
            token.tId = tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.mobUrl = restLocationId.urlName
            data.locationId = locationId
            data.menuId = (menu.id?:"0").toLong()
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().getMenu(PostData(request)).doOnSuccess {  }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({responce:ResponceModel ->
                        if (responce.serviceStatus.equals("S",true)){
                            if (responce.data!=null){
                                responce.data?.let {
                                    (getNavigator() as ResturentNavigator).showProgress(false)
                                    val menudata : MenuModel = GsonBuilder().setPrettyPrinting().create().fromJson(BuilderManager.decodeString(it),MenuModel::class.java)
                                    (getNavigator() as ResturentNavigator).onUpdateMenu(menudata)
                                    CartDataInt.cartdata.menuId = menudata.menuId
                                    CartDataInt.cartdata.status = 1
                                }
                            }else{
                                (getNavigator() as ResturentNavigator).showProgress(false)
                                (getNavigator() as ResturentNavigator).showFeedbackMessage("Something went wrong!")
                            }
                        }else{
                            responce.message?.let {
                                (getNavigator() as ResturentNavigator).showProgress(true)
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(it)}
                        }
                    },
                    { throwable : Throwable? ->
                        (getNavigator() as ResturentNavigator).showProgress(true)

                        throwable?.message?.let {
                        (getNavigator() as ResturentNavigator).showFeedbackMessage(it)} }

                    ))
        }else{ (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}

    }
    private fun  getUserCoupons(tId: String, custid:Long){
         if (getApplicationUtility().isInternetConnected()){
             (getNavigator() as ResturentNavigator).oncartProgress(true)
             val token = TokenRequest()
             token.tId = tId
             val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
             data.custId= custid
             token.data = data
             val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
             getCompositeDisposable().add(
                 getApplicationRequest().getUserCoupons(PostData(request)).doOnSuccess { }
                     .subscribeOn(getScheduleProvider().io())
                     .observeOn(getScheduleProvider().ui())
                     .subscribe({ responce: ResponceModel ->

                         if (responce.serviceStatus.equals("S", true)) {
                             if (responce.data != null) {
                                 responce.data?.let {
                                     updateCoupon(BuilderManager.getDataArray(BuilderManager.decodeString(it)))
                                     (getNavigator() as ResturentNavigator).onCoupon()
                                 }
                             } else {
                                 (getNavigator() as ResturentNavigator).showFeedbackMessage("Something went wrong!")
                             }
                         } else {
                             responce.message?.let {
                                 (getNavigator() as ResturentNavigator).showFeedbackMessage(it)
                             }
                         }
                         (getNavigator() as ResturentNavigator).oncartProgress(false)
                     },
                         { throwable: Throwable? ->
                             (getNavigator() as ResturentNavigator).oncartProgress(false)
                             throwable?.message?.let {
                                 (getNavigator() as ResturentNavigator).showFeedbackMessage(
                                     it
                                 )
                             }
                         }
                     ))
         } else {
             (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
         }
    }
    private fun  updateCoupon(tId: String, custid:Long){
        if (getApplicationUtility().isInternetConnected()){
            (getNavigator() as ResturentNavigator).oncartProgress(true)
            val token = TokenRequest()
            token.tId = tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.custId= custid
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().updateCoupon(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->

                        if (responce.serviceStatus.equals("S", true)) {
                            getUserCoupons(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0L)
                        } else {
                            responce.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(it)
                            }
                        }
                        (getNavigator() as ResturentNavigator).oncartProgress(false)
                    },
                        { throwable: Throwable? ->
                            (getNavigator() as ResturentNavigator).oncartProgress(false)
                            throwable?.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }
    private fun updateCoupon(list: ArrayList<Coupon>) {
        val available = CartDataInt.restrurent.coupons
        val validcoupon = java.util.ArrayList<Coupon>()
        if (list.isEmpty().not()) {
            for (u in 0 until available.size) {
                if (available[u].CType.equals("Specific")) {
                    val x = list.indexOfFirst { it.couponCode == available[u].couponCode }
                    if (x != -1) {
                        available[u].custCouponId = list[x].custCouponId
                        available[u].usageCountLeft = list[x].usageCountLeft
                        validcoupon.add(available[u])
                    }
                }
            }
            CartDataInt.validcoupon.validcoupon = validcoupon
            val jsonprofileString = GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.validcoupon)
            getApplicationSharePref().setValidCoupon(jsonprofileString)
        }
    }

    fun saveData(issave :Boolean) {
        if (issave){
            getApplicationSharePref().setCart(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.cartdata))
            getApplicationSharePref().setResturent( GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.restrurent))
            getApplicationSharePref().setProfile(GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(CartDataInt.profiledata))
            getApplicationSharePref().setMenuData(GsonBuilder().setPrettyPrinting()
                .serializeNulls().create().toJson(CartDataInt.menudata))
        }else{
            getApplicationSharePref().setCart(null)
            getApplicationSharePref().setResturent(null)
            getApplicationSharePref().setValidCoupon(null)
            getApplicationSharePref().setMenuData(null)
        }
    }
    fun getUserID():String? = getApplicationSharePref().getUserID()

    fun saveAddress() {
        if (getApplicationSharePref().getAddress().isNullOrEmpty()){
            CartDataInt.profiledata.addressBook?.let {
                if (it.isEmpty().not()){
                    val deliveryaddress =   it[0]
                    val deliveryinfo = DeliveryInfo()
                    deliveryinfo.addressId = deliveryaddress.custAddressBookId
                    deliveryinfo.addr1 = deliveryaddress.addr1
                    deliveryinfo.addr2 = deliveryaddress.addr2
                    deliveryinfo.name = deliveryaddress.fName
                    deliveryinfo.telephone = deliveryaddress.telephone
                    deliveryinfo.instructions = deliveryaddress.instructions
                    deliveryinfo.city = deliveryaddress.city
                    deliveryinfo.state = deliveryaddress.state
                    deliveryinfo.zip = deliveryaddress.zip
                    deliveryinfo.latitude = deliveryaddress.latitude
                    deliveryinfo.longitude = deliveryaddress.longitude
                    deliveryinfo.custAddressName = deliveryaddress.custAddressName
                    CartDataInt.cartdata.deliveryInfo = deliveryinfo
                    getApplicationSharePref().setAddress(Gson().toJson(deliveryinfo))
                }
            }
        }else{
            getApplicationSharePref().getAddress()?.let {
                val deliveryinfo: DeliveryInfo = Gson().fromJson(it, DeliveryInfo::class.java)
                CartDataInt.cartdata.deliveryInfo =deliveryinfo
            }
        }
    }
    fun getAvailableCoupon() {
        loginByCustId(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0L)
    }
    private fun loginByCustId(tId: String, custid:Long){
        if (getApplicationUtility().isInternetConnected()) {
            (getNavigator() as ResturentNavigator).oncartProgress(true)
            val token = TokenRequest()
            token.tId = tId
            val data = com.exchange.user.module.restaurent_module.model.api_model.Data()
            data.custId= custid
            token.data = data
            val request = BuilderManager.encodeString(GsonBuilder().create().toJson(token))
            getCompositeDisposable().add(
                getApplicationRequest().DoLoginByCustId(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S", true)) {

                            if (responce.data!=null){
                                responce.data?.let {
                                    val profiledata : ProfileModel = Gson().fromJson(BuilderManager.decodeString(it), ProfileModel::class.java)
                                    CartDataInt.profiledata = profiledata
                                    CartDataInt.cartdata.custId = profiledata.id
                                    saveData(profiledata)
                                }
                            }else{
                                (getNavigator() as ResturentNavigator).showFeedbackMessage("Something went wrong!")
                            }
                            updateCoupon(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0L)
                           // getUserCoupons(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0L)
                        } else {
                            responce.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(it)
                            }
                        }
                        (getNavigator() as ResturentNavigator).oncartProgress(false)
                    },
                        { throwable: Throwable? ->
                            (getNavigator() as ResturentNavigator).oncartProgress(false)
                            throwable?.message?.let {
                                (getNavigator() as ResturentNavigator).showFeedbackMessage(
                                    it
                                )
                            }
                        }
                    ))
        } else {
            (getNavigator() as ResturentNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
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
            username.append(" ${it}")
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

    private fun getPostionName(
        items: CatList,
        portionId: String?
    ): String {
        var postiotion ="p1"

        when(portionId?.toLong()){
            items.p1?.id ->{
                postiotion ="p1"}
            items.p2?.id ->{
                postiotion ="p2"}
            items.p3?.id ->{
                postiotion ="p3"}
            items.p4?.id ->{
                postiotion ="p4"}
            items.p5?.id ->{
                postiotion ="p5"}
            items.p6?.id ->{
                postiotion ="p6"}

        }
        return postiotion
    }
    private fun getUnitPrice(
        portionId: String?,
        items: com.exchange.user.module.restaurent_module.model.menu_model.ItemList,
        catagoury: CatList
    ): Double {
        var unitprice = 0.0
        when(portionId?.toLong()){
            catagoury.p1?.id ->{
                unitprice = items.p1}
            catagoury.p2?.id ->{
                unitprice = items.p2}
            catagoury.p3?.id ->{
                unitprice = items.p3}
            catagoury.p4?.id ->{
                unitprice = items.p4}
            catagoury.p5?.id ->{
                unitprice = items.p5}
            catagoury.p6?.id ->{
                unitprice = items.p6}
        }
        return unitprice
    }

    private fun getItemAddOnList(
        category: CatList,
        items: com.exchange.user.module.restaurent_module.model.menu_model.ItemList,
        itemAddOnList: List<OrderItemsAddOnList>?
    ): java.util.ArrayList<ItemsAddonList> {
        val addonList = java.util.ArrayList<ItemsAddonList>()
        itemAddOnList?.let { itemsAddOn->
            if (itemsAddOn.isEmpty().not()){
                for (x in itemAddOnList.indices){
                    val addOnItems =  items.addOnList.find { it.itemAddOnId == itemAddOnList[x].itemAddOnId }
                    addOnItems?.let { addonitems->
                        val itemsAddOn1 =  ItemsAddonList()
                        itemsAddOn1.itemAddOnId = addOnItems.itemAddOnId
                        itemsAddOn1.name = addOnItems.name
                        itemsAddOn1.addOnOptions =  getAddOnOption(category,addonitems,itemAddOnList[x].addOnOptions)
                        addonList.add(itemsAddOn1)
                    }
                }
            }
        }
        return addonList
    }

    private fun getaddOnOptionPrice(
        addOnOptionitem: com.exchange.user.module.restaurent_module.model.menu_model.AddOnOption, portionId: String): Double {
        var unitprice = 0.0
        when(portionId){
            "p1"->{unitprice=addOnOptionitem.p1}
            "p2"->{unitprice=addOnOptionitem.p2}
            "p3"->{unitprice=addOnOptionitem.p3}
            "p4"->{unitprice=addOnOptionitem.p4}
            "p5"->{unitprice=addOnOptionitem.p5}
            "p6"->{unitprice=addOnOptionitem.p6}
        }

        return unitprice
    }

    private fun getModifire(moddifireone: java.util.ArrayList<AddOnOptionModifier>, addOnOptionModifier1: AddOnOptionModifier2?): AddOnOptionModifier? {
        var  addOnOptionModifier :AddOnOptionModifier?= null
        if (addOnOptionModifier1!=null){
            addOnOptionModifier = moddifireone.find {it.text == addOnOptionModifier1.text}
        }
        return addOnOptionModifier
    }


    private fun getAddOnOption(
        category: CatList,
        addonItems: com.exchange.user.module.restaurent_module.model.menu_model.AddOnList,
        addOnOptions: List<com.exchange.user.module.order_info_module.model.AddOnOption>?
    ): java.util.ArrayList<AddOnOption> {
        val addonOptionList = java.util.ArrayList<AddOnOption>()
        addOnOptions?.let {
            if (addOnOptions.isEmpty().not()){
                for (x in addOnOptions.indices){
                    val addOnOption = addonItems.addOnOptions?.find {it.id == addOnOptions[x].id }
                    addOnOption?.let { addOnOptionItem ->
                        val patinae =  getPostionName(category,addOnOptions[x].portionId)
                        val price = getaddOnOptionPrice(addOnOptionItem,patinae)
                        val addonOptionitemAdd = AddOnOption()
                        addonOptionitemAdd.id = addOnOptionItem.id
                        addonOptionitemAdd.unitPrice = price
                        addonOptionitemAdd.isSelected = true
                        addonOptionitemAdd.dflt = addOnOptionItem.dflt
                        addonOptionitemAdd.name = addOnOptionItem.name
                        addonOptionitemAdd.portionId = addOnOptions[x].portionId?.toLong()
                        addonOptionitemAdd.qty =  addOnOptions[x].qty
                        addonItems.addOnOptionModifier1?.let {
                            val modifiable  = BuilderManager.getModifierList(it)
                            addonOptionitemAdd.addOnOptionModifier1 = getModifire(modifiable,addOnOptions[x].addOnOptionModifier1)
                            addonOptionitemAdd.addOnOptionModifier2 = getModifire(modifiable,addOnOptions[x].addOnOptionModifier2)
                        }
                        addonOptionitemAdd.amt = getAddOnOptionAmt(addonOptionitemAdd)
                        addonOptionList.add(addonOptionitemAdd)
                    }

                }

            }

        }

        return addonOptionList
    }


    private fun getAddOnOptionAmt(addonOptionitemAdd: AddOnOption): Double {
        var amt = addonOptionitemAdd.unitPrice?:0.0
        var modifierprice=1.0
        if (addonOptionitemAdd.addOnOptionModifier1!=null){
            modifierprice = addonOptionitemAdd.unitPrice?.times(addonOptionitemAdd.addOnOptionModifier1?.factor?:0.0)?:1.0
            amt = modifierprice

        }
        if (addonOptionitemAdd.addOnOptionModifier2!=null){
            if (modifierprice==1.0){
                amt = addonOptionitemAdd.unitPrice?.times(addonOptionitemAdd.addOnOptionModifier2?.factor?:0.0)?:1.0
            }else{
                amt = modifierprice.times(addonOptionitemAdd.addOnOptionModifier2?.factor?:0.0)

            }
        }
        return amt
    }

    private fun getItemAmt(itemList: ItemList): Double {
        val amt: Double
        var amount = 0.0
        if (itemList.itemAddOnList.isNotEmpty()){
            for(a in 0 until itemList.itemAddOnList.size){
                val addonOption = itemList.itemAddOnList[a].addOnOptions
                for (b in 0 until addonOption.size){
                    amount += addonOption[b].amt
                }
            }
        }

        amt = (itemList.unitPrice + amount)
        return amt
    }

    fun addReOrderItems(menudData: MenuModel, orderInfoResonse: OrderInfoModel) {
        val itemListCard = java.util.ArrayList<ItemList>()
        orderInfoResonse.itemList?.let { orderItemsList->
            if (orderItemsList.isNullOrEmpty().not()) {
                for (x in orderItemsList.indices) {
                    val category = menudData.catList?.find {it.catId == orderItemsList[x].catId }
                    category?.let { catagouryItems ->
                        val itemList = ItemList()
                        itemList.categoryId = category.catId
                        val catItems = catagouryItems.itemList.find {it.id == orderItemsList[x].id}
                        catItems?.let { items ->
                            val positionotion = getPostionName(catagouryItems,orderItemsList[x].portionId)
                            if (items.specialOffer!=null){
                                itemList.havespicialoffer = true
                                var spamont : Double ? = 0.0
                                when(positionotion){
                                    "p1"->{ spamont = items.specialOffer!!.amt1 }
                                    "p2"->{ spamont = items.specialOffer!!.amt2 }
                                    "p3"->{ spamont = items.specialOffer!!.amt3 }
                                }
                                itemList.havespicialofferAmount =spamont?:0.0
                            }
                            else{
                                itemList.havespicialoffer = false
                                itemList.havespicialofferAmount =0.0
                            }
                            itemList.minQ = items.minQ?:1
                            itemList.maxQ = items.maxQ?:1
                            itemList.portionId = orderItemsList[x].portionId?.toLong()
                            itemList.id = items.id
                            itemList.specialInstructions = orderItemsList[x].specialInstructions
                            itemList.name =items.name
                            itemList.qty =(orderItemsList[x].qty?:"0").toLong()
                            itemList.isTaxFree = items.isTaxFree?:false
                            itemList.unitPrice = getUnitPrice(orderItemsList[x].portionId,items,category)
                            itemList.itemAddOnList = getItemAddOnList(category,items,orderItemsList[x].itemAddOnList)
                            itemList.amt= getItemAmt(itemList)
                            itemListCard.add(itemList)
                        }
                    }
                }

            }

        }
        CartDataInt.cartdata.itemList = itemListCard
    }


}