package com.exchange.user.module.cart_module

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bellymelly.user.module.cart_module.adapter.NgoItems
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.FragmentCartBinding
import com.exchange.user.module.base_module.BaseFragment
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.OrderData
import com.exchange.user.module.base_module.base_model.OrderDataModel
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.adapter.LikedItems
import com.exchange.user.module.cart_module.adapter.PaymentAdapter
import com.exchange.user.module.cart_module.adapter.ServiceAdapter
import com.exchange.user.module.cart_module.model.action.AddItemsToCart
import com.exchange.user.module.cart_module.model.action.CartUpdate
import com.exchange.user.module.cart_module.model.action.adapter.CartSelectedAdapter
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.card_model.ItemList
import com.exchange.user.module.cart_module.model.data_model.CartDataViewModel
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.change_address_module.ChangeAddressActivity
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.coupon_module.AppliedCoupenActivity
import com.exchange.user.module.order_confirm_module.OrderConfirmActivity
import com.exchange.user.module.paymentModule.WebPayment
import com.exchange.user.module.restaurent_module.ResturantActivity
import com.exchange.user.module.restaurent_module.model.LatLongModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.*
import com.exchange.user.module.restaurent_module.model.menu_model.MenuModel
import com.exchange.user.module.utility_module.AppExecutors
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.DeliveryZone
import com.exchange.user.module.utility_module.dialog_commands.DialogScheduleTime
import com.exchange.user.module.utility_module.dialog_commands.OkButton
import com.google.gson.GsonBuilder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class CartFragment: BaseFragment<FragmentCartBinding, CartFragmentViewModel>(),CartFragmentNavigator,
    CartUpdate, AddItemsToCart,NgoItems.OnNgoSelectClickListener, DialogScheduleTime,
    ServiceAdapter.ServiceInteface, PaymentAdapter.PaymentInteface {
    private var tipadd = 0L
    private var message : String  = ""
    private var titel : String  = ""
    private var isopen: Boolean = false
    private var isDelievryOutSide: Boolean = false
    private var isMinOrder : Boolean = false
    private var minAmout : Double = 0.0
    private var isChoosePickup : Boolean = false
    private var isChooseDelivery : Boolean = false
    private var isChooseCurbside : Boolean = false
    private var schedulefor: Service? = null
    private lateinit var ngoAdapter: NgoItems
    @Inject
    lateinit var factory : ViewModelProviderFactory
    @Inject
    lateinit var cartselctedadapter : CartSelectedAdapter
    @Inject
    lateinit var serviceAdapter: ServiceAdapter
    @Inject
    lateinit var paymentAdapter: PaymentAdapter

    private lateinit var cartfragmentViewModel : CartFragmentViewModel
    private lateinit var fragmentcartBinding: FragmentCartBinding
    override val bindingVariable: Int get() = BR.cartFragmentViewModel
    override val layoutId: Int get() = R.layout.fragment_cart
    companion object{
        lateinit var  cartFragment : CartFragment
    }

    override val viewModel: CartFragmentViewModel
        get() {
            cartfragmentViewModel = ViewModelProvider(this, factory).get(CartFragmentViewModel::class.java)
            return cartfragmentViewModel
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartFragment = this
        activity?.window?.statusBarColor = ContextCompat.getColor(getContaxt(), R.color.white_color)
        cartfragmentViewModel.setNavigator(this)
        CartDataInt.cartdata.discountAmt = null
        CartDataInt.cartdata.discountType = null
        CartDataInt.cartdata.discount1Amt = null
        CartDataInt.cartdata.discount1Type = null

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentcartBinding = viewDataBinding
        fragmentcartBinding.orderitems.adapter = cartselctedadapter
        fragmentcartBinding.serviceRecy.adapter =serviceAdapter
        fragmentcartBinding.paymentRecy.adapter = paymentAdapter
        fragmentcartBinding.cartdataviewmodel = CartDataViewModel("US")
        cartselctedadapter.setcartupdate(this,CartDataInt.cartdata.itemList)
        serviceAdapter.setListner(this)
        paymentAdapter.setListner(this)

    }

    override fun onStart() {
        super.onStart()
        initData()
        setlitner()
    }

    private fun setlitner() {
        fragmentcartBinding.licanceplate.filters = arrayOf(InputFilter.AllCaps())
        fragmentcartBinding.colorofcar.filters = arrayOf(InputFilter.AllCaps())
        fragmentcartBinding.modelofcar.filters = arrayOf(InputFilter.AllCaps())
        fragmentcartBinding.makerofcar.filters = arrayOf(InputFilter.AllCaps())
    }

    override fun initData() {
        calculateTotal()
        chckEmptyCart()
        appliedCouponDiscount()
        checkRestaurent()
        scheduleorder()
        setSuggection()
        setNgo()
        calculateTotal()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
    override fun onReasturentMenu() {
        val region =  LocList(CartDataInt.restrurent.id.toString(),CartDataInt.restrurent.name?:"",CartDataInt.restrurent.urlname?:"")
        startActivity(Intent(getContaxt(), ResturantActivity:: class.java).putExtra(ConstantCommand.DATA,region))
    }
    override fun applyCoupon() {
      startActivity(Intent(mContext, AppliedCoupenActivity::class.java))
    }
    override fun tip(tip: Int) {
        tipcalulate(tip)
    }
    override fun cancelCoupon() {
        removeCoupon()
        calculateTotal()
        appliedCouponDiscount()
    }

    override fun cancelDiscount() {
        removeDiscount()
        calculateTotal()
        appliedCouponDiscount()

    }

    override fun onScheduleTime() {
      //  mContext.startActivity(Intent(mContext, ScheduleOrderActivity::class.java))
        AppExecutors.instance.mainThread().execute {
            CartDataInt.restrurent.let { resturent ->
                 val service  = serviceAdapter.itemList.find { it.isSelected }
                service?.let {selectedService->
                    BuilderManager.scheduleOrder(getContaxt(),resturent, CartDataInt.cartdata,this,selectedService)
                }

            }
        }
    }

    override fun openPayment(openLink: String) {
        fragmentcartBinding.buttonSubmit.isEnabled = true
        (getContaxt() as Activity).startActivityForResult(Intent(context,WebPayment::class.java)
            .putExtra(ConstantCommand.DATA,openLink),ConstantCommand.LOC_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==ConstantCommand.LOC_REQ_CODE){
            if (resultCode == Activity.RESULT_OK){
                onCashPayment()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onScheduleTimeString(service: Service) {
        schedulefor = service
        scheduleorder()
        checkServiceValidate()
    }

    override fun addOrChangeAddress() {
        try {
            startActivity(Intent(activity,ChangeAddressActivity::class.java))
        }catch (e: Exception){}
    }


    override fun startOrderProgress(boolean: Boolean) {
        val autoTransition = AutoTransition()
        autoTransition.duration = 100
        TransitionManager.beginDelayedTransition(fragmentcartBinding.submitbtn,autoTransition)
        if (boolean){
            fragmentcartBinding.buttonSubmit.visibility= View.GONE
            fragmentcartBinding.animatedview.visibility= View.VISIBLE
        }
        else{
            fragmentcartBinding.buttonSubmit.visibility= View.VISIBLE
            fragmentcartBinding.animatedview.visibility= View.GONE
        }

    }

    override fun submit() {
        Log.e("Orderdata - ", GsonBuilder().serializeNulls().create().toJson(CartDataInt.cartdata))
        //setNgoData()
        when {
            CartDataInt.cartdata.timeSelection==null -> {
                onScheduleTime()
            }
            checkServiceValidate().not() -> {
                BuilderManager.AlertMessage(mContext,titel,message,"OKAY",object : OkButton {
                    override fun onClickOkey(){}})
            }

            CartDataInt.cartdata.paymentTypeId==null -> {
                BuilderManager.AlertMessageWihoutImage(mContext,
                    "Missing Payment Method",getContaxt().getString(R.string.choose_payment_option),"OKAY",
                    object : OkButton {
                        override fun onClickOkey(){}})
            }
            else -> {
                if (CartDataInt.cartdata.paymentTypeId ==25L){
                    if((CartDataInt.cartdata.totalAmt)<= 0.0){
                        BuilderManager.AlertMessageWihoutImage(mContext,
                            "Choose Other Payment Method ",
                            getContaxt().getString(R.string.choose_other_payment_option),"OKAY",
                            object : OkButton {
                                override fun onClickOkey(){}})
                    }else{
                        onMiltryStarPayment()
                    }
                }else{
                    CartDataInt.restrurent.paymentTypes?.let { it ->
                        val index =   it.indexOfFirst { it.id==CartDataInt.cartdata.paymentTypeId }
                        if (index!=-1){
                            when {
                                it[index].type.equals("CC") -> {
                                    if((CartDataInt.cartdata.totalAmt)<= 0.0){
                                        BuilderManager.AlertMessageWihoutImage(mContext,
                                            "Choose Other Payment Method ",
                                            getContaxt().getString(R.string.choose_other_payment_option),"OKAY",
                                            object : OkButton {
                                                override fun onClickOkey(){}})
                                    }else{
                                        onCreditCardPayment()
                                    }
                                }
                                it[index].type.equals("STAR") -> {
                                    if((CartDataInt.cartdata.totalAmt)<= 0.0){
                                        BuilderManager.AlertMessageWihoutImage(mContext,
                                            "Choose Other Payment Method ",
                                            getContaxt().getString(R.string.choose_other_payment_option),"OKAY",
                                            object : OkButton {
                                                override fun onClickOkey(){}})
                                    }else{
                                        onMiltryStarPayment()
                                    }
                                }
                                it[index].type.equals("POP") -> onCashPayment()
                                it[index].type.equals("$$") -> onCashPayment()
                                it[index].type.equals("PCHK") -> onCashPayment()
                                else -> {
                                    fragmentcartBinding.buttonSubmit.isEnabled = true
                                    Toast.makeText(getContaxt(),"Payment type is not valid",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private fun onCreditCardPayment() {
        cartfragmentViewModel.setTempOrder(ConstantCommand.FROM_CREDIT_CART)
    }

    private fun onMiltryStarPayment() {
        cartfragmentViewModel.setTempOrder(ConstantCommand.FROM_MILTRY_STAR)
    }

    private fun onCashPayment(){
        fragmentcartBinding.buttonSubmit.isEnabled = false
        val datavalue = OrderData()
        datavalue.setmOrderData(CartDataInt.cartdata)
        CartDataInt.orderdata.data = datavalue
        cartfragmentViewModel.setOrder(CartDataInt.orderdata,null)
    }

    override fun remove(adapterPosition: Int) {
        BuilderManager.AlertMessage(
            mContext,
            "Remove Item !!",
            "Are you Sure ! You want to remove Items from Cart",
            "Remove",
            object : OkButton {
                override fun onClickOkey() {
                    CartDataInt.cartdata.itemList.removeAt(adapterPosition)
                    if (::cartselctedadapter.isInitialized) {
                        cartselctedadapter.notifyDataSetChanged()
                    }
                    initData()
                    setSuggection()
                    calculateTotal()
                    paymentAdapter.notifyDataSetChanged()
                }

            })
    }

    override fun onIncresses() {
          removeCoupon()
          removeDiscount()
          initData()
          //setNgoData()
          calculateTotal()
        serviceAdapter.notifyDataSetChanged()
    }

    override fun onDecresses() {
          removeCoupon()
          removeDiscount()
           initData()
          //setNgoData()
        serviceAdapter.notifyDataSetChanged()
    }

    override fun onsetItemToCart(items: ItemList) {
        for (x in 0 until CartDataInt.cartdata.itemList.size){
            if (CartDataInt.cartdata.itemList[x].id == items.id)
            {
                CartDataInt.cartdata.itemList[x] = items
            }
        }
        if (::cartselctedadapter.isInitialized){
            cartselctedadapter.notifyDataSetChanged()
        }
        initData()
        serviceAdapter.notifyDataSetChanged()
    }

    override fun onAddItemToCartPopUp(items: ItemList) {
        CartDataInt.cartdata.itemList.add(items)
        if (::cartselctedadapter.isInitialized){
            cartselctedadapter.notifyDataSetChanged()
        }
        initData()
        serviceAdapter.notifyDataSetChanged()
    }

    override fun onAddItemToCart(items:ItemList) {
        CartDataInt.cartdata.itemList.add(items)
        if (::cartselctedadapter.isInitialized){
            cartselctedadapter.notifyDataSetChanged()
        }
        initData()
        serviceAdapter.notifyDataSetChanged()
    }

    override fun onRemoveItemToCart(items: ItemList) {
        try {
            for (x in 0 until CartDataInt.cartdata.itemList.size) {
                if (CartDataInt.cartdata.itemList[x].id == items.id) {
                    CartDataInt.cartdata.itemList.removeAt(x)
                }
            }
            if (::cartselctedadapter.isInitialized){
                cartselctedadapter.notifyDataSetChanged()
            }
            initData()
            setSuggection()
            serviceAdapter.notifyDataSetChanged()
        }catch (e : Exception){
            e.stackTrace
        }
    }

    override fun openImage(img: String?, name: String?) {}

    private fun setNgo(){
        if (CartDataInt.restrurent.ngo.isNullOrEmpty().not()){
            val ngodata  =  ArrayList<Ngo>()
            for(i in 0 until CartDataInt.restrurent.ngo.size){
                if (CartDataInt.restrurent.ngo[i].show == "1"){
                    if (CartDataInt.restrurent.ngo[i].recId== CartDataInt.restrurent.featuredNgoId){
                        CartDataInt.restrurent.ngo[i].isSelected = true
                    }
                    ngodata.add(CartDataInt.restrurent.ngo[i])
                }
            }
            if (ngodata.isEmpty().not()){
                fragmentcartBinding.ngolay.visibility = View.VISIBLE
                fragmentcartBinding.ngoItems.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                ngoAdapter = NgoItems(mContext,ngodata,this)
                fragmentcartBinding.ngoItems.adapter = ngoAdapter

            }else {

                fragmentcartBinding.ngolay.visibility = View.GONE
            }
        }else{
            fragmentcartBinding.ngolay.visibility = View.GONE
        }
    }

    private fun setSuggection() {
        if (CartDataInt.suggestion.sugesstionlist.isNotEmpty()){
            fragmentcartBinding.likelay.visibility = View.VISIBLE
            fragmentcartBinding.likedItems.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            fragmentcartBinding.likedItems.adapter = LikedItems(mContext,ArrayList(CartDataInt.suggestion.sugesstionlist.distinctBy { it.item?.id }), this, CartDataInt.cartdata)
        }
        else{
            fragmentcartBinding.likelay.visibility = View.GONE
        }
    }

    override fun onNgoSelect(ngo: Ngo) {}

    private fun removeCoupon() {
        val autoTransition = AutoTransition()
        autoTransition.duration=100
        TransitionManager.beginDelayedTransition(fragmentcartBinding.couponcontainer,autoTransition)
        CartDataInt.cartdata.couponAmt = null
        CartDataInt.cartdata.custCouponId = null
        CartDataInt.cartdata.restChainCouponId = null
        CartDataInt.cartdata.couponType = null

    }

    private fun removeDiscount(){
        val autoTransition = AutoTransition()
        autoTransition.duration=100
        TransitionManager.beginDelayedTransition(fragmentcartBinding.couponcontainer,autoTransition)
        CartDataInt.cartdata.discountAmt = null
        CartDataInt.cartdata.discountType = null
        CartDataInt.cartdata.discount1Amt = null
        CartDataInt.cartdata.discount1Type = null
    }

    override fun onSelectService(service: Service, isCoupon: Boolean) {
        when {
            service.name.equals("Pickup",true) -> onSelectPickup(service,isCoupon)
            service.name.equals("Curbside",true) -> {onSelectCurbside(service,isCoupon)}
            service.name.equals("On-base Delivery",true) -> onSelectDelivery(service,isCoupon)
            service.name.equals("Off-base Delivery",true) -> onSelectOffBaseDelivery(service,isCoupon)
            else -> {

            }
        }
        scheduleorder()
        calculateTotal()
    }

    private fun checkPaymentDiscont(paymentype: PaymentType) {
        if (CartDataInt.cartdata.discountAmt != null && CartDataInt.cartdata.discountAmt!! > 0.0)
        {
            val y = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discountType }
            if (y != -1) {
                CartDataInt.restrurent.discounts[y].paymentTypes?.let { it ->
                    if (it.any{ it.Id==paymentype.id}) {

                        val discount  =  CartDataInt.restrurent.discounts.find { discountItem -> discountItem.paymentTypes?.any { it.Id == paymentype.id }?:false}
                        discount?.let {
                            if(it.preTax.equals("T")){
                                CartDataInt.cartdata.discountAmt = getCoupenPreTaxAmount(CartDataInt.cartdata.preTaxAmt,it)
                                CartDataInt.cartdata.discountType =   it.id
                            }else{
                                CartDataInt.cartdata.discountAmt = getCoupenPostTaxAmount(CartDataInt.cartdata,it,CartDataInt.cartdata.preTaxAmt)
                                CartDataInt.cartdata.discountType =   it.id
                            }
                            appliedCouponDiscount()
                            calculateTotal()
                        }
                        calculateTotal()
                        appliedCouponDiscount()

                    }else{
                        CartDataInt.cartdata.discountAmt = null
                        CartDataInt.cartdata.discountType = null
                        calculateTotal()
                        appliedCouponDiscount()

                    }
                }

            }
        }
        if (CartDataInt.cartdata.discount1Amt != null && CartDataInt.cartdata.discount1Amt!!> 0.0) {
            val y = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discount1Type }
            if (y != -1) {
                CartDataInt.restrurent.discounts[y].paymentTypes?.let { it ->
                    if (it.any{ it.Id==paymentype.id}) {
                        val discount  =  CartDataInt.restrurent.discounts.find { discountItem -> discountItem.paymentTypes?.any { it.Id == paymentype.id }?:false}
                        discount?.let {
                            if(it.preTax.equals("T")){
                                CartDataInt.cartdata.discount1Amt = getCoupenPreTaxAmount(CartDataInt.cartdata.preTaxAmt,it)
                                CartDataInt.cartdata.discount1Type =   it.id
                            }else{
                                CartDataInt.cartdata.discount1Amt = getCoupenPostTaxAmount(CartDataInt.cartdata,it,CartDataInt.cartdata.preTaxAmt)
                                CartDataInt.cartdata.discount1Type =   it.id
                            }
                            appliedCouponDiscount()
                            calculateTotal()
                        }
                        appliedCouponDiscount()
                        calculateTotal()
                    }else{
                        val autoTransition = AutoTransition()
                        autoTransition.duration = 100
                        TransitionManager.beginDelayedTransition(
                            fragmentcartBinding.couponcontainer,
                            autoTransition
                        )
                        fragmentcartBinding.coupenapplyed.visibility = View.GONE
                        fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
                        CartDataInt.cartdata.discount1Amt = null
                        CartDataInt.cartdata.discount1Type = null
                        appliedCouponDiscount()
                        calculateTotal()
                    }
                }

            }
            else{
                CartDataInt.cartdata.discount1Type = null
                CartDataInt.cartdata.discount1Amt = null
                calculateTotal()
                appliedCouponDiscount()
            }
        }
        else{
            CartDataInt.cartdata.discount1Type = null
            CartDataInt.cartdata.discount1Amt = null
            val discount  =  CartDataInt.restrurent.discounts.find { it -> it.paymentTypes?.any { it.Id == paymentype.id }?:false}
            discount?.let {
                if(it.preTax.equals("T")){
                    CartDataInt.cartdata.discount1Amt = getCoupenPreTaxAmount(CartDataInt.cartdata.preTaxAmt,it)
                    CartDataInt.cartdata.discount1Type =   it.id
                }else{
                    CartDataInt.cartdata.discount1Amt = getCoupenPostTaxAmount(CartDataInt.cartdata,it,CartDataInt.cartdata.preTaxAmt)
                    CartDataInt.cartdata.discount1Type =   it.id
                }
            }
            appliedCouponDiscount()
            calculateTotal()
        }
    }

    private fun getCoupenPreTaxAmount(preTaxAmt: Double, coupen : Discount): Double {
        return when {
            coupen.type.equals("%") -> {
                val persent = (100 - ceil((1 - coupen.disc) * 100).toInt())
                var discount = (persent * preTaxAmt)/100
                if(coupen.maxCap > 0.0){
                    discount =  minOf(discount,coupen.maxCap)
                }
                discount
            }
            coupen.type.equals("$") -> coupen.disc
            else -> 0.0
        }
    }

    private fun getCoupenPostTaxAmount(cartdata: CartData, coupen : Discount,preTaxAmt: Double): Double {
        return when {
            coupen.type.equals("%") -> {
                val persent = coupen.disc*100
                var discount =  (persent * (preTaxAmt+cartdata.taxAmt))/100   // cartdata.preTaxAmt - discountpersent
                if(coupen.maxCap > 0.0){
                    discount =  minOf(discount,coupen.maxCap)
                }
                discount
            }
            coupen.type.equals("$") -> coupen.disc
            else -> 0.0
        }
    }

    override fun onSelectPayment(payment: PaymentType) {
        when {
            payment.type.equals("CC",true) -> { paymentCreditCard(payment)

            }
            payment.type.equals("STAR",true) -> { paymentMiltryStar(payment)

            }
            payment.type.equals("POP",true) -> {
                paymentCash(payment)
            }
            payment.type.equals("$$",true) -> { paymentCash(payment)

            }
            payment.type.equals("PCHK",true) -> {
                paymentCash(payment)

            }
        }
        checkServiceValidate()

    }

    override fun onUnSelectedPayment(payment: PaymentType?) {
        CartDataInt.cartdata.paymentTypeId = null

    }

    private fun paymentCreditCard(payment: PaymentType){
        CartDataInt.cartdata.paymentTypeId = payment.id
        checkPaymentDiscont(payment)
    }

    private fun paymentMiltryStar(payment: PaymentType){
        if (payment.id!=25L){
            CartDataInt.cartdata.paymentTypeId =25L
        }else {
            CartDataInt.cartdata.paymentTypeId = payment.id
        }
        checkPaymentDiscont(payment)
    }
    private fun paymentCash(payment: PaymentType){
        CartDataInt.cartdata.paymentTypeId = payment.id
        CartDataInt.cartdata.aafesPayTraceId=null
        CartDataInt.cartdata.isAAFESPayGateway=null
        checkPaymentDiscont(payment)
    }

    private fun scheduleorder(){
        if (CartDataInt.cartdata.timeSelection!=null && CartDataInt.cartdata.timeSelection==0 && isopen){
            fragmentcartBinding.selectSchedule.text = getContaxt().getString(R.string.today_asap)
        }else if (CartDataInt.cartdata.timeSelection!=null && (CartDataInt.cartdata.timeSelection==0).not()){

            if (schedulefor!=null){
                if (schedulefor?.name?.equals(serviceAdapter.itemList.find {it.isSelected }?.name)!!){
                    val date = BuilderManager.getTime(CartDataInt.cartdata.dueOn ?: "")
                    fragmentcartBinding.selectSchedule.text = BuilderManager.getProperDayTime(date?: Date())
                }else{
                    CartDataInt.cartdata.timeSelection = null
                    fragmentcartBinding.selectSchedule.text = getContaxt().getString(R.string.schedule_my_food)
                }
            }else{
                CartDataInt.cartdata.timeSelection = null
                fragmentcartBinding.selectSchedule.text = getContaxt().getString(R.string.schedule_my_food)
            }

        }else{
            CartDataInt.cartdata.timeSelection = null
            fragmentcartBinding.selectSchedule.text = getContaxt().getString(R.string.schedule_my_food)
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun checkRestaurent() = if (CartDataInt.restrurent.name.isNullOrEmpty().not()
        && cartfragmentViewModel.getUserID()!=null && CartDataInt.profiledata.id!=null) {
        fragmentcartBinding.cardview.visibility = View.VISIBLE
        fragmentcartBinding.data.visibility = View.GONE
        fragmentcartBinding.restroname.text = CartDataInt.restrurent.name
        BuilderManager.LoadImage(fragmentcartBinding.headerImage,CartDataInt.restrurent.header)
        BuilderManager.LoadImage(fragmentcartBinding.headerbackgroundImage,CartDataInt.restrurent.headerBackgroundImage)

        isopen = BuilderManager.isResturentOpen(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,CartDataInt.restrurent.timeZone!!)
        CartDataInt.restrurent.services?.let {
            val  service = ArrayList<Service>()
            it.forEach { items->
                if (items.available.equals("T",true) && BuilderManager.isAvailableService(items,CartDataInt.restrurent.timeZone!!)){
                    service.add(items)
                }

            }

            if (service.any { it.isSelected }.not()){
                service[0].isSelected =true
                schedulefor = service[0]
            }
            serviceAdapter.addItems(service)
        }

        if (CartDataInt.restrurent.address!= null) {
            val readdress = StringBuilder()
            if (!CartDataInt.restrurent.address!!.addressLine1.isNullOrEmpty()) {
                readdress.append("${CartDataInt.restrurent.address!!.addressLine1},")
            }
            if (!CartDataInt.restrurent.address!!.addressLine2.isNullOrEmpty()) {
                readdress.append("${CartDataInt.restrurent.address!!.addressLine2},")
            }
//            if (!CartDataInt.restrurent.address!!.city.isNullOrEmpty()) {
//                readdress.append("${CartDataInt.restrurent.address!!.city}")
//            }

//            if (!CartDataInt.restrurent.address!!.state.isNullOrEmpty()) {
//                readdress.append("${CartDataInt.restrurent.address!!.state},")
//            }
//            if (!CartDataInt.restrurent.address!!.zip.isNullOrEmpty()) {
//                readdress.append("${CartDataInt.restrurent.address!!.zip},")
//            }
            fragmentcartBinding.restroaddress.text  = readdress
        } else {
            fragmentcartBinding.restroaddress.text  = ""
        }

        if (CartDataInt.cartdata.deliveryInfo != null) {
            fragmentcartBinding.changeoradd.text = "Change"
            val address = StringBuilder()

            if (!CartDataInt.cartdata.deliveryInfo!!.addr1.isNullOrEmpty()) {
                address.append("${CartDataInt.cartdata.deliveryInfo!!.addr1},")
            }
            if (!CartDataInt.cartdata.deliveryInfo!!.addr2.isNullOrEmpty()) {
                address.append("${CartDataInt.cartdata.deliveryInfo!!.addr2},")
            }
            if (!CartDataInt.cartdata.deliveryInfo!!.city.isNullOrEmpty()) {
                address.append("${CartDataInt.cartdata.deliveryInfo!!.city},")
            }
//
//            if (!CartDataInt.cartdata.deliveryInfo!!.state.isNullOrEmpty()) {
//                address.append("${CartDataInt.cartdata.deliveryInfo!!.state},")
//            }
//            if (!CartDataInt.cartdata.deliveryInfo!!.zip.isNullOrEmpty()) {
//                address.append("${CartDataInt.cartdata.deliveryInfo!!.zip},")
//            }

            val mobile = StringBuilder()

            if (!CartDataInt.cartdata.deliveryInfo!!.name.isNullOrEmpty()) {
                fragmentcartBinding.mobileno.visibility = View.VISIBLE
                mobile.append("${CartDataInt.cartdata.deliveryInfo!!.name} ")
            }

            if (!CartDataInt.cartdata.deliveryInfo!!.telephone.isNullOrEmpty()) {
                fragmentcartBinding.mobileno.visibility = View.VISIBLE
                mobile.append("${CartDataInt.cartdata.deliveryInfo!!.telephone}")
            }
            fragmentcartBinding.deliveryittel.text = "Delivery Address"

            if (!CartDataInt.cartdata.deliveryInfo!!.custAddressName.isNullOrEmpty()){
                when(CartDataInt.cartdata.deliveryInfo!!.custAddressName?:"".toLowerCase()){

                    "home" ->{
                        fragmentcartBinding.deliveryittel.text = "Delivery to"
                        fragmentcartBinding.dliveryname.text ="Home"
                        fragmentcartBinding.dliveryname.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.underline_orange))
                        fragmentcartBinding.checkimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_ic_check_orange))
                        fragmentcartBinding.dliveryname.setTextColor(Color.parseColor("#EF9C21"))
                        fragmentcartBinding.dliveryname.visibility = View.VISIBLE
                    }
                    "office" ->{
                        fragmentcartBinding.deliveryittel.text = "Delivery to"
                        fragmentcartBinding.dliveryname.text ="Office"
                        fragmentcartBinding.dliveryname.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.underline_green))
                        fragmentcartBinding.checkimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_ic_check_green))
                        fragmentcartBinding.dliveryname.setTextColor(Color.parseColor("#15AD15"))
                        fragmentcartBinding.dliveryname.visibility = View.VISIBLE
                    }
                    "other" ->{
                        fragmentcartBinding.deliveryittel.text = "Delivery to"
                        fragmentcartBinding.dliveryname.text ="Other"
                        fragmentcartBinding.checkimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_ic_check))
                        fragmentcartBinding.dliveryname.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.underline_blue))
                        fragmentcartBinding.dliveryname.setTextColor(Color.parseColor("#4A40BA"))
                        fragmentcartBinding.dliveryname.visibility = View.VISIBLE
                    }
                }
            }else{

                fragmentcartBinding.deliveryittel.text = "Delivery Address"
                fragmentcartBinding.dliveryname.visibility = View.GONE
                fragmentcartBinding.checkimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_ic_check))
            }

            fragmentcartBinding.mobileno.text =   mobile
            fragmentcartBinding.address.text = address.toString()
        } else {
            fragmentcartBinding.address.text = ""
            fragmentcartBinding.changeoradd.text = "Add"
            fragmentcartBinding.deliveryittel.text = "Delivery Address"
        }
        if(!CartDataInt.restrurent.logoImg.isNullOrEmpty()){
            BuilderManager.LoadImage(fragmentcartBinding.restroimage,CartDataInt.restrurent.logoImg)
        }
        else{
            fragmentcartBinding.restroimage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.image))
        }
        when {
            CartDataInt.cartdata.custId==null -> {
                val location =  LocList((CartDataInt.restrurent.id?:0).toString(),CartDataInt.restrurent.name?:"",CartDataInt.restrurent.urlname?:"")
                cartfragmentViewModel.loginByCustId(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0,location)
            }
            CartDataInt.orderdata.tId.isNullOrEmpty() -> {
                val location =  LocList((CartDataInt.restrurent.id?:0).toString(),CartDataInt.restrurent.name?:"",CartDataInt.restrurent.urlname?:"")
                cartfragmentViewModel.loginByCustId(CartDataInt.orderdata.tId?:"",CartDataInt.profiledata.id?:0,location)
            }
            else -> {

            }
        }
    }
    else{
        fragmentcartBinding.cardview.visibility = View.GONE
        fragmentcartBinding.data.visibility = View.VISIBLE
    }

    private fun applyCoupenDiscountWithService(service: Service) {
        if (CartDataInt.cartdata.discountAmt != null && CartDataInt.cartdata.discountAmt!! > 0.0) {
            val y =
                CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discountType }
            if (y != -1) {
                CartDataInt.restrurent.discounts[y].service?.let {
                    if (it.serviceId != service.id) {
                        val autoTransition = AutoTransition()
                        autoTransition.duration = 100
                        TransitionManager.beginDelayedTransition(fragmentcartBinding.couponcontainer,autoTransition)
                        CartDataInt.cartdata.discountAmt = null
                        CartDataInt.cartdata.discountType = null
                        calculateTotal()
                        appliedCouponDiscount()
                    }
                }
            }
        }
        else if (CartDataInt.cartdata.discount1Amt != null && CartDataInt.cartdata.discount1Amt!! > 0.0) {
            val y =
                CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discount1Type }
            if (y != -1) {
                CartDataInt.restrurent.discounts[y].service?.let {
                    if (it.serviceId != service.id) {
                        val autoTransition = AutoTransition()
                        autoTransition.duration = 100
                        TransitionManager.beginDelayedTransition(fragmentcartBinding.couponcontainer,autoTransition)
                        CartDataInt.cartdata.discount1Amt = null
                        CartDataInt.cartdata.discount1Type = null
                        calculateTotal()
                        appliedCouponDiscount()
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private  fun appliedCouponDiscount(){

        if (
            (CartDataInt.cartdata.couponAmt!=null && CartDataInt.cartdata.couponAmt!! > 0.0) &&
            ((CartDataInt.cartdata.discountAmt!=null && CartDataInt.cartdata.discountAmt!! > 0.0)||
            (CartDataInt.cartdata.discount1Amt!=null && CartDataInt.cartdata.discount1Amt!! > 0.0))){
            fragmentcartBinding.Gocoupenapply.visibility = View.GONE
        }
        if (
            (CartDataInt.cartdata.couponAmt!=null && CartDataInt.cartdata.couponAmt!! > 0.0).not() &&
            (CartDataInt.cartdata.discountAmt!=null && CartDataInt.cartdata.discountAmt!! > 0.0).not() &&
            (CartDataInt.cartdata.discount1Amt!=null && CartDataInt.cartdata.discount1Amt!! > 0.0).not()
           ){

            val autoTransition = AutoTransition()
            autoTransition.duration = 100
            TransitionManager.beginDelayedTransition(fragmentcartBinding.couponcontainer, autoTransition)
            fragmentcartBinding.coupenapplyed.visibility = View.GONE
            fragmentcartBinding.discountapplyed.visibility = View.GONE
            fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
        } else{
            if (CartDataInt.cartdata.couponAmt!=null && CartDataInt.cartdata.couponAmt!! > 0.0){
                fragmentcartBinding.coupenapplyed.visibility = View.VISIBLE
                //fragmentcartBinding.Gocoupenapply.visibility = View.GONE
                if (CartDataInt.cartdata.custCouponId!=null){
                    val x = CartDataInt.restrurent.coupons.indexOfFirst { it.id == CartDataInt.cartdata.custCouponId }
                    if (x!=-1){
                        fragmentcartBinding.couponname.text = CartDataInt.restrurent.coupons[x].couponCode
                        fragmentcartBinding.couponoffprice.text = " (${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)} OFF)"
                        fragmentcartBinding.couponmessage.text = "Congratulations, Coupon Applied Successfully !!"
                        // serviceAdapter.setCoupon(true)
                        checkServiceValidate()
                    }else{
                        val y = CartDataInt.restrurent.coupons.indexOfFirst { it.custCouponId == CartDataInt.cartdata.custCouponId }
                        if (y!=-1){
                            fragmentcartBinding.couponname.text = CartDataInt.restrurent.coupons[y].couponCode
                            fragmentcartBinding.couponoffprice.text = " (${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)} OFF)"
                            fragmentcartBinding.couponmessage.text = "Congratulations, Coupon Applied Successfully !!"
                            //  serviceAdapter.setCoupon(true)
                            checkServiceValidate()
                        }else{
                            val autoTransition = AutoTransition()
                            autoTransition.duration = 100
                            TransitionManager.beginDelayedTransition( fragmentcartBinding.couponcontainer,autoTransition)
                            fragmentcartBinding.coupenapplyed.visibility = View.GONE
                            fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
                            //  serviceAdapter.setCoupon(false)
                        }

                    }
                }
                else if (CartDataInt.cartdata.restChainCouponId!=null){
                    val x = CartDataInt.restrurent.coupons.indexOfFirst { it.id == CartDataInt.cartdata.restChainCouponId }
                    if (x!=-1){
                          fragmentcartBinding.couponname.text = CartDataInt.restrurent.coupons[x].couponCode
                        fragmentcartBinding.couponoffprice.text = " (${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)} OFF)"
                        fragmentcartBinding.couponmessage.text = "Congratulations, Coupon Applied Successfully !!"
                        //serviceAdapter.setCoupon(true)
                        checkServiceValidate()
                    }else{
                        val y = CartDataInt.profiledata.coupons.indexOfFirst { it.custCouponId == CartDataInt.cartdata.restChainCouponId }
                        if (y!=-1){
                            fragmentcartBinding.couponname.text = CartDataInt.profiledata.coupons[y].couponCode
                            fragmentcartBinding.couponoffprice.text = " (${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)} OFF)"
                            fragmentcartBinding.couponmessage.text = "Congratulations, Coupon Applied Successfully !!"
                            // serviceAdapter.setCoupon(true)
                            checkServiceValidate()
                        }else{
                            val autoTransition = AutoTransition()
                            autoTransition.duration = 100
                            TransitionManager.beginDelayedTransition( fragmentcartBinding.couponcontainer,autoTransition)
                            fragmentcartBinding.coupenapplyed.visibility = View.GONE
                            fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
                            // serviceAdapter.setCoupon(false)
                        }
                    }
                }
            }
            else{
                val autoTransition = AutoTransition()
                autoTransition.duration = 100
                TransitionManager.beginDelayedTransition(
                    fragmentcartBinding.couponcontainer,
                    autoTransition
                )
                fragmentcartBinding.coupenapplyed.visibility = View.GONE
                fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
            }

            if(CartDataInt.cartdata.discountAmt!=null && CartDataInt.cartdata.discountAmt!! > 0.0) {

                fragmentcartBinding.discountapplyed.visibility = View.VISIBLE
                // fragmentcartBinding.Gocoupenapply.visibility = View.GONE

                val x = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discountType }

                if (x != -1) {
//                    fragmentcartBinding.discountmessage.setTextColor(ContextCompat.getColor(mContext,R.color.black_color))
//                    fragmentcartBinding.discountoffprice.setTextColor(ContextCompat.getColor(mContext,R.color.color_green))
//                    fragmentcartBinding.discountname.setTextColor(ContextCompat.getColor(mContext,R.color.black_color))
                    fragmentcartBinding.discountname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coupondiscount,0,0,0)


                    var isPaymentDiscount = false
                    CartDataInt.restrurent.discounts[x].paymentTypes?.let { it ->
                        isPaymentDiscount = it.any { it.Id?:0L==CartDataInt.cartdata.paymentTypeId}
                    }
                    if (isPaymentDiscount){
                        var paymentname=""
                        var whatpersent =""
                        if (CartDataInt.restrurent.discounts[x].type.equals("%")){
                            whatpersent = "${(CartDataInt.restrurent.discounts[x].disc * 100)}%"
                        }
                        val paymenttype: PaymentType? = if (CartDataInt.cartdata.paymentTypeId==25L){
                            CartDataInt.restrurent.paymentTypes?.find { it.type == "STAR" }
                        }else{
                            CartDataInt.restrurent.paymentTypes?.find { it.id == CartDataInt.cartdata.paymentTypeId }
                        }
                        when {
                            paymenttype?.type.equals("CC", true) -> {
                                paymentname = getString(R.string.debit_credit_card)
                                fragmentcartBinding.crocediscount.visibility = View.VISIBLE
                            }
                            paymenttype?.type.equals("STAR", true) -> {
//                                fragmentcartBinding.discountmessage.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountoffprice.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountname.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountapplyed.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                                fragmentcartBinding.discountname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_star_milty,0,0,0)
                                paymentname = getString(R.string.miltry_star_card)
                                fragmentcartBinding.crocediscount.visibility = View.GONE
                            }
                            else -> paymentname = paymenttype?.desc ?: ""
                        }
                        fragmentcartBinding.discountname.text =
                            CartDataInt.restrurent.discounts[x].description
                        fragmentcartBinding.discountoffprice.text =
                            " (${BuilderManager.getFormat().format(
                                CartDataInt.cartdata.discountAmt ?: 0.0
                            )} OFF)"
                        fragmentcartBinding.discountoffprice.visibility = View.GONE
                         val message = "Thank you for using your ${paymentname}! \n" +
                                 "You are saving an additional $whatpersent (${BuilderManager.getFormat().format(
                                     CartDataInt.cartdata.discountAmt ?: 0.0
                                 )})!"
                        fragmentcartBinding.discountmessage.text =message

                    }
                    else {
                        fragmentcartBinding.discountoffprice.visibility = View.VISIBLE
                        fragmentcartBinding.discountname.text =
                            CartDataInt.restrurent.discounts[x].description
                        fragmentcartBinding.discountoffprice.text =
                            " (${BuilderManager.getFormat().format(
                                CartDataInt.cartdata.discountAmt ?: 0.0
                            )} OFF)"
                        fragmentcartBinding.discountmessage.text =
                            "Congratulations, Discount Applied Successfully !!"
                    }
                    //serviceAdapter.setCoupon(true)
                } else {
                    val autoTransition = AutoTransition()
                    autoTransition.duration = 100
                    TransitionManager.beginDelayedTransition(
                        fragmentcartBinding.couponcontainer,
                        autoTransition
                    )
                    fragmentcartBinding.discountapplyed.visibility = View.GONE
                    fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
                    // serviceAdapter.setCoupon(false)

                }
            }
            else if(CartDataInt.cartdata.discount1Amt!=null && CartDataInt.cartdata.discount1Amt!! > 0.0) {

                fragmentcartBinding.discountapplyed.visibility = View.VISIBLE
                // fragmentcartBinding.Gocoupenapply.visibility = View.GONE

                val x = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discount1Type }

                if (x != -1) {
//                    fragmentcartBinding.discountmessage.setTextColor(ContextCompat.getColor(mContext,R.color.black_color))
//                    fragmentcartBinding.discountoffprice.setTextColor(ContextCompat.getColor(mContext,R.color.color_green))
//                    fragmentcartBinding.discountname.setTextColor(ContextCompat.getColor(mContext,R.color.black_color))
                    fragmentcartBinding.discountname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coupondiscount,0,0,0)
                  //  fragmentcartBinding.discountapplyed.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white_color))
                    var ispaymentDiscount = false
                    CartDataInt.restrurent.discounts[x].paymentTypes?.let { it ->
                        ispaymentDiscount = it.any { it.Id?:0L==CartDataInt.cartdata.paymentTypeId}
                    }

                    if (ispaymentDiscount){
                        var paymentname=""
                        var whatpersent =""
                        if (CartDataInt.restrurent.discounts[x].type.equals("%")){
                            whatpersent = "${(CartDataInt.restrurent.discounts[x].disc * 100)}%"
                        }
                        val paymenttype: PaymentType? = if (CartDataInt.cartdata.paymentTypeId==25L){
                            CartDataInt.restrurent.paymentTypes?.find { it.type == "STAR" }
                        }else{
                            CartDataInt.restrurent.paymentTypes?.find { it.id == CartDataInt.cartdata.paymentTypeId }
                        }

                        when {
                            paymenttype?.type.equals("CC", true) -> {
                                paymentname = getString(R.string.debit_credit_card)
                                fragmentcartBinding.crocediscount.visibility = View.VISIBLE
                            }
                            paymenttype?.type.equals("STAR", true) -> {
//                                fragmentcartBinding.discountmessage.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountoffprice.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountname.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
//                                fragmentcartBinding.discountapplyed.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                                fragmentcartBinding.discountname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_icon_star_milty,0,0,0)
                                paymentname = getString(R.string.miltry_star_card)
                                fragmentcartBinding.crocediscount.visibility = View.GONE

                            }
                            else -> {
                                paymentname = paymenttype?.desc ?: ""
                                fragmentcartBinding.crocediscount.visibility = View.VISIBLE
                            }
                        }

                        fragmentcartBinding.discountoffprice.visibility = View.GONE


                        fragmentcartBinding.discountname.text =
                            CartDataInt.restrurent.discounts[x].description
                        fragmentcartBinding.discountoffprice.text =
                            " (${BuilderManager.getFormat().format(
                                CartDataInt.cartdata.discount1Amt ?: 0.0
                            )} OFF)"

                        val message = "Thank you for using your ${paymentname}! \n" +
                                "You are saving an additional $whatpersent (${BuilderManager.getFormat().format(
                                    CartDataInt.cartdata.discount1Amt ?: 0.0
                                )})!"
                        fragmentcartBinding.discountmessage.text =message

                    }
                    else {
                        fragmentcartBinding.discountoffprice.visibility = View.VISIBLE
                        fragmentcartBinding.discountname.text =
                            CartDataInt.restrurent.discounts[x].description
                        fragmentcartBinding.discountoffprice.text =
                            " (${BuilderManager.getFormat().format(
                                CartDataInt.cartdata.discount1Amt ?: 0.0
                            )} OFF)"
                        fragmentcartBinding.discountmessage.text =
                            "Congratulations, Discount Applied Successfully !!"
                    }
                    // serviceAdapter.setCoupon(true)
                } else {
                    val autoTransition = AutoTransition()
                    autoTransition.duration = 100
                    TransitionManager.beginDelayedTransition(
                        fragmentcartBinding.couponcontainer,
                        autoTransition
                    )
                    fragmentcartBinding.discountapplyed.visibility = View.GONE
                    fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
                    // serviceAdapter.setCoupon(false)
                }
            }
            else{

                val autoTransition = AutoTransition()
                autoTransition.duration = 100
                TransitionManager.beginDelayedTransition(
                    fragmentcartBinding.couponcontainer,
                    autoTransition
                )
                fragmentcartBinding.discountapplyed.visibility = View.GONE
                fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
            }
        }

    }

    override fun showOrderError(toString: String) {
        BuilderManager.AlertMessage(getContaxt(), "Error Found.",toString, "OKAY", object :
            OkButton {
            override fun onClickOkey() {}
        })
        fragmentcartBinding.buttonSubmit.isEnabled = true
    }

    override fun openConfirmationScreen() {
        val date = BuilderManager.getTime(CartDataInt.cartdata.dueOn ?: "")
        getContaxt().startActivity(Intent(getContaxt(),OrderConfirmActivity::class.java)
            .putExtra(ConstantCommand.DATA,BuilderManager.getProperDayTime(date?: Date())))
        fragmentcartBinding.buttonSubmit.isEnabled = true
    }

    private fun chckEmptyCart() {
        if (cartfragmentViewModel.getUserID()!=null){
            if (CartDataInt.profiledata.id!=null) {
                if (CartDataInt.cartdata.itemList.isNotEmpty()) {
                    fragmentcartBinding.cardview.visibility = View.VISIBLE
                    fragmentcartBinding.data.visibility = View.GONE
                } else {
                    fragmentcartBinding.cardview.visibility = View.GONE
                    fragmentcartBinding.data.visibility = View.VISIBLE
                }
            }else{
                fragmentcartBinding.cardview.visibility = View.GONE
                fragmentcartBinding.data.visibility = View.VISIBLE
            }
        }else{
            fragmentcartBinding.cardview.visibility = View.GONE
            fragmentcartBinding.data.visibility = View.VISIBLE
        }
    }
    private fun tipcalulate(tip: Int){
        when(tip){
            5 -> {
                when (tipadd) {
                    5.toLong() -> {
                        tipadd = 0.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                    0.toLong() -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                    else -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.button_shape_gradian_second)

                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                    }
                }
            }
            10 -> {
                when (tipadd) {
                    10.toLong() -> {
                        tipadd= 0.toLong()
                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                    0.toLong() -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                    else -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.shape_with_white_graystoke)


                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.button_shape_gradian_second)

                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.shape_with_white_graystoke)


                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                    }
                }
            }
            15 -> {
                when (tipadd) {
                    15.toLong() -> {
                        tipadd = 0.toLong()
                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                    0.toLong() -> {
                        tipadd= tip.toLong()
                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                    else -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.button_shape_gradian_second)


                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.shape_with_white_graystoke)


                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                }
            }
            20 -> {
                when (tipadd) {
                    20.toLong() -> {

                        tipadd = 0.toLong()
                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                    0.toLong() -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                    else -> {
                        tipadd = tip.toLong()

                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.shape_with_white_graystoke)

                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.button_shape_gradian_second)

                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                }
            }
            25 -> {
                when (tipadd) {
                    25.toLong() -> {
                        tipadd= 0.toLong()
                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                    }
                    0.toLong() -> {
                        tipadd= tip.toLong()
                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                    else -> {
                        tipadd = tip.toLong()
                        fragmentcartBinding.tip5.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip5.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                        fragmentcartBinding.tip10.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip10.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                        fragmentcartBinding.tip15.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip15.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                        fragmentcartBinding.tip20.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                        fragmentcartBinding.tip20.setBackgroundResource(R.drawable.shape_with_white_graystoke)
                        fragmentcartBinding.tip25.setTextColor(ContextCompat.getColor(mContext,R.color.white_color))
                        fragmentcartBinding.tip25.setBackgroundResource(R.drawable.button_shape_gradian_second)
                    }
                }
            }
        }
        updateTotal()
    }
    private fun onSelectCurbside(service: Service,coupon: Boolean) {
        CartDataInt.restrurent.paymentTypes?.let { it ->
            val payment = ArrayList<PaymentType>()
            it.forEach { items ->
                service.payment?.forEach {servicepayment->
                    if (servicepayment.id==items.id && !coupon){
                        payment.add(items)
                    }else if (servicepayment.id==items.id && coupon && items.type.equals("POP").not()){
                        payment.add(items)
                    }
                }
            }
            val uniqepayment = payment.distinctBy { it.type }
            payment.clear()
            payment.addAll(uniqepayment)
            if (payment.any { it.isSelected}.not()){
                val  index = payment.indexOfLast {it.type.equals("STAR") }
                if (index!=-1){
                    payment[index].isSelected = true
                }
            }
            CartDataInt.cartdata.paymentTypeId =null
            paymentAdapter.addItems(payment)
        }
        tipcalulate(tipadd.toInt())
        fragmentcartBinding.tiplay.visibility = View.GONE
        CartDataInt.cartdata.serviceId = service.id
        CartDataInt.cartdata.ccInfo.service = service.id
        CartDataInt.cartdata.srvcFee = service.minFee
        val minorder = service.minOrder
        if (minorder > CartDataInt.cartdata.preTaxAmt) {
            isMinOrder = true
            minAmout = minorder
        } else {
            isMinOrder = false
            minAmout = minorder
        }
        Handler(Looper.getMainLooper()).postDelayed({ fragmentcartBinding.expandOption.expand() },500)
        val readytime = (service.readyInMin ?: "0").toLong()
        CartDataInt.cartdata.createdOn = BuilderManager.getCurrentDate(CartDataInt.restrurent.timeZone!!)
        CartDataInt.cartdata.dueOn = BuilderManager.getReadyTime(readytime,CartDataInt.restrurent.timeZone!!)
        fragmentcartBinding.timetaken.text = getContaxt().getString(R.string.pick_in).plus(" ${service.readyInMin} min")
        fragmentcartBinding.timetaken.visibility = View.GONE
        fragmentcartBinding.delivrytimetaken.visibility = View.GONE
        applyCoupenDiscountWithService(service)
        fragmentcartBinding.deliverylay.visibility = View.GONE
        isChooseDelivery = false
        isChoosePickup = false
        isChooseCurbside = true
        checkcurbsideValidate()


    }
    private fun onSelectPickup(service: Service, coupon: Boolean) {
        CartDataInt.restrurent.paymentTypes?.let { it ->
            val payment = ArrayList<PaymentType>()
            it.forEach { items ->
                service.payment?.forEach {servicepayment->
                    if (servicepayment.id==items.id && !coupon){
                        payment.add(items)
                    }else if (servicepayment.id==items.id && coupon && items.type.equals("POP").not()){
                        payment.add(items)
                    }
                }
            }
            val uniqepayment = payment.distinctBy { it.type }
            payment.clear()
            payment.addAll(uniqepayment)
            if (payment.any { it.isSelected}.not()){
                val  index = payment.indexOfLast {it.type.equals("STAR") }
                if (index!=-1){
                    payment[index].isSelected = true
                }
            }

            CartDataInt.cartdata.paymentTypeId =null
            paymentAdapter.addItems(payment)
        }
        Handler(Looper.getMainLooper()).postDelayed({  fragmentcartBinding.expandOption.collapse() },500)
        tipcalulate(tipadd.toInt())
        fragmentcartBinding.tiplay.visibility = View.GONE
        CartDataInt.cartdata.serviceId = service.id
        CartDataInt.cartdata.ccInfo.service = service.id
        CartDataInt.cartdata.srvcFee = service.minFee
        val readytime = (service.readyInMin ?: "0").toLong()
        CartDataInt.cartdata.createdOn = BuilderManager.getCurrentDate(CartDataInt.restrurent.timeZone!!)
        if (CartDataInt.cartdata.timeSelection != null && CartDataInt.cartdata.timeSelection == 0) {
            CartDataInt.cartdata.dueOn = BuilderManager.getReadyTime(
                readytime,
                CartDataInt.restrurent.timeZone!!
            )
        }
        val minorder = service.minOrder
        if (minorder > CartDataInt.cartdata.preTaxAmt) {
            isMinOrder = true
            minAmout = minorder
        } else {
            isMinOrder = false
            minAmout = minorder
        }
        fragmentcartBinding.timetaken.text = getContaxt().getString(R.string.pick_in).plus(" ${service.readyInMin} min")
        fragmentcartBinding.timetaken.visibility = View.VISIBLE
        fragmentcartBinding.delivrytimetaken.visibility = View.GONE
        applyCoupenDiscountWithService(service)

        fragmentcartBinding.deliverylay.visibility = View.GONE
        isChooseDelivery = false
        isChoosePickup = true
        isChooseCurbside = false
        validatePickUp()
    }
    private fun onSelectDelivery(service: Service, coupon: Boolean) {
        CartDataInt.restrurent.paymentTypes?.let { it ->
            val payment = ArrayList<PaymentType>()
            it.forEach { items ->
                service.payment?.forEach {servicepayment->
                    if (servicepayment.id==items.id && !coupon){
                        payment.add(items)
                    }else if (servicepayment.id==items.id && coupon && items.type.equals("POP").not()){
                        payment.add(items)
                    }
                }
            }
            val uniqepayment = payment.distinctBy { it.type }
            payment.clear()
            payment.addAll(uniqepayment)
            if (payment.any { it.isSelected}.not()){
                val  index = payment.indexOfLast {it.type.equals("STAR") }
                if (index!=-1){
                    payment[index].isSelected = true
                }
            }
            CartDataInt.cartdata.paymentTypeId =null
            paymentAdapter.addItems(payment)
        }
        Handler(Looper.getMainLooper()).postDelayed({  fragmentcartBinding.expandOption.collapse() },500)

        fragmentcartBinding.tiplay.visibility = View.GONE
        CartDataInt.cartdata.serviceId = service.id
        CartDataInt.cartdata.ccInfo.service = service.id
        val readytime = (service.readyInMin ?: "0").toLong()
        CartDataInt.cartdata.createdOn = BuilderManager.getCurrentDate(CartDataInt.restrurent.timeZone!!)
        if (CartDataInt.cartdata.timeSelection != null && CartDataInt.cartdata.timeSelection == 0) {
            CartDataInt.cartdata.dueOn = BuilderManager.getReadyTime(
                readytime,
                CartDataInt.restrurent.timeZone!!
            )
        }
        val minorder = service.minOrder
        if (minorder > CartDataInt.cartdata.preTaxAmt) {
            isMinOrder = true
            minAmout = minorder
        } else {
            isMinOrder = false
            minAmout = minorder
        }
        val point = LatLongModel()
        if (CartDataInt.cartdata.deliveryInfo != null) {
            point.lat = CartDataInt.cartdata.deliveryInfo!!.latitude
            point.log = CartDataInt.cartdata.deliveryInfo!!.longitude
        }
        val deliveryzone = service.deliveryZones
        val index = DeliveryZone.getDeliveryZone(point, deliveryzone)
        if (index >= 0) {
            CartDataInt.cartdata.delzoneInd = deliveryzone?.get(index)?.deliveryZoneId
            CartDataInt.cartdata.srvcFee = deliveryzone?.get(index)?.fixedCharge ?: 0.0
            isDelievryOutSide = false
            fragmentcartBinding.delivrytimetaken.text = getContaxt().getString(R.string.delivery_in).plus(" ").plus(service.readyIn)
            fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
            fragmentcartBinding.timetaken.visibility = View.GONE
        } else {
            if (CartDataInt.restrurent.miscMask!![2].toString() == "1") {  //  delivery is not availble out side of deliecry areya
                fragmentcartBinding.delivrytimetaken.text = getContaxt().getString(R.string.delivery_no_available)
                fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                fragmentcartBinding.timetaken.visibility = View.GONE
                isDelievryOutSide = true
            } else {
                if (deliveryzone.isNullOrEmpty().not()) {
                    CartDataInt.cartdata.delzoneInd = deliveryzone!![deliveryzone.size - 1].deliveryZoneId
                    CartDataInt.cartdata.srvcFee = deliveryzone[deliveryzone.size - 1].fixedCharge
                    isDelievryOutSide = false
                    fragmentcartBinding.delivrytimetaken.text =getContaxt().getString(R.string.delivery_in).plus(" ").plus(" ${service.readyInMin} min")
                    fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                    fragmentcartBinding.timetaken.visibility = View.GONE
                } else {

                    CartDataInt.cartdata.srvcFee = max(service.minFee, service.minCharges)

//                    if (service.dynamicFe e != null && service.dynamicFee.equals("T", true)) {
//                        if (CartDataInt.cartdata.preTaxAmt < service.minOrder) {
//                            CartDataInt.cartdata.srvcFee = 0.0
//                        } else if ((service.minOrder <= CartDataInt.cartdata.preTaxAmt) && (CartDataInt.cartdata.preTaxAmt < service.orderAmt1)) {
//                            CartDataInt.cartdata.srvcFee =
//                                service.minFlatFee + CartDataInt.cartdata.preTaxAmt * service.minVarFee
//                        } else if ((service.orderAmt1 <= CartDataInt.cartdata.preTaxAmt) && (CartDataInt.cartdata.preTaxAmt < service.orderAmt2)) {
//                            CartDataInt.cartdata.srvcFee =
//                                service.minFlatFee + CartDataInt.cartdata.preTaxAmt * service.minVarFee
//                        } else if ((service.orderAmt2 <= CartDataInt.cartdata.preTaxAmt) && (CartDataInt.cartdata.preTaxAmt < service.orderAmt3)) {
//                            CartDataInt.cartdata.srvcFee =
//                                service.flatFee2 + CartDataInt.cartdata.preTaxAmt * service.varFee2
//                        } else { /* OrderAmt3 < X */
//                            CartDataInt.cartdata.srvcFee =
//                                service.flatFee3 + CartDataInt.cartdata.preTaxAmt * service.varFee3
//                        }
//                    } else {
//                        CartDataInt.cartdata.srvcFee = max(service.minFee, service.minCharges)
//                    }
                    fragmentcartBinding.delivrytimetaken.text = getContaxt().getString(R.string.delivery_in).plus(" ").plus("${service.readyInMin} min")
                    fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                    fragmentcartBinding.timetaken.visibility = View.GONE
                }

            }
        }
        applyCoupenDiscountWithService(service)

        fragmentcartBinding.deliverylay.visibility = View.VISIBLE
        isChooseDelivery = true
        isChoosePickup = false
        isChooseCurbside = false
        checkdeliveryValidate()
    }
    private fun onSelectOffBaseDelivery(service: Service, coupon: Boolean) {
        CartDataInt.restrurent.paymentTypes?.let { it ->
            val payment = ArrayList<PaymentType>()
            it.forEach { items ->
                service.payment?.forEach {servicepayment->
                    if (servicepayment.id==items.id && !coupon){
                        payment.add(items)
                    }else if (servicepayment.id==items.id && coupon && items.type.equals("POP").not()){
                        payment.add(items)
                    }
                }
            }
            val uniqepayment = payment.distinctBy { it.type }
            payment.clear()
            payment.addAll(uniqepayment)
            if (payment.any { it.isSelected}.not()){
                val  index = payment.indexOfLast {it.type.equals("STAR") }
                if (index!=-1){
                    payment[index].isSelected = true
                }
            }
            CartDataInt.cartdata.paymentTypeId =null
            paymentAdapter.addItems(payment)
        }
        Handler(Looper.getMainLooper()).postDelayed({fragmentcartBinding.expandOption.collapse()},500)
        fragmentcartBinding.tiplay.visibility = View.GONE
        CartDataInt.cartdata.serviceId = service.id
        CartDataInt.cartdata.ccInfo.service = service.id
        val readytime = (service.readyInMin ?: "0").toLong()
        CartDataInt.cartdata.createdOn = BuilderManager.getCurrentDate(CartDataInt.restrurent.timeZone!!)
        if (CartDataInt.cartdata.timeSelection != null && CartDataInt.cartdata.timeSelection == 0) {
            CartDataInt.cartdata.dueOn = BuilderManager.getReadyTime(readytime,CartDataInt.restrurent.timeZone!!)
        }
        val minorder = service.minOrder
        if (minorder > CartDataInt.cartdata.preTaxAmt) {
            isMinOrder = true
            minAmout = minorder
        } else {
            /// User can do  order
            isMinOrder = false
            minAmout = minorder
        }
        val point = LatLongModel()
        if (CartDataInt.cartdata.deliveryInfo != null) {
            point.lat = CartDataInt.cartdata.deliveryInfo!!.latitude
            point.log = CartDataInt.cartdata.deliveryInfo!!.longitude
        }
        val deliveryzone = service.deliveryZones
        val index = DeliveryZone.getDeliveryZone(point, deliveryzone)

        if (index >= 0) {
            CartDataInt.cartdata.delzoneInd = deliveryzone?.get(index)?.deliveryZoneId
            CartDataInt.cartdata.srvcFee = deliveryzone?.get(index)?.fixedCharge ?: 0.0
            isDelievryOutSide = false
            fragmentcartBinding.delivrytimetaken.text =
                getContaxt().getString(R.string.delivery_in).plus(" ").plus(service.readyIn)
            fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
            fragmentcartBinding.timetaken.visibility = View.GONE

        } else {
            if (CartDataInt.restrurent.miscMask!![2].toString() == "1") {  //  delivery is not availble out side of deliecry areya
                fragmentcartBinding.delivrytimetaken.text =
                    getContaxt().getString(R.string.delivery_no_available)
                fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                fragmentcartBinding.timetaken.visibility = View.GONE
                isDelievryOutSide = true
            } else {
                if (deliveryzone.isNullOrEmpty().not()) {
                    CartDataInt.cartdata.delzoneInd =
                        deliveryzone!![deliveryzone.size - 1].deliveryZoneId
                    CartDataInt.cartdata.srvcFee = deliveryzone[deliveryzone.size - 1].fixedCharge
                    isDelievryOutSide = false
                    fragmentcartBinding.delivrytimetaken.text =
                        getContaxt().getString(R.string.delivery_in).plus(" ")
                            .plus("${service.readyInMin} min")
                    fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                    fragmentcartBinding.timetaken.visibility = View.GONE
                } else {
                    CartDataInt.cartdata.srvcFee = max(service.minFee, service.minCharges)

                    fragmentcartBinding.delivrytimetaken.text =
                        getContaxt().getString(R.string.delivery_in).plus(" ")
                            .plus("${service.readyInMin} min")
                    fragmentcartBinding.delivrytimetaken.visibility = View.VISIBLE
                    fragmentcartBinding.timetaken.visibility = View.GONE
                }

            }
        }
        applyCoupenDiscountWithService(service)



//        if (CartDataInt.cartdata.discountAmt != null && CartDataInt.cartdata.discountAmt!! > 0.0) {
//            val y =
//                CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discountType }
//            if (y != -1) {
//
//                CartDataInt.restrurent.discounts[y].service?.let {
//                    if (CartDataInt.restrurent.discounts[y].service!!.serviceId != service.id) {
//                        val autoTransition = AutoTransition()
//                        autoTransition.duration = 100
//                        TransitionManager.beginDelayedTransition(
//                            fragmentcartBinding.couponcontainer,
//                            autoTransition
//                        )
//                        fragmentcartBinding.coupenapplyed.visibility = View.GONE
//                        fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
//                        CartDataInt.cartdata.discountAmt = null
//                        CartDataInt.cartdata.discountType = null
//                        calculateTotal()
//                    }
//
//                }
//            }
//        }
//        else if (CartDataInt.cartdata.discount1Amt != null && CartDataInt.cartdata.discount1Amt!! > 0.0) {
//            val y =
//                CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discount1Type }
//            if (y != -1) {
//
//                CartDataInt.restrurent.discounts[y].service?.let {
//                    if (CartDataInt.restrurent.discounts[y].service!!.serviceId != service.id) {
//                        val autoTransition = AutoTransition()
//                        autoTransition.duration = 100
//                        TransitionManager.beginDelayedTransition(
//                            fragmentcartBinding.couponcontainer,
//                            autoTransition
//                        )
//                        fragmentcartBinding.coupenapplyed.visibility = View.GONE
//                        fragmentcartBinding.Gocoupenapply.visibility = View.VISIBLE
//                        CartDataInt.cartdata.discount1Amt = null
//                        CartDataInt.cartdata.discount1Type = null
//                        calculateTotal()
//                    }
//
//                }
//            }
//        }
        fragmentcartBinding.deliverylay.visibility = View.VISIBLE
        isChooseDelivery = true
        isChoosePickup = false
        isChooseCurbside = false
        checkdeliveryValidate()
    }


    private fun calculateTotal(){
        if(CartDataInt.cartdata.itemList.isNotEmpty()) {
            var amount = 0.0
            for (x in 0 until CartDataInt.cartdata.itemList.size)
            {
                var amountvalue = 0.0
                if(!CartDataInt.cartdata.itemList[x].havespicialoffer) {
                    amountvalue +=  CartDataInt.cartdata.itemList[x].unitPrice
                    CartDataInt.cartdata.itemList[x].itemAddOnList.forEach { addonItmes->
                        addonItmes.addOnOptions.forEach { addonOption->
                            amountvalue += addonOption.amt
                        }
                    }
                    amount += amountvalue.times(CartDataInt.cartdata.itemList[x].qty)
                }
                else {
                    if(CartDataInt.cartdata.itemList.size == 1){
                        CartDataInt.cartdata.itemList.removeAt(x)
                        calculateTotal()
                    }
                    else if (getItemsTotel()<CartDataInt.cartdata.itemList[x].havespicialofferAmount)
                    {
                        CartDataInt.cartdata.itemList.removeAt(x)
                        calculateTotal()
                    }
                }

            }
            CartDataInt.cartdata.preTaxAmt = amount
            fragmentcartBinding.totalamt.text = BuilderManager.getFormat().format(CartDataInt.cartdata.preTaxAmt)
            cartfragmentViewModel.saveData(true)
            updateTotal()

        }
        else{
            CartDataInt.cartdata.preTaxAmt = 0.0
            fragmentcartBinding.totalamt.text = BuilderManager.getFormat().format(0)
            updateTotal()
            fragmentcartBinding.cardview.visibility = View.GONE
            fragmentcartBinding.data.visibility = View.VISIBLE
            CartDataInt.orderdata = OrderDataModel()
            CartDataInt.cartdata = CartData()
            CartDataInt.suggestion = Suggestion()
            CartDataInt.restrurent = RestrurentModel()
            CartDataInt.menudata = MenuModel()
            cartfragmentViewModel.saveData(false)
        }
    }
    private fun getItemsTotel() : Double{
        var amount = 0.0
        if(CartDataInt.cartdata.itemList.isNotEmpty()) {
            for (x in 0 until  CartDataInt.cartdata.itemList.size)
            {
                if(! CartDataInt.cartdata.itemList[x].havespicialoffer) {
                    amount += CartDataInt.cartdata.itemList[x].amt
                }
            }
        }
        return amount
    }
    @SuppressLint("SetTextI18n")
    fun updateTotal(){
        fragmentcartBinding.applieddiscLay.visibility = View.GONE
        fragmentcartBinding.totalamt.text = BuilderManager.getFormat().format(CartDataInt.cartdata.preTaxAmt)
        fragmentcartBinding.itemstotel.text = BuilderManager.getFormat().format(CartDataInt.cartdata.preTaxAmt)
        fragmentcartBinding.gstapplied.text = BuilderManager.getFormat().format((CartDataInt.restrurent.tax * CartDataInt.cartdata.preTaxAmt))
        fragmentcartBinding.deliverycharge.text = BuilderManager.getFormat().format(CartDataInt.cartdata.srvcFee)
        var gradtotel = 0.0
        var tipamount = 0.0

        if (CartDataInt.cartdata.discountAmt!=null){
            var couponAmtdisamount =0.0
            var pretaxamount =0.0
            var discountAmt = 0.0
            discountAmt = min(CartDataInt.cartdata.discountAmt?:0.0,CartDataInt.cartdata.preTaxAmt)

            fragmentcartBinding.applieddiscLay.visibility = View.VISIBLE
            fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discountAmt?:0.0)}"


            if(CartDataInt.cartdata.couponAmt!=null) {
                couponAmtdisamount = min(CartDataInt.cartdata.couponAmt?:0.0,CartDataInt.cartdata.preTaxAmt)

                fragmentcartBinding.copendiscount.text = if ((CartDataInt.cartdata.couponAmt?:0.0)==0.0){
                        "${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"

                    }else{
                        "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"

                    }


                if (CartDataInt.cartdata.couponType ==0){
                    pretaxamount = CartDataInt.cartdata.preTaxAmt -couponAmtdisamount
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * pretaxamount)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }

                }
                else{
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    pretaxamount = CartDataInt.cartdata.preTaxAmt-couponAmtdisamount
                }
            }
            else{
                pretaxamount =  CartDataInt.cartdata.preTaxAmt
                fragmentcartBinding.copendiscount.text =
                    if ((CartDataInt.cartdata.couponAmt?:0.0)==0.0){
                          "${BuilderManager.getFormat().format(
                        CartDataInt.cartdata.couponAmt?:0.0)}"
                    }else{
                          "- ${BuilderManager.getFormat().format(
                        CartDataInt.cartdata.couponAmt?:0.0)}"
                    }
            }

            val x = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discountType }
            if (x != -1) {
                if(CartDataInt.restrurent.discounts[x].preTax.equals("T")){
                    CartDataInt.cartdata.discountAmt = getCoupenPreTaxAmount(pretaxamount,CartDataInt.restrurent.discounts[x])
                    discountAmt = min(CartDataInt.cartdata.discountAmt?:0.0,CartDataInt.cartdata.preTaxAmt)
                    fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discountAmt?:0.0)}"
                    val pretaxamount1 = pretaxamount - discountAmt
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * pretaxamount1)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    gradtotel = tipamount + pretaxamount1 + (CartDataInt.restrurent.tax * pretaxamount1) + CartDataInt.cartdata.srvcFee
                    if (gradtotel<0.0){
                        gradtotel =0.0
                    }
                    fragmentcartBinding.gstapplied.text = BuilderManager.getFormat().format((CartDataInt.restrurent.tax * pretaxamount1))
                    CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * pretaxamount1)

                }
                else{

                    CartDataInt.cartdata.discountAmt = getCoupenPostTaxAmount(CartDataInt.cartdata,CartDataInt.restrurent.discounts[x],pretaxamount)
                    discountAmt = min(CartDataInt.cartdata.discountAmt?:0.0,CartDataInt.cartdata.preTaxAmt)
                    fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discountAmt?:0.0)}"

                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    gradtotel = tipamount + pretaxamount + (CartDataInt.restrurent.tax * pretaxamount) + CartDataInt.cartdata.srvcFee - discountAmt
                    if (gradtotel<0.0){
                        gradtotel =0.0
                    }
                    CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * pretaxamount)
                }
            }

        }

        else if (CartDataInt.cartdata.discount1Amt!=null){
            var couponAmtdisamount =0.0
            var pretaxamount = 0.0
            var discountAmt = 0.0
            discountAmt = min(CartDataInt.cartdata.discount1Amt?:0.0,CartDataInt.cartdata.preTaxAmt)
            fragmentcartBinding.applieddiscLay.visibility = View.VISIBLE
            fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discount1Amt?:0.0)}"


            if(CartDataInt.cartdata.couponAmt!=null) {
                couponAmtdisamount = min(CartDataInt.cartdata.couponAmt?:0.0,CartDataInt.cartdata.preTaxAmt)
                fragmentcartBinding.copendiscount.text =

                    if ((CartDataInt.cartdata.couponAmt?:0.0)==0.0){
                        "${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                    }else{
                        "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                    }

                if (CartDataInt.cartdata.couponType ==0){
                    pretaxamount = CartDataInt.cartdata.preTaxAmt -couponAmtdisamount
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * pretaxamount)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }

                }
                else{
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    pretaxamount = CartDataInt.cartdata.preTaxAmt-couponAmtdisamount
                }
            }else{
                fragmentcartBinding.copendiscount.text = if((CartDataInt.cartdata.couponAmt?:0.0)==0.0){
                    "${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                }else{
                    "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                }
                pretaxamount =  CartDataInt.cartdata.preTaxAmt
            }






            val x = CartDataInt.restrurent.discounts.indexOfFirst { it.id == CartDataInt.cartdata.discount1Type }
            if (x != -1) {
                if(CartDataInt.restrurent.discounts[x].preTax.equals("T")){
                    CartDataInt.cartdata.discount1Amt = getCoupenPreTaxAmount(pretaxamount,CartDataInt.restrurent.discounts[x])
                    discountAmt = min(CartDataInt.cartdata.discount1Amt?:0.0,CartDataInt.cartdata.preTaxAmt)
                    fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discount1Amt?:0.0)}"
                    val pretaxamount = pretaxamount - discountAmt
                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * pretaxamount)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    gradtotel = tipamount + pretaxamount + (CartDataInt.restrurent.tax * pretaxamount) + CartDataInt.cartdata.srvcFee
                    if (gradtotel<0.0){
                        gradtotel =0.0
                    }
                    fragmentcartBinding.gstapplied.text = BuilderManager.getFormat().format((CartDataInt.restrurent.tax * pretaxamount))
                    CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * pretaxamount)

                }
                else{
                    CartDataInt.cartdata.discount1Amt = getCoupenPostTaxAmount(CartDataInt.cartdata,CartDataInt.restrurent.discounts[x],pretaxamount)
                    discountAmt = min(CartDataInt.cartdata.discount1Amt?:0.0,CartDataInt.cartdata.preTaxAmt)
                    fragmentcartBinding.discount.text = "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.discount1Amt?:0.0)}"

                    cartfragmentViewModel.getCountryCode().let {
                        tipamount = if (!it.equals("IN",true)){
                            (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                        }else{
                            tipadd.toDouble()
                        }
                    }
                    gradtotel = tipamount.plus(pretaxamount).plus((CartDataInt.restrurent.tax * pretaxamount)) + CartDataInt.cartdata.srvcFee - discountAmt
                    if (gradtotel<0.0){
                        gradtotel =0.0
                    }
                    CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * pretaxamount)
                }
            }

        }

        else if(CartDataInt.cartdata.couponAmt!=null) {
            val couponAmt = min(CartDataInt.cartdata.couponAmt?:0.0,CartDataInt.cartdata.preTaxAmt)
            fragmentcartBinding.copendiscount.text =

                if ((CartDataInt.cartdata.couponAmt?:0.0)==0.0){
                    "${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                }else{
                    "- ${BuilderManager.getFormat().format(CartDataInt.cartdata.couponAmt?:0.0)}"
                }

            if (CartDataInt.cartdata.couponType ==0){
                val pretaxamount = CartDataInt.cartdata.preTaxAmt -couponAmt
                cartfragmentViewModel.getCountryCode().let {
                    tipamount = if (!it.equals("IN",true)){
                        (tipadd.toDouble() * pretaxamount)/100
                    }else{
                        tipadd.toDouble()
                    }
                }

                gradtotel = tipamount + pretaxamount + (CartDataInt.restrurent.tax * pretaxamount) + CartDataInt.cartdata.srvcFee
                if (gradtotel<0.0){
                    gradtotel =0.0
                }
                fragmentcartBinding.gstapplied.text = BuilderManager.getFormat().format((CartDataInt.restrurent.tax * pretaxamount))
                CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * pretaxamount)
            }
            else{
                cartfragmentViewModel.getCountryCode().let {
                    tipamount = if (!it.equals("IN",true)){
                        (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                    }else{
                        tipadd.toDouble()
                    }
                }
                gradtotel = tipamount + CartDataInt.cartdata.preTaxAmt + (CartDataInt.restrurent.tax * CartDataInt.cartdata.preTaxAmt) + CartDataInt.cartdata.srvcFee - couponAmt
                if (gradtotel<0.0){
                    gradtotel =0.0
                }
                CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * CartDataInt.cartdata.preTaxAmt)
            }
        }
        else{
            CartDataInt.cartdata.taxAmt = (CartDataInt.restrurent.tax * CartDataInt.cartdata.preTaxAmt)
            fragmentcartBinding.copendiscount.text = "${BuilderManager.getFormat().format(0.0)}"

            cartfragmentViewModel.getCountryCode().let {
                tipamount = if (!it.equals("IN",true)){
                    (tipadd.toDouble() * CartDataInt.cartdata.preTaxAmt)/100
                }else{
                    tipadd.toDouble()
                }
            }
            gradtotel = tipamount + CartDataInt.cartdata.preTaxAmt + (CartDataInt.restrurent.tax * CartDataInt.cartdata.preTaxAmt) + CartDataInt.cartdata.srvcFee
            if (gradtotel<0.0){
                gradtotel =0.0
            }
        }
        fragmentcartBinding.tipadd.text = BuilderManager.getFormat().format(tipamount)
        fragmentcartBinding.gradtotle.text = BuilderManager.getFormat().format(gradtotel)
        fragmentcartBinding.submittotle.text = BuilderManager.getFormat().format(gradtotel)
        CartDataInt.cartdata.totalAmt = gradtotel
        CartDataInt.cartdata.tipAmt = tipamount.toLong()
    }


    override fun showFeedbackMessage(message: String) {
        showBaseToast(fragmentcartBinding.root,message)
        fragmentcartBinding.buttonSubmit.isEnabled = true
    }
    private fun checkdeliveryValidate(): Boolean {
        var valid = false
        if(checkResturentDeliveryValidate()){
            if(CartDataInt.cartdata.paymentTypeId != null) {
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_green)
                valid = true
            }
            else{
                message = "Please choose payment method"
                titel = "Oops!!"
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
            }
        }else{
            message =  checkResturentDeliveryValidateMessage()
            fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
//            BuilderManager.AlertMessage(mContext,titel,message,"OKAY",object :OkButton {
//                override fun onClickOkey() {}})
        }
        return valid
    }
    private fun checkResturentDeliveryValidateMessage(): String  {
        val isOpen = BuilderManager.isResturentOpen(
            CartDataInt.restrurent.schedule!!,
            CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
            CartDataInt.restrurent.timeZone!!
        )
        if(!isOpen && CartDataInt.cartdata.timeSelection==0){
            message = "Sorry !! We are currently Closed"
            titel = "Restaurant Closed"
        }
        if(BuilderManager.isDeliveryTime().not() && CartDataInt.cartdata.timeSelection==0){
            message = "Sorry !! Delivery is not available"// ${BuilderManager.getDeliveryTime()}"
            titel = "Oops!!"
        }
        if(isMinOrder){
            message ="Minimun order for this restaurant is ${BuilderManager.getFormat().format(minAmout)}"
            titel = "Oops!!"
        }
        if(isDelievryOutSide){
            message = "Your delivery address is outside our delivery zones. Currently we are not able to provide service to these areas. Please change the address or select another order type."
            titel ="Oops!!"
        }
        if(CartDataInt.cartdata.deliveryInfo==null){
            message = "Please add your delivery address."
            titel ="Oops!!"
        }


        return message
    }


    private fun checkResturentDeliveryValidate(): Boolean {
        var valid = true
        fragmentcartBinding.submitbtn.visibility = View.VISIBLE
        val isOpen = BuilderManager.isResturentOpen(
            CartDataInt.restrurent.schedule!!,
            CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
            CartDataInt.restrurent.timeZone!!
        )
        if(!isOpen && CartDataInt.cartdata.timeSelection==0){
            valid = false
            fragmentcartBinding.submitbtn.visibility = View.GONE
        }
        if(BuilderManager.isDeliveryTime().not() && CartDataInt.cartdata.timeSelection==0){
            valid = false
        }
        if(isMinOrder){
            valid = false
        }

        if(isDelievryOutSide){
            valid = false
        }

        if(CartDataInt.cartdata.deliveryInfo==null){
            valid = false
        }
        return valid
    }

    private fun validatePickUp(): Boolean {
        var valid = false
        if(checkResturentPIckupValidate()){
            if (CartDataInt.cartdata.paymentTypeId != null)
            {
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_green)
                valid =  true
            } else{
                message = "Please choose payment method"
                titel = "Oops!!"
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
            }

        }else{
            message =  checkResturentPicupValidateMessage()
            fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
//            BuilderManager.AlertMessage(mContext,titel,message,"OKAY",object :
//                OkButton {
//                override fun onClickOkey() {}})
        }
        return valid
    }
    private fun checkResturentPicupValidateMessage(): String  {
        val isOpen = BuilderManager.isResturentOpen(
            CartDataInt.restrurent.schedule!!,
            CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
            CartDataInt.restrurent.timeZone!!
        )
        if(!isOpen && CartDataInt.cartdata.timeSelection==0){
            message = "Sorry !! We are currently Closed"
            titel = "Restaurant Closed"
        }
        if(!BuilderManager.isPickupTime() && CartDataInt.cartdata.timeSelection!=null&&CartDataInt.cartdata.timeSelection==0){
            message = "Sorry !! PickUp is not available" //${BuilderManager.getDeliveryTime()}"
            titel = "Oops!!"
        }
        if(isMinOrder){
            message ="Minimun Order for this restaurant is ${BuilderManager.getFormat().format(minAmout)}"
            titel = "Oops!!"
        }
        return message
    }


    private fun checkResturentPIckupValidate(): Boolean {
        var valid = true
        fragmentcartBinding.submitbtn.visibility = View.VISIBLE
        val isOpen = BuilderManager.isResturentOpen(
            CartDataInt.restrurent.schedule!!,
            CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
            CartDataInt.restrurent.timeZone!!
        )
        if(!isOpen && CartDataInt.cartdata.timeSelection==0){
            valid = false
            fragmentcartBinding.submitbtn.visibility = View.GONE
        }
        if(!BuilderManager.isPickupTime() && CartDataInt.cartdata.timeSelection!=null&&CartDataInt.cartdata.timeSelection==0){
            valid = false
        }
        if(isMinOrder){
            valid = false
        }
        return valid
    }

    private fun checkResturentCurbsideValidate(): Boolean {
        var valid = true
        fragmentcartBinding.submitbtn.visibility = View.VISIBLE
        val isOpen = BuilderManager.isResturentOpen(
            CartDataInt.restrurent.schedule!!,
            CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
            CartDataInt.restrurent.timeZone!!
        )
        if(!isOpen && CartDataInt.cartdata.timeSelection==0){
            valid = false
            fragmentcartBinding.submitbtn.visibility = View.GONE
        }
        if(isMinOrder){
            valid = false
        }
        CartDataInt.cartdata.carplatenumber =fragmentcartBinding.licanceplate.text.toString()
        CartDataInt.cartdata.carcolor =fragmentcartBinding.colorofcar.text.toString()
        CartDataInt.cartdata.carmake =fragmentcartBinding.makerofcar.text.toString()
        CartDataInt.cartdata.carmodel =fragmentcartBinding.modelofcar.text.toString()
        return valid
    }

    private fun checkcurbsideValidate(): Boolean {
        var valid = false
        if(checkResturentCurbsideValidate()){
            if(CartDataInt.cartdata.paymentTypeId != null) {
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_green)
                valid = true
            }
            else{
                message = "Please choose payment method"
                titel = "Oops!!"
                fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
            }
        }else{
            message =  checkResturentCurbsideValidateMessage()
            fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
//            BuilderManager.AlertMessage(mContext,titel,message,"OKAY",object :OkButton {
//                override fun onClickOkey() {}})
        }
        return valid
    }

    private fun checkResturentCurbsideValidateMessage(): String  {
        val isOpen = BuilderManager.isResturentOpen(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!, CartDataInt.restrurent.timeZone!!)
        if(!isOpen){
            message = "Sorry !! We are currently Closed"
            titel = "Restaurant Closed"
        }
        if(isMinOrder){
            message ="Minimun Order for this restaurant is ${BuilderManager.getFormat().format(minAmout)}"
            titel = "Oops!!"
        }
        return message
    }

    private fun validateCategory(): Boolean{
        var validate = true
        if (CartDataInt.cartdata.dueOn==null){
            validate = false
            fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
        }else{
            for(i in 0 until CartDataInt.cartdata.itemList.size){
                val catagoury =   CartDataInt.menudata.catList?.find { it.catId == CartDataInt.cartdata.itemList[i].categoryId}
                if (BuilderManager.isAvailableCatagoury(catagoury,CartDataInt.restrurent.timeZone!!,CartDataInt.cartdata.dueOn).not()){
                    validate = false
                    fragmentcartBinding.submitbtn.setBackgroundResource(R.drawable.button_rec_shape_gradian_gray)
                    val available = StringBuilder()
                    available.append(BuilderManager.getFormateTime(catagoury?.openTime))
                    available.append(" -to- ")
                    available.append(BuilderManager.getFormateTime(catagoury?.closeTime))
                    message =  "${CartDataInt.cartdata.itemList[i].name} Item from ${catagoury?.catName} category is only available between\n" +
                            "$available.\n" +
                            " \nPlease choose a different order time.\n"


                    titel = "Not Available"

                }
            }
        }
        return  validate
    }

    private fun checkServiceValidate(): Boolean {
        var valid = false
        CartDataInt.cartdata.SpecialInstructions = fragmentcartBinding.sepialInstruction.text.toString()
        when {
            isChooseDelivery -> valid =  checkdeliveryValidate()
            isChoosePickup -> valid =  validatePickUp()
            isChooseCurbside->valid = checkcurbsideValidate()
            else -> {
                message = "Please Choose PickUp/Delivery Option."
                titel = "Oops!!"
            }
        }
        if (valid && validateCategory().not()){
            valid =  false
        }

        return valid
    }

}