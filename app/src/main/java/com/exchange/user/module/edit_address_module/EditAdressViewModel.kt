package com.exchange.user.module.edit_address_module

import android.content.Context
import android.util.Log
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.edit_address_module.model.AddLocationRequest
import com.exchange.user.module.edit_address_module.model.ChangeAddressModel
import com.exchange.user.module.edit_address_module.model.CustomerAddress
import com.exchange.user.module.edit_address_module.model.DataAddress
import com.exchange.user.module.profile_module.model.AddressBook
import com.exchange.user.module.restaurent_module.model.api_model.TokenRequest
import com.exchange.user.module.restaurent_module.model.api_model.TokenResponce
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class EditAdressViewModel(applicationprefrence: ApplicationSharePrefrence, schedulerProvider : SchedulerProvider,
                          applicationutility : ApplicationUtility,
                          applicationcontext : Context,
                          validations  : Validations,
                          applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun onBack(){
        (getNavigator() as EditAdressNavigator).onBackPress()
    }

     fun  saveAndproced(){
         (getNavigator() as EditAdressNavigator).saveAddress()
     }

    fun getPhoneNumber(): String = getApplicationSharePref().getPhoneNumber()?:""
    fun getfirstname(): String{
        if((getApplicationSharePref().getUserName()?:" ").split(" ").isEmpty()){
          return ""
        }else{
            return (getApplicationSharePref().getUserName()?:" ").split(" ")[0]
        }
    }
    fun getlastname(): String {
        if((getApplicationSharePref().getUserName()?:" ").split(" ").isEmpty() || (getApplicationSharePref().getUserName()?:" ").split(" ").size ==1){
            return ""
        } else{
            return (getApplicationSharePref().getUserName()?:" ").split(" ")[1]
        }
    }


    fun getLocation(): String {
        val locationname = StringBuilder()
        locationname.append(getRegion())
        val country = getSelectedCountry()
        locationname.append(",${country.name}")
        return locationname.toString()
    }

    fun getSelectedCountry(): Country {
        val countrydata = getApplicationSharePref().getSelectCountry()
        if (countrydata.isNullOrEmpty()) {
            val countrydatamodel = getCountry()
            return countrydatamodel.country.get(0)
        } else {

            val country: Country = Gson().fromJson(countrydata, Country::class.java)
            return country
        }
    }

    fun getRegion(): String{
        val region = getApplicationSharePref().getLocalRestrurent()
        if (region.isNullOrEmpty().not()) {
            val regiondata: RegionList = Gson().fromJson(region, RegionList::class.java)
            return regiondata.RegionName
        }
        return ""
    }
    private fun getCountry(): CountryModel {
        val countrydatamodel = getApplicationSharePref().getCountry()
        if (countrydatamodel.isNullOrEmpty()) {
            val countrymodel = CountryModel()
            countrymodel.country = BuilderManager.getCountrys()
            val json: String? = Gson().toJson(countrymodel)
            getApplicationSharePref().setCountry(json)
            return countrymodel
        } else {
            val countrymodel: CountryModel = Gson().fromJson(countrydatamodel, CountryModel::class.java)
            return countrymodel

        }


    }

    fun saveaddress(firstname: String,lastname : String,mobileno:String,location: String,
                    houseandareya: String,instructions: String, buldingno: String, roomno: String,addressId:Long,addressname : String) {
        if (getValidation().isFieldEmpty(location)){
            (getNavigator() as EditAdressNavigator).showError(ConstantCommand.LOCATION_ERROR,getContext().getString(R.string.invalid_location))

        }else  if(getValidation().isPhoneNumberValid(mobileno).not()){
            (getNavigator() as EditAdressNavigator).showError(ConstantCommand.M0BILE_ERROR,getContext().getString(R.string.invalid_mobile))
        }else{
            val addressbook = CustomerAddress()
            addressbook.addr1 = "Bldg No.${buldingno},Room No.${roomno}"
            addressbook.addr2 = houseandareya
            addressbook.lat = 0.0
            addressbook.lon = 0.0
            addressbook.addrName = addressname
            addressbook.fName =firstname
            addressbook.mName = ""
            addressbook.lName = lastname
            addressbook.tel = mobileno
            addressbook.state = "other"
            addressbook.city = location
            addressbook.instr = instructions
            addressbook.zip = "000000"
            addressbook.custAddrId = if(addressId==0L){ null }else addressId
            addressbook.custId = CartDataInt.profiledata.id
            addressbook.isPrimary = 0
            setUserAddress(CartDataInt.orderdata.tId?:"",addressbook)
        }

    }



    private fun setUserAddress(tId: String, addressbook: CustomerAddress) {
        if (tId.isEmpty()){

            getToken(LocList((CartDataInt.restrurent.id?:0).toString(),CartDataInt.restrurent.name?:"",CartDataInt.restrurent.urlname?:""),addressbook)
        }else{
            if (getApplicationUtility().isInternetConnected())
            {
                showProgress()
                val addlocationrequest = AddLocationRequest()
                addlocationrequest.tId = tId
                val dataadress = DataAddress()
                dataadress.customerAddress = addressbook
                addlocationrequest.data = dataadress
                val request= BuilderManager.encodeString(GsonBuilder().create().toJson(addlocationrequest))
                getCompositeDisposable().add(
                    getApplicationRequest().setUserAddress(PostData(request)).doOnSuccess { }
                        .subscribeOn(getScheduleProvider().io())
                        .observeOn(getScheduleProvider().ui())
                        .subscribe({ responce: ResponceModel ->
                            hideProgress()
                            if (responce.serviceStatus.equals("S", true)) {
                                if (responce.data != null) {
                                    responce.data?.let {
                                        val changeaddress: ChangeAddressModel = Gson().fromJson(BuilderManager.decodeString(it),ChangeAddressModel::class.java)
                                        changeaddress.addressBook?.let {
                                            val availableAddressBook = AddressBook()
                                            availableAddressBook.addr1 = it.addr1
                                            availableAddressBook.addr2 = it.addr2
                                            availableAddressBook.latitude = it.latitude
                                            availableAddressBook.longitude = it.longitude
                                            availableAddressBook.custAddressName = it.custAddressName
                                            availableAddressBook.fName = it.fName
                                            availableAddressBook.lName = it.lName
                                            availableAddressBook.mName = it.mName
                                            availableAddressBook.telephone = it.telephone
                                            availableAddressBook.state = it.state
                                            availableAddressBook.city = it.city
                                            availableAddressBook.instructions = it.instructions
                                            availableAddressBook.zip = it.zip
                                            availableAddressBook.custAddressBookId = it.custAddressBookId
                                            availableAddressBook.isPrimaryAddress = it.isPrimaryAddress
                                            CartDataInt.profiledata.addressBook?.let {
                                                val index =  it.indexOfFirst {it.custAddressBookId == it.custAddressBookId}
                                                if (index!=-1){
                                                    it[index] = availableAddressBook
                                                    (getNavigator() as EditAdressNavigator).onChangeAddress(changeaddress)
                                                }else{
                                                    it.add(availableAddressBook)
                                                    (getNavigator() as EditAdressNavigator).onChangeAddress(changeaddress)
                                                }
                                            }

                                        }
                                    }
                                } else {
                                    (getNavigator() as EditAdressNavigator).showFeedbackMessage("Something went wrong!")
                                }
                            } else {
                                responce.message?.let {
                                    (getNavigator() as EditAdressNavigator).showFeedbackMessage(it)
                                }
                            }
                        },
                            { throwable: Throwable? ->
                                hideProgress()
                                throwable?.message?.let {
                                    (getNavigator() as EditAdressNavigator).showFeedbackMessage(
                                        it
                                    )
                                }
                            }

                        ))


            } else {
                (getNavigator() as EditAdressNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
            }
        }
    }



    fun getToken(restLocationId: LocList, addressbook: CustomerAddress) {
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
                                        setUserAddress(tokendata.tId?:"",addressbook)
                                    }
                                }
                            }else{
                                (getNavigator() as EditAdressNavigator).showFeedbackMessage("Something went wrong!")
                            }

                        }else{
                            responce.message?.let {
                                (getNavigator() as EditAdressNavigator).showFeedbackMessage(it)}
                        }
                    },
                        { throwable : Throwable? -> throwable?.message?.let {
                            (getNavigator() as EditAdressNavigator).showFeedbackMessage(it)
                        } }

                    )
            )

        }
        else{ (getNavigator() as EditAdressNavigator).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))}
    }


}