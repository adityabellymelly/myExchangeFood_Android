package com.exchange.user.module.schedule_order_module

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.R
import com.exchange.user.databinding.ActivityScheduleOrderBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_command.CartDataInt.Companion.cartdata
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.utility_module.BuilderManager
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class ScheduleOrderActivity : BaseActivity<ActivityScheduleOrderBinding, ScheduleViewModel>(),ScheduleNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var scheduleviewModel: ScheduleViewModel
    private lateinit var activityscheduleBinding : ActivityScheduleOrderBinding
    private val calander = Calendar.getInstance()
    private val ONE_MINUTE_IN_MILLIS = 60000
    var opentime : LocalDateTime?= null
    var closetime : LocalDateTime? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_schedule_order
    }
    override fun getViewModel(): ScheduleViewModel {
        scheduleviewModel = ViewModelProvider(this, factory).get(ScheduleViewModel::class.java)
        return scheduleviewModel
    }
    override fun getBindingVariable(): Int {
        return androidx.databinding.library.baseAdapters.BR.countryViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityscheduleBinding = getViewDataBinding()
        scheduleviewModel.setNavigator(this)
        initData()
    }

    override fun initData() {
        val current = Calendar.getInstance(BuilderManager.getTimeZone()!!).time
        val t = current.time
        val date = Date(t + (CartDataInt.restrurent.timeZone!!.hoursDifference *
                CartDataInt.restrurent.timeZone!!.minutesDifference * ONE_MINUTE_IN_MILLIS))
        activityscheduleBinding.calanderView.minDate = date.time + (24 * 60 * 60 * 1000)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.DAY_OF_YEAR, 7)
        activityscheduleBinding.calanderView.maxDate = maxDate.timeInMillis

       val service = CartDataInt.restrurent.services?.find { it.isSelected }
        service?.let { availableService ->
            when {
                availableService.name.equals("Pickup") -> {
                    opentime =
                        BuilderManager.getPickupOpenTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                    closetime =
                        BuilderManager.getPickupCloseTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                }
                availableService.name.equals("Curbside") -> {
                    opentime =
                        BuilderManager.getPickupOpenTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                    closetime =
                        BuilderManager.getPickupCloseTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                }
                availableService.name.equals("Off-base Delivery") -> {
                    opentime =
                        BuilderManager.getDeliveryOpenTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                    closetime = BuilderManager.getDeliveryCloseTime(
                        CartDataInt.restrurent.schedule!!,
                        CartDataInt.restrurent.timeZone!!
                    )
                }
                availableService.name.equals("On-base Delivery") -> {
                    opentime =
                        BuilderManager.getDeliveryOpenTime(CartDataInt.restrurent.schedule!!, CartDataInt.restrurent.timeZone!!)
                    closetime = BuilderManager.getDeliveryCloseTime(
                        CartDataInt.restrurent.schedule!!,
                        CartDataInt.restrurent.timeZone!!
                    )
                }
            }

        }

        CartDataInt.restrurent.schedule?.let {
           val  isOpen = BuilderManager.isResturentOpen(it,
                CartDataInt.restrurent.calendar!!.openOrCloseYearMask!!,
                CartDataInt.restrurent.timeZone!!
            )
            CartDataInt.restrurent.menus?.let {
                val menudata =
                    it.find { ((it.id ?: "0").toLong()) == (CartDataInt.cartdata.menuId ?: 0).toLong() }
                if (menudata?.asap.equals("T") && isOpen) {
                    if (isOpen) {
                        onClickAsap(activityscheduleBinding, this,CartDataInt.cartdata)
                        activityscheduleBinding.asaplay.visibility = View.VISIBLE
                    } else {
                        onClickToday(activityscheduleBinding, this,CartDataInt. cartdata)
                        activityscheduleBinding.asaplay.visibility = View.GONE
                    }
                } else {
                    onClickToday(activityscheduleBinding, this, CartDataInt.cartdata)
                    activityscheduleBinding.asaplay.visibility = View.GONE
                }

                if (menudata?.lt.equals("T") && isOpen) {
                    activityscheduleBinding.todaylay.visibility = View.VISIBLE
                    val adapter = ArrayAdapter<String>(
                        this,
                        R.layout.spinner_list_item,
                        BuilderManager.getTimeSet(
                            isOpen,
                            true,
                            CartDataInt.restrurent.timeZone!!,
                            opentime,
                            closetime
                        )
                    )
                    activityscheduleBinding.selectTimeSpbinner.adapter = adapter
                } else {
                    onClickToday(activityscheduleBinding, this,CartDataInt. cartdata)
                    activityscheduleBinding.todaylay.visibility = View.GONE
                }

                if (menudata?.fo.equals("T") && isOpen) {
                    activityscheduleBinding.laterlay.visibility = View.VISIBLE
                    val adapter = ArrayAdapter<String>(
                        this,
                        R.layout.spinner_list_item,
                        BuilderManager.getTimeSet(
                            isOpen,
                            false,
                            CartDataInt.restrurent.timeZone!!,
                            opentime,
                            closetime
                        )
                    )
                    activityscheduleBinding.laterselectTimeSpbinner.adapter = adapter
                } else if (menudata?.fo.equals("T") && !isOpen) {
                    activityscheduleBinding.laterlay.visibility = View.VISIBLE
                    val adapter = ArrayAdapter<String>(
                        this,
                        R.layout.spinner_list_item,
                        BuilderManager.getTimeSet(
                            isOpen,
                            false,
                            CartDataInt. restrurent.timeZone!!,
                            opentime,
                            closetime
                        )
                    )
                    activityscheduleBinding.laterselectTimeSpbinner.adapter = adapter
                    onClickLater(activityscheduleBinding, this,CartDataInt.cartdata)
                } else {
                    activityscheduleBinding.laterlay.visibility = View.GONE
                }

            }
        }


        activityscheduleBinding.calanderView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calander.set(Calendar.YEAR, year)
            calander.set(Calendar.MONTH, month)
            calander.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val time = activityscheduleBinding.laterselectTimeSpbinner.selectedItem as String
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault())
            val localtime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
            val localDate = LocalDate.of(
                calander.get(Calendar.YEAR),
                (calander.get(Calendar.MONTH) + 1),
                calander.get(Calendar.DAY_OF_MONTH)
            )
            val localdatetime = LocalDateTime.of(localDate, localtime)
            //.plusHours(restrurent.timeZone!!.hoursDifference).plusMinutes(restrurent.timeZone!!.minutesDifference)

            val formatted = localdatetime.format(formatter)
            cartdata.dueOn = formatted
            Log.e("Later Time selected", formatted)

        }


        activityscheduleBinding.laterselectTimeSpbinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val time = activityscheduleBinding.laterselectTimeSpbinner.selectedItem as String
                time.toUpperCase(Locale.US)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneId.systemDefault())
                val localtime =
                    LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
                val localDate = LocalDate.of(
                    calander.get(Calendar.YEAR),
                    (calander.get(Calendar.MONTH) + 1),
                    calander.get(Calendar.DAY_OF_MONTH)
                )
                val localdatetime = LocalDateTime.of(localDate, localtime)
                // .plusHours(restrurent.timeZone!!.hoursDifference).plusMinutes(restrurent.timeZone!!.minutesDifference)


                val formatted = localdatetime.format(formatter)
                cartdata.dueOn = formatted

                Log.e("Later Time selected", formatted)
            }

        }



        activityscheduleBinding.selectTimeSpbinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val time = activityscheduleBinding.selectTimeSpbinner.selectedItem as String
                time.toUpperCase(Locale.US)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault())
                val localtime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
                val localdatetime = LocalDateTime.now(ZoneId.of(BuilderManager.getTimeZone()?.id))
                    .plusHours(CartDataInt.restrurent.timeZone!!.hoursDifference)
                    .plusMinutes(CartDataInt.restrurent.timeZone!!.minutesDifference)
                    .withHour(localtime.hour).withMinute(localtime.minute)
                val formatted = localdatetime.format(formatter)
                cartdata.dueOn = formatted
                Log.e("Time selected", time)

            }

        }

    }

    override fun onASAP() {
        onClickAsap(activityscheduleBinding,this,CartDataInt.cartdata)
    }

    override fun onTodayLate() {
        onClickToday(activityscheduleBinding,this,CartDataInt.cartdata)
    }

    override fun onFuture() {
        onClickLater(activityscheduleBinding,this,CartDataInt.cartdata)
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(activityscheduleBinding.root,message)
    }

    override fun onSelectTime() {
        finish()
    }

    override fun onBack() {
        onBackPressed()
    }


    private fun onClickToday(
        activityscheduleBinding: ActivityScheduleOrderBinding,
        context: Context,
        cartdata: CartData
    ) {
        activityscheduleBinding.todaylay.isSelected = true
        activityscheduleBinding.todaytext.isSelected = true
        activityscheduleBinding.asaptext.isSelected = false
        activityscheduleBinding.asaplay.isSelected = false
        activityscheduleBinding.laterlay.isSelected = false
        activityscheduleBinding.latertext.isSelected = false

        activityscheduleBinding.todaytext.setTypeface(
            activityscheduleBinding.todaytext.typeface,
            Typeface.BOLD
        )
        activityscheduleBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white_color
            )
        )
        activityscheduleBinding.asaptext.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
        activityscheduleBinding.asaptext.setTypeface(
            activityscheduleBinding.asaptext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        activityscheduleBinding.latertext.setTypeface(
            activityscheduleBinding.latertext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.timeselectedlay.visibility = View.VISIBLE
        activityscheduleBinding.latertimeselectedlay.visibility = View.GONE
        cartdata.timeSelection = 1
    }

    private fun onClickAsap(
        activityscheduleBinding: ActivityScheduleOrderBinding,
        context: Context,
        cartdata: CartData
    ) {
        activityscheduleBinding.todaylay.isSelected = false
        activityscheduleBinding.todaytext.isSelected = false
        activityscheduleBinding.asaptext.isSelected = true
        activityscheduleBinding.asaplay.isSelected = true
        activityscheduleBinding.laterlay.isSelected = false
        activityscheduleBinding.latertext.isSelected = false

        activityscheduleBinding.asaptext.setTypeface(activityscheduleBinding.asaptext.typeface, Typeface.BOLD)
        activityscheduleBinding.asaptext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white_color
            )
        )
        activityscheduleBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        activityscheduleBinding.todaytext.setTypeface(
            activityscheduleBinding.todaytext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        activityscheduleBinding.latertext.setTypeface(
            activityscheduleBinding.latertext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.timeselectedlay.visibility = View.GONE
        activityscheduleBinding.latertimeselectedlay.visibility = View.GONE
        cartdata.timeSelection = 0
    }


    private fun onClickLater(
        activityscheduleBinding: ActivityScheduleOrderBinding,
        context: Context,
        cartdata: CartData
    ) {
        activityscheduleBinding.todaylay.isSelected = false
        activityscheduleBinding.todaytext.isSelected = false
        activityscheduleBinding.asaptext.isSelected = false
        activityscheduleBinding.asaplay.isSelected = false
        activityscheduleBinding.laterlay.isSelected = true
        activityscheduleBinding.latertext.isSelected = true

        activityscheduleBinding.asaptext.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
        activityscheduleBinding.asaptext.setTypeface(
            activityscheduleBinding.asaptext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        activityscheduleBinding.todaytext.setTypeface(
            activityscheduleBinding.todaytext.typeface,
            Typeface.NORMAL
        )
        activityscheduleBinding.latertext.setTypeface(
            activityscheduleBinding.todaytext.typeface,
            Typeface.BOLD
        )
        activityscheduleBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white_color
            )
        )
        activityscheduleBinding.timeselectedlay.visibility = View.GONE
        activityscheduleBinding.latertimeselectedlay.visibility = View.VISIBLE
        cartdata.timeSelection = 2

    }


    override fun onBackPressed() {
        super.onBackPressed()
        CartDataInt.cartdata.timeSelection = null
    }


}