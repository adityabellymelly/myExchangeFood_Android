package com.exchange.user.module.utility_module
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.exchange.user.R
import com.exchange.user.databinding.*
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.cart_module.model.card_model.AddOnOptionModifier
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.country_module.adapter.LocationAdapter
import com.exchange.user.module.country_module.model.Country
import com.exchange.user.module.country_module.model.CountryModel
import com.exchange.user.module.country_module.model.location_model.RegionList
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Coupon
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Schedule
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.Service
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnOptionModifier1
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.ItemList
import com.exchange.user.module.signin_module.model.SignInRequest
import com.exchange.user.module.utility_module.dialog_commands.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONArray
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import uk.co.senab.photoview.PhotoView
import java.io.UnsupportedEncodingException
import java.text.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.math.roundToInt

object BuilderManager {
    var openclosetime = ""
    var deliverytime = ""
    var pickuptime = ""
    var IsdeliveryTime = false
    var IsPickupTime = false
    private var opendateTime: LocalDateTime? = null
    private var closedateTime: LocalDateTime? = null
    private var opentime: LocalTime? = null
    private var closetime: LocalTime? = null


    fun intilizeAll(){
        openclosetime = ""
        deliverytime = ""
        pickuptime = ""
        IsdeliveryTime = false
        IsPickupTime = false
        opendateTime= null
        closedateTime= null
        opentime= null
        closetime  = null

    }

    fun getTimeZone(): TimeZone? {
        val time  =  TimeZone.getTimeZone("America/Chicago")
        return time
       // return TimeZone.getTimeZone("America/Jamaica")
    }

    val mCalender: Calendar by lazy { Calendar.getInstance() }
    @SuppressLint("CheckResult")
    fun loadImage(view: AppCompatImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(view)
        }
    }

    @SuppressLint("CheckResult")
    fun loadCountryImage(view: AppCompatImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load(ConstantCommand.COUNTRY_IMAGE_BASE_URL.plus(imageUrl))
                .into(view)
        }
    }

    @SuppressLint("CheckResult")
    fun LoadImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.fonzie_the_burger)
            requestOptions.transform(RoundedCorners(12))
            requestOptions.error(R.drawable.fonzie_the_burger)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load(ConstantCommand.IMAGE_BASE_URL.plus(imageUrl))
                .into(view)
        }
    }

    @SuppressLint("CheckResult")
    fun LoadImageWithOutBaseUrl(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.fonzie_the_burger)
            requestOptions.transform(RoundedCorners(12))
            requestOptions.error(R.drawable.fonzie_the_burger)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(view)
        }
    }


    @SuppressLint("CheckResult")
    fun ItemLoadImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.fonzie_the_burger)
            requestOptions.transform(RoundedCorners(12))
            requestOptions.error(R.drawable.fonzie_the_burger)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load("https://s3.amazonaws.com/v1.0/images/menuitemimages/".plus(imageUrl))
                .into(view)
        }
    }

    @SuppressLint("CheckResult")
    fun ItemLoadImage(view: PhotoView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.fonzie_the_burger)
            requestOptions.transform(RoundedCorners(12))
            requestOptions.error(R.drawable.fonzie_the_burger)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load("https://s3.amazonaws.com/v1.0/images/menuitemimages/".plus(imageUrl))
                .into(view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                .plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            formatted
        } else {
            val ONE_MINUTE_IN_MILLIS = 60000
            val current = Calendar.getInstance(getTimeZone()!!).time
            val t = current.time
            val date =
                Date(t + (timeZone.hoursDifference * timeZone.minutesDifference * ONE_MINUTE_IN_MILLIS))
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = formatter.format(date)
            formatted
        }
    }

    fun  getDate (timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone): Date? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                .plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)

            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss")
            val date = getDateFromString(current.format(formatter))
            date

        }else{
            val ONE_MINUTE_IN_MILLIS = 60000
            val current = Calendar.getInstance(getTimeZone()!!).time
            val t = current.time
            val date = Date(t + (timeZone.hoursDifference * timeZone.minutesDifference * ONE_MINUTE_IN_MILLIS))
            date
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getReadyTime(
        readytime: Long,
        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone
    ): String? {
        val ONE_MINUTE_IN_MILLIS = 60000
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                .plusHours(timeZone.hoursDifference)
                .plusMinutes(timeZone.minutesDifference)
                .plusMinutes(readytime)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            return formatted
        } else {
            val current = Calendar.getInstance(getTimeZone()!!).time
            val t = current.time
            val date = Date(t + (readytime * ONE_MINUTE_IN_MILLIS))
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = formatter.format(date)
            return formatted
        }

    }


    fun roundToDigit(values: Float, places: Int): String {
        var value = values
        if (places < 0) throw IllegalArgumentException()
        val factor = 10.0.pow(places.toDouble()).toLong()
        value *= factor
        val tmp = value.roundToInt()
        val result = tmp.toDouble() / factor
        return (DecimalFormat("0.00").format(result))
    }


    @SuppressLint("SimpleDateFormat")
    fun getDateFromString(datestring: String?): Date? {
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
        var date: Date? = null

        try {
            datestring?.let {
                date = formatter.parse(it)
            }

        } catch (e: java.lang.Exception) {
        }
        return date
    }


 fun getDateFromStringSecond(datestring: String?): Date? {
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm a")
        var date: Date? = null

        try {
            datestring?.let {
                date = formatter.parse(it)
            }

        } catch (e: java.lang.Exception) {
        }
        return date
    }

    fun getOpenCloseTime(): String {
        return openclosetime
    }


    fun getTime(dueOn: String): Date? {
        val formate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        try {
            return formate.parse(dueOn)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Date()
    }

    fun isDeliveryTime(): Boolean {

        return IsdeliveryTime
    }

    fun isPickupTime(): Boolean {

        return IsPickupTime
    }


    fun initFormat(): Locale {
        val indiaLocale = Locale("en", "US")

        return indiaLocale
    }


    fun getFormat(): NumberFormat {
        val format = NumberFormat.getCurrencyInstance(initFormat())
        format.minimumFractionDigits = 2
        // Log.e("Defalt Format - ",format.currency.getDisplayName())
        return format
    }

    @SuppressLint("CheckResult")
    fun ItemVegLoadImage(view: ImageView, imageUrl: String?) {
        if (imageUrl != null) {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.fonzie_the_burger)
            requestOptions.error(R.drawable.fonzie_the_burger)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
            Glide.with(view.context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(view)
        }
    }

    fun isAvailableCatagoury(
        categoury: CatList?,
        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone,
        dueOn: String?
    ): Boolean{
        var isavailable = false
        if ((categoury?.openTime?:"").equals((categoury?.closeTime?:""),true)){
            isavailable = true
        }else{
            val  now  =  LocalDateTime.parse(dueOn,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))

            val nowDateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                .plusHours(timeZone.hoursDifference)
                .plusMinutes(timeZone.minutesDifference)
            val opendateTime =LocalDateTime.of(nowDateTime.toLocalDate(),LocalTime.parse(categoury?.openTime))
            val closedateTime = LocalDateTime.of(nowDateTime.toLocalDate(),LocalTime.parse(categoury?.closeTime))
            if (now.isAfter(opendateTime) && now.isBefore(closedateTime)) {
                isavailable = true
            }
        }
        return  isavailable

    }

//
//
//    fun removeCatagoury(
//        location: List<CatList>,
//        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone
//    ): java.util.ArrayList<CatList> {
//        val catagoury = java.util.ArrayList<CatList>()
//        for ( i in location.indices){
//            if ((location[i].openTime?:"").equals((location[i].closeTime?:""),true)){
//                catagoury.add(location[i])
//            }else{
//                val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
//                    .plusHours(timeZone.hoursDifference)
//                    .plusMinutes(timeZone.minutesDifference)
//                val opentime = LocalTime.parse(location[i].openTime)
//                val closetime = LocalTime.parse(location[i].closeTime)
//                val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
//                    .plusHours(timeZone.hoursDifference)
//                    .plusMinutes(timeZone.minutesDifference)
//
//                val opendateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
//                    .plusHours(timeZone.hoursDifference)
//                    .plusMinutes(timeZone.minutesDifference)
//                    .withHour(opentime.hour).withMinute(opentime.minute)
//                    .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)
//
//                val closedateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
//                    .plusHours(timeZone.hoursDifference)
//                    .plusMinutes(timeZone.minutesDifference).withHour(closetime.hour)
//                    .withMinute(closetime.minute)
//                    .withDayOfYear(if (opentime.isAfter(closetime)) opendateTime.dayOfYear + 1 else opendateTime.dayOfYear)
//                if (nowdatetime.isAfter(opendateTime) && nowdatetime.isBefore(closedateTime)) {
//                    catagoury.add(location[i])
//                }
//            }
//        }
//        return  catagoury
//    }



    @SuppressLint("DefaultLocale")
    fun romoveItems(
        itemList: ArrayList<ItemList>,
        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone
    ): ArrayList<ItemList> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
            val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
            val dow = date.dayOfWeek
            val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

            when (dayName.toLowerCase()) {

                "mon" -> {
                    return removeMonItems(itemList)
                }
                "tue" -> {
                    return removeTueItems(itemList)
                }
                "wed" -> {
                    return removeWedItems(itemList)
                }
                "thu" -> {
                    return removeThuItems(itemList)
                }
                "fri" -> {

                    return removeFriItems(itemList)

                }
                "sat" -> {
                    return removeSatItems(itemList)
                }
                "sun" -> {
                    return removeSunItems(itemList)

                }
            }

        }

        return itemList
    }



    fun isAvailableService(service: Service,timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone): Boolean
    {
        val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
        val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
        val dow = date.dayOfWeek
        val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        when (dayName.toLowerCase(Locale.US)) {
            "mon" -> {
                return service.openOn?.mon.equals("T",true)
            }
            "tue" -> {
                return service.openOn?.tue.equals("T",true)
            }
            "wed" -> {
                return service.openOn?.wed.equals("T",true)
            }
            "thu" -> {
                return service.openOn?.thu.equals("T",true)
            }
            "fri" -> {

                return service.openOn?.fri.equals("T",true)

            }
            "sat" -> {
                return service.openOn?.sat.equals("T",true)
            }
            "sun" -> {
                return service.openOn?.sun.equals("T",true)

            }

        }


        return false;

    }

    private fun removeMonItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.mon.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }
        }
        return itemListsecond

    }

    private fun removeTueItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.tue.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }

        }
        return itemListsecond
    }


    private fun removeWedItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.wed.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }

        }
        return itemListsecond

    }

    private fun removeThuItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.thu.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }

        }
        return itemListsecond
    }


    private fun removeFriItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.fri.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }

        }
        return itemListsecond
    }


    private fun removeSatItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListSecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.sat.equals("T", true)) {
                itemListSecond.add(itemList[i])
            }

        }
        return itemListSecond

    }


    private fun removeSunItems(itemList: ArrayList<ItemList>): ArrayList<ItemList> {
        val itemListsecond = ArrayList<ItemList>()
        for (i in 0 until itemList.size) {
            if (itemList[i].openOn.sun.equals("T", true)) {
                itemListsecond.add(itemList[i])
            }

        }
        return itemListsecond

    }


    fun encodeString(s: String): String {
        var data = ByteArray(0)

        try {
            data = s.toByteArray(charset("UTF-8"))

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } finally {

            return Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "")
        }
    }

    fun decodeString(encoded: String): String {
        val dataDec = Base64.decode(encoded, Base64.DEFAULT)
        var decodedString = ""
        try {

            decodedString = String(dataDec, charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()

        } finally {

            return decodedString
        }
    }


    @SuppressLint("DefaultLocale")
    fun isResturentOpen(
        schedule: List<Schedule>,
        openOrCloseYearMask: String,
        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone
    ): Boolean {
        intilizeAll()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
            val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
            val dow = date.dayOfWeek
            val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            for (i in schedule.indices) {
                if (dayName.toLowerCase() == schedule[i].day!!.toLowerCase()) {
                    if (schedule[i].closed.equals("F")) {
                        val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference)

                        val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference)

                        val opentime = LocalTime.parse(schedule[i].oT1)
                        val closetime = LocalTime.parse(schedule[i].cT1)

                        val opendateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference)
                            .withHour(opentime.hour).withMinute(opentime.minute)
                            .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)

                        val closedateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(closetime.hour)
                            .withMinute(closetime.minute)
                            .withDayOfYear(if (opentime.isAfter(closetime)) opendateTime.dayOfYear + 1 else opendateTime.dayOfYear)

                        val opentime2 = LocalTime.parse(schedule[i].oT2)
                        val closetime2 = LocalTime.parse(schedule[i].cT2)

                        val opendateTime2 = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(opentime2.hour)
                            .withMinute(opentime2.minute)
                            .withDayOfYear(if (now.isBefore(opentime2)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)

                        val closedateTime2 = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(closetime2.hour)
                            .withMinute(closetime2.minute)
                            .withDayOfYear(if (opentime2.isAfter(closetime2)) opendateTime2.dayOfYear + 1 else opendateTime2.dayOfYear)

                        val firstDel = LocalTime.parse(schedule[i].firstDel)
                        val firstDeldateTime =
                            org.threeten.bp.LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                                .plusHours(timeZone.hoursDifference)
                                .plusMinutes(timeZone.minutesDifference).withHour(firstDel.hour)
                                .withMinute(firstDel.minute)
                                .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)
                        val lastDel = LocalTime.parse(schedule[i].lastDel)

                        val lastDeldateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(lastDel.hour)
                            .withMinute(lastDel.minute)
                            .withDayOfYear(if (firstDel.isAfter(closetime)) firstDeldateTime.dayOfYear + 1 else firstDeldateTime.dayOfYear)

                        val firstOrd = LocalTime.parse(schedule[i].firstOrd)

                        val firstOrddateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(firstOrd.hour)
                            .withMinute(firstOrd.minute)
                            .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)

                        val lastOrd = LocalTime.parse(schedule[i].lastOrd)

                        val lastOrddateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                            .plusHours(timeZone.hoursDifference)
                            .plusMinutes(timeZone.minutesDifference).withHour(lastOrd.hour)
                            .withMinute(lastOrd.minute)
                            .withDayOfYear(if (firstOrd.isAfter(closetime)) firstOrddateTime.dayOfYear + 1 else firstOrddateTime.dayOfYear)



                        setOpenDateTime(opendateTime)
                        setCloseDateTime(closedateTime)


                        if (nowdatetime.isAfter(opendateTime) && nowdatetime.isBefore(closedateTime)) {
                            val open = StringBuilder()
                            open.append(getFormateTime(schedule[i].oT1))
                            open.append("-to-")
                            open.append(getFormateTime(schedule[i].cT1))
                            openclosetime = open.toString()
                            setOpenDateTime(opendateTime)
                            setCloseDateTime(closedateTime)

                            if (nowdatetime.isAfter(firstDeldateTime) && nowdatetime.isBefore(
                                    lastDeldateTime
                                )
                            ) {
                                IsdeliveryTime = true
                                val delivery = StringBuilder()
                                delivery.append(getFormateTime(schedule[i].firstDel))
                                delivery.append("-to-")
                                delivery.append(getFormateTime(schedule[i].lastDel))
                                deliverytime = delivery.toString()

                            } else {
                                val delivery = StringBuilder()
                                delivery.append(getFormateTime(schedule[i].firstDel))
                                delivery.append("-to-")
                                delivery.append(getFormateTime(schedule[i].lastDel))
                                deliverytime = delivery.toString()
                            }

                            if (nowdatetime.isAfter(firstOrddateTime) && nowdatetime.isBefore(lastOrddateTime)) {
                                IsPickupTime = true
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            }

                            val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                            val day = today - 1
                            val index = openOrCloseYearMask.toCharArray()[day]
                            return index.toString().equals("1")

                        } else if (nowdatetime.isAfter(opendateTime2) && nowdatetime.isBefore(
                                closedateTime2
                            )
                        ) {
                            val open = StringBuilder()
                            open.append(getFormateTime(schedule[i].oT2))
                            open.append("-to-")
                            open.append(getFormateTime(schedule[i].cT2))
                            openclosetime = open.toString()
                            setOpenDateTime(opendateTime2)
                            setCloseDateTime(closedateTime2)


                            if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                IsdeliveryTime = true
                                val delivery = StringBuilder()
                                delivery.append(getFormateTime(schedule[i].firstDel))
                                delivery.append("-to-")
                                delivery.append(getFormateTime(schedule[i].lastDel))
                                deliverytime = delivery.toString()

                            } else {
                                val delivery = StringBuilder()
                                delivery.append(getFormateTime(schedule[i].firstDel))
                                delivery.append("-to-")
                                delivery.append(getFormateTime(schedule[i].lastDel))
                                deliverytime = delivery.toString()
                            }

                            if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                IsPickupTime = true
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            }

                            val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                            val day = today - 1
                            val index = openOrCloseYearMask.toCharArray()[day]
                            return index.toString() == "1"

                        } else {

                            if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                IsPickupTime = true
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                            }

                            return false
                        }
                    } else {

                        val pickup = StringBuilder()
                        pickup.append(getFormateTime(schedule[i].firstOrd))
                        pickup.append("-to-")
                        pickup.append(getFormateTime(schedule[i].lastOrd))
                        pickuptime = pickup.toString()
                        return false
                    }
                }
            }
        } else {
            val date = Calendar.getInstance()
            val dow = date.get(Calendar.DAY_OF_WEEK)
            when (dow) {
                Calendar.MONDAY -> {
                    for (i in schedule.indices) {

                        if ("Mon".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)

                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    //LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)

                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }


                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }

                                        return false
                                    }

                                }
                            } else {
                                    val pickup = StringBuilder()
                                    pickup.append(getFormateTime(schedule[i].firstOrd))
                                    pickup.append("-to-")
                                    pickup.append(getFormateTime(schedule[i].lastOrd))
                                    pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.TUESDAY -> {
                    for (i in schedule.indices) {

                        if ("Tue".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)


                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    //LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)

                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)
                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.WEDNESDAY -> {
                    for (i in 0 until schedule.size) {

                        if ("Wed".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)
                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    // LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)
                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)
                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.THURSDAY -> {
                    for (i in schedule.indices) {

                        if ("Thu".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)


                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    //LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)
                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }


                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.FRIDAY -> {
                    for (i in schedule.indices) {

                        if ("Fri".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)


                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    //LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)
                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.SATURDAY -> {
                    for (i in schedule.indices) {

                        if ("Sat".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)

                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    // LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)
                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }



                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }
                Calendar.SUNDAY -> {
                    for (i in 0 until schedule.size) {

                        if ("Sun".toLowerCase().equals(schedule[i].day!!.toLowerCase())) {

                            if (schedule[i].closed.equals("F")) {

                                if (schedule[i].closed.equals("F")) {

                                    val opentime = LocalTime.parse(schedule[i].oT1)
                                    val closetime = LocalTime.parse(schedule[i].cT1)

                                    val opentime2 = LocalTime.parse(schedule[i].oT2)
                                    val closetime2 = LocalTime.parse(schedule[i].cT2)

                                    val firstDel = LocalTime.parse(schedule[i].firstDel)
                                    val lastDel = LocalTime.parse(schedule[i].lastDel)

                                    val firstOrd = LocalTime.parse(schedule[i].firstOrd)
                                    val lastOrd = LocalTime.parse(schedule[i].lastOrd)


                                    val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                                        .plusHours(timeZone.hoursDifference)
                                        .plusMinutes(timeZone.minutesDifference)
                                    //LocalTime.now(ZoneId.systemDefault())
                                    setOpenTime(opentime)
                                    setCloseTime(closetime)
                                    if (now.isAfter(opentime) && now.isBefore(closetime)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()

                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }


                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }

                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else if (now.isAfter(opentime2) && now.isBefore(closetime2)) {
                                        val open = StringBuilder()
                                        open.append(getFormateTime(schedule[i].oT1))
                                        open.append("-to-")
                                        open.append(getFormateTime(schedule[i].cT1))
                                        openclosetime = open.toString()
                                        setOpenTime(opentime)
                                        setCloseTime(closetime)

                                        if (now.isAfter(firstDel) && now.isBefore(lastDel)) {
                                            IsdeliveryTime = true
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()

                                        } else {
                                            val delivery = StringBuilder()
                                            delivery.append(getFormateTime(schedule[i].firstDel))
                                            delivery.append("-to-")
                                            delivery.append(getFormateTime(schedule[i].lastDel))
                                            deliverytime = delivery.toString()
                                        }

                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                                        val day = today - 1
                                        val index = openOrCloseYearMask.toCharArray()[day]
                                        return index.toString().equals("1")
                                    } else {
                                        if (now.isAfter(firstOrd) && now.isBefore(lastOrd)) {
                                            IsPickupTime = true
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        } else {
                                            val pickup = StringBuilder()
                                            pickup.append(getFormateTime(schedule[i].firstOrd))
                                            pickup.append("-to-")
                                            pickup.append(getFormateTime(schedule[i].lastOrd))
                                            pickuptime = pickup.toString()
                                        }
                                        return false
                                    }

                                }
                            } else {
                                val pickup = StringBuilder()
                                pickup.append(getFormateTime(schedule[i].firstOrd))
                                pickup.append("-to-")
                                pickup.append(getFormateTime(schedule[i].lastOrd))
                                pickuptime = pickup.toString()
                                return false
                            }
                        }
                    }
                }

            }
        }
        return false
    }

    private fun setCloseTime(closetime: LocalTime?) {
        this.closetime = closetime
    }

    private fun setOpenTime(opentime: LocalTime?) {
        this.opentime = opentime
    }

    private fun setCloseDateTime(closedateTime: LocalDateTime) {
        this.closedateTime = closedateTime
    }

    private fun setOpenDateTime(opendateTime: LocalDateTime) {
        this.opendateTime = opendateTime
    }

    fun getOpenDateTime(): LocalDateTime? {
        return opendateTime
    }


    fun chooesLocation(
        mContext: Context,
        regionList: ArrayList<RegionList>,
        locationDailogListner: LocationDailogListner
    ) {

        val dialog = Dialog(mContext, R.style.MaterialAnimations)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogLocationListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.dialog_location_list,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        val layoutParams = LayoutParams()
        val window = dialog.window
        layoutParams.copyFrom(window!!.attributes)
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height = LayoutParams.MATCH_PARENT
        window.setGravity(Gravity.BOTTOM)
        layoutParams.windowAnimations = R.style.DialogAnimationLeftToRight
        window.attributes = layoutParams
        dialogAlertBinding.locationrec.adapter = LocationAdapter(mContext,regionList, object :
            LocationAdapter.LocationListner {
            override fun onSelectLocation(region: RegionList) {
                locationDailogListner.onSelectLocation(region)
            }

        })


        if (dialog.isShowing) {
            dialog.dismiss()
        }
        try {
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun getFormateTime(time: String?): String {
        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        var date = Date()
        time?.let {
            date = sdf.parse(it)!!
        }

        return sdfs.format(date)
    }

    fun AlertMessage(
        context: Context,
        title: String,
        message: String,
        buttn_name: String,
        okButton: OkButton
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogAlertBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context.applicationContext),
            R.layout.dialog_alert,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        dialogAlertBinding.msg.text = message
        dialogAlertBinding.titel.text = title
        dialogAlertBinding.ok.text = buttn_name
        dialogAlertBinding.ok.setOnClickListener {
            okButton.onClickOkey()
            dialog.dismiss()
        }

        dialogAlertBinding.cancel.setOnClickListener { dialog.dismiss() }

        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }

        if (dialog.isShowing) {
            dialog.dismiss()
        }
        try {
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun AlertMessageWihoutImage(
        context: Context,
        title: String,
        message: String,
        buttn_name: String,
        okButton: OkButton
    ) {
        val htmlAsSpanned = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogAlertSecondBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context.applicationContext),
            R.layout.dialog_alert_second,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        dialogAlertBinding.msg.text = message
        dialogAlertBinding.titel.text = htmlAsSpanned
        dialogAlertBinding.ok.text = buttn_name
        dialogAlertBinding.ok.setOnClickListener {
            okButton.onClickOkey()
            dialog.dismiss()
        }

        dialogAlertBinding.cancel.setOnClickListener { dialog.dismiss() }

        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }

        if (dialog.isShowing) {
            dialog.dismiss()
        }
        try {
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun scheduleOrder(
        mContext: Context,
        restrurent: RestrurentModel,
        cartdata: CartData,
        dialogScheduleTime: DialogScheduleTime,
        service: Service
    ) {
        var isOpen: Boolean
        var opentime : LocalDateTime?= null
        var closetime : LocalDateTime? = null
        val ONE_MINUTE_IN_MILLIS = 60000
        val dialog = Dialog(mContext, R.style.Restrurent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogScheduleOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext.applicationContext),
            R.layout.dialog_schedule_order,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        val calander = Calendar.getInstance()
        val current = Calendar.getInstance(getTimeZone()!!).time //+ (24 * 60 * 60 * 1000)
        val t = current.time
        val date = Date(t + (restrurent.timeZone!!.hoursDifference * restrurent.timeZone!!.minutesDifference * ONE_MINUTE_IN_MILLIS))

        dialogAlertBinding.calanderView.minDate = date.time + (24 * 60 * 60 * 1000)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.DAY_OF_YEAR, 7)
        dialogAlertBinding.calanderView.maxDate = maxDate.timeInMillis

        calander.set(Calendar.DAY_OF_MONTH,calander.get(Calendar.DAY_OF_MONTH)+1)// .plusHours(restrurent.timeZone!!.hoursDifference).plusMinutes(restrurent.timeZone!!.minutesDifference)

        when {
            service.name.equals("Pickup") -> {
                opentime = getPickupOpenTime(restrurent.schedule!!,restrurent.timeZone!!)
                closetime = getPickupCloseTime(restrurent.schedule!!,restrurent.timeZone!!)
            }
            service.name.equals("Curbside") -> {
                opentime = getPickupOpenTime(restrurent.schedule!!,restrurent.timeZone!!)
                closetime = getPickupCloseTime(restrurent.schedule!!,restrurent.timeZone!!)
            }
            service.name.equals("Off-base Delivery") -> {
                opentime = getDeliveryOpenTime(restrurent.schedule!!,restrurent.timeZone!!)
                closetime = getDeliveryCloseTime(restrurent.schedule!!,restrurent.timeZone!!)
            }
            service.name.equals("On-base Delivery") -> {
                opentime = getDeliveryOpenTime(restrurent.schedule!!,restrurent.timeZone!!)
                closetime = getDeliveryCloseTime(restrurent.schedule!!,restrurent.timeZone!!)
            }
        }

        restrurent.schedule?.let {
            isOpen = isResturentOpen(
                it,
                restrurent.calendar!!.openOrCloseYearMask!!,
                restrurent.timeZone!!
            )
            restrurent.menus?.let {
                val menudata = it.find { ((it.id ?: "0").toLong()) == (cartdata.menuId ?: 0).toLong() }
                dialogAlertBinding.topmessage.text = mContext.getString(R.string.online_order_not_available)

                if (menudata?.fo.equals("T") && isOpen) {
                    val timesdata  = getTimeSet(isOpen, false, restrurent.timeZone!!,opentime,closetime)
                    if(timesdata.isEmpty()){
                        dialogAlertBinding.laterlay.visibility = View.GONE
                        dialogAlertBinding.topmessage.text = mContext.getString(R.string.online_order_not_available)
                    }else {
                        dialogAlertBinding.laterlay.visibility = View.VISIBLE
                        val adapter = ArrayAdapter<String>(mContext, R.layout.spinner_list_item,timesdata)
                        dialogAlertBinding.laterselectTimeSpbinner.adapter = adapter
                        onClickLater(dialogAlertBinding, mContext, cartdata)
                    }
                } else if (menudata?.fo.equals("T") && !isOpen) {

                    val timesdata  = getTimeSet(isOpen, false, restrurent.timeZone!!, opentime, closetime)
                    if(timesdata.isEmpty()){
                        dialogAlertBinding.laterlay.visibility = View.GONE
                        dialogAlertBinding.topmessage.text = mContext.getString(R.string.online_order_not_available)
                    }else {
                        dialogAlertBinding.laterlay.visibility = View.VISIBLE
                        val adapter = ArrayAdapter<String>(
                            mContext,
                            R.layout.spinner_list_item,
                            timesdata
                        )
                        dialogAlertBinding.laterselectTimeSpbinner.adapter = adapter
                        onClickLater(dialogAlertBinding, mContext, cartdata)
                    }
                } else {
                    dialogAlertBinding.laterlay.visibility = View.GONE
                }

                if (menudata?.lt.equals("T")) {
                    var  timedata =  getTimeSet(isOpen, true, restrurent.timeZone!!,opentime,closetime)
                    if (timedata.isEmpty()){
                        dialogAlertBinding.todaylay.visibility = View.GONE
                        dialogAlertBinding.topmessage.text = mContext.getString(R.string.online_order_not_available)
                    }else{
                        dialogAlertBinding.todaylay.visibility = View.VISIBLE
                        val adapter = ArrayAdapter<String>(mContext,R.layout.spinner_list_item,timedata)
                        dialogAlertBinding.selectTimeSpbinner.adapter = adapter
                        onClickToday(dialogAlertBinding, mContext, cartdata)
                    }

                } else {
                    dialogAlertBinding.todaylay.visibility = View.GONE
                }
                if (menudata?.asap.equals("T") && isOpen) {
                    if (isOpen) {
                        onClickAsap(dialogAlertBinding, mContext, cartdata)
                        dialogAlertBinding.asaplay.visibility = View.VISIBLE
                        onClickAsap(dialogAlertBinding,mContext,cartdata)
                    }
                    else {
                        dialogAlertBinding.asaplay.visibility = View.GONE
                    }
                } else {
                    dialogAlertBinding.asaplay.visibility = View.GONE
                }

            }
        }


        dialogAlertBinding.todaylay.setOnClickListener {
            onClickToday(dialogAlertBinding, mContext, cartdata)
        }

        dialogAlertBinding.asaplay.setOnClickListener {
            onClickAsap(dialogAlertBinding, mContext, cartdata)
        }

        dialogAlertBinding.laterlay.setOnClickListener {
            onClickLater(dialogAlertBinding, mContext, cartdata)
        }

        dialogAlertBinding.selectTimeBt.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()

            }
            dialogScheduleTime.onScheduleTimeString(service)
        }

        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                cartdata.timeSelection = null
                dialog.dismiss()
            }
            true
        }

        dialogAlertBinding.calanderView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calander.set(Calendar.YEAR, year)
            calander.set(Calendar.MONTH, month)
            calander.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val time = dialogAlertBinding.laterselectTimeSpbinner.selectedItem as String
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.systemDefault())
            var localtime =
                LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
            var localDate = LocalDate.of(
                calander.get(Calendar.YEAR),
                (calander.get(Calendar.MONTH) + 1),
                calander.get(Calendar.DAY_OF_MONTH)
            )
            var localdatetime = LocalDateTime.of(localDate, localtime)
            //.plusHours(restrurent.timeZone!!.hoursDifference).plusMinutes(restrurent.timeZone!!.minutesDifference)

            val formatted = localdatetime.format(formatter)
            cartdata.dueOn = formatted
            Log.e("Later Time selected", formatted)

        }


        dialogAlertBinding.laterselectTimeSpbinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var time = dialogAlertBinding.laterselectTimeSpbinner.selectedItem as String
                time.toUpperCase(Locale.US)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneId.systemDefault())
                var localtime =
                    LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))

                var localDate = LocalDate.of(
                    calander.get(Calendar.YEAR),
                    (calander.get(Calendar.MONTH) + 1),
                    calander.get(Calendar.DAY_OF_MONTH)
                )
                var localdatetime = LocalDateTime.of(localDate, localtime)
                // .plusHours(restrurent.timeZone!!.hoursDifference).plusMinutes(restrurent.timeZone!!.minutesDifference)


                val formatted = localdatetime.format(formatter)
                cartdata.dueOn = formatted

                Log.e("Later Time selected", formatted)
            }

        }



        dialogAlertBinding.selectTimeSpbinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var time = dialogAlertBinding.selectTimeSpbinner.selectedItem as String
                time.toUpperCase(Locale.US)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZoneId.systemDefault())
                var localtime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))

                var localdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(restrurent.timeZone!!.hoursDifference)
                    .plusMinutes(restrurent.timeZone!!.minutesDifference)
                    .withHour(localtime.hour).withMinute(localtime.minute)

                val formatted = localdatetime.format(formatter)
                cartdata.dueOn = formatted
                Log.e("Time selected", time)

            }

        }

        dialogAlertBinding.back.setOnClickListener {
            cartdata.timeSelection = null
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }




        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getPickupOpenTime(schedule: List<Schedule>, timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone) : LocalDateTime?{
        val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
        val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
        val dow = date.dayOfWeek
        val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        for (i in schedule.indices) {
            if (dayName.toLowerCase() == schedule[i].day!!.toLowerCase()) {
                val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val opentime = LocalTime.parse(schedule[i].oT1)
                val closetime = LocalTime.parse(schedule[i].cT1)

                val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val firstOrd = LocalTime.parse(schedule[i].firstOrd)

                val firstOrddateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference).withHour(firstOrd.hour)
                    .withMinute(firstOrd.minute)
                    .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)


                return firstOrddateTime

            }
        }
        return null
    }

    fun getPickupCloseTime(schedule: List<Schedule>, timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone) : LocalDateTime? {
        val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
        val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
        val dow = date.dayOfWeek
        val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        for (i in schedule.indices) {
            if (dayName.toLowerCase() == schedule[i].day!!.toLowerCase()) {
                val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val opentime = LocalTime.parse(schedule[i].oT1)
                val closetime = LocalTime.parse(schedule[i].cT1)

                val firstOrd = LocalTime.parse(schedule[i].firstOrd)

                val firstOrddateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference).withHour(firstOrd.hour)
                    .withMinute(firstOrd.minute)
                    .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)




                val lastOrd = LocalTime.parse(schedule[i].lastOrd)

                val lastOrddateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference).withHour(lastOrd.hour)
                    .withMinute(lastOrd.minute)
                    .withDayOfYear(if (firstOrd.isAfter(closetime)) firstOrddateTime.dayOfYear + 1 else firstOrddateTime.dayOfYear)

                return lastOrddateTime
            }
        }
        return  null
    }

    fun getDeliveryOpenTime(schedule: List<Schedule>, timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone) : LocalDateTime?{
        val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
        val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
        val dow = date.dayOfWeek
        val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        for (i in schedule.indices) {
            if (dayName.toLowerCase() == schedule[i].day!!.toLowerCase()) {
                val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val opentime = LocalTime.parse(schedule[i].oT1)
                val closetime = LocalTime.parse(schedule[i].cT1)

                val firstDel = LocalTime.parse(schedule[i].firstDel)

                val firstDeldateTime =
                    org.threeten.bp.LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                        .plusHours(timeZone.hoursDifference)
                        .plusMinutes(timeZone.minutesDifference).withHour(firstDel.hour)
                        .withMinute(firstDel.minute)
                        .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)

                return firstDeldateTime

            }
        }
        return null
    }

    fun getDeliveryCloseTime(schedule: List<Schedule>, timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone) : LocalDateTime? {
        val localday = LocalDateTime.now(ZoneId.of(getTimeZone()?.id)).plusHours(timeZone.hoursDifference).plusMinutes(timeZone.minutesDifference)
        val date = LocalDate.of(localday.year, localday.month, localday.dayOfMonth)
        val dow = date.dayOfWeek
        val dayName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
        for (i in schedule.indices) {
            if (dayName.toLowerCase() == schedule[i].day!!.toLowerCase()) {
                val now = LocalTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val nowdatetime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                val opentime = LocalTime.parse(schedule[i].oT1)
                val closetime = LocalTime.parse(schedule[i].cT1)

                val firstDel = LocalTime.parse(schedule[i].firstDel)

                val firstDeldateTime =
                    org.threeten.bp.LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                        .plusHours(timeZone.hoursDifference)
                        .plusMinutes(timeZone.minutesDifference).withHour(firstDel.hour)
                        .withMinute(firstDel.minute)
                        .withDayOfYear(if (now.isBefore(opentime)) nowdatetime.dayOfYear - 1 else nowdatetime.dayOfYear)
                val lastDel = LocalTime.parse(schedule[i].lastDel)

                val lastDeldateTime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference).withHour(lastDel.hour)
                    .withMinute(lastDel.minute)
                    .withDayOfYear(if (firstDel.isAfter(closetime)) firstDeldateTime.dayOfYear + 1 else firstDeldateTime.dayOfYear)

                return lastDeldateTime
            }
        }
        return  null
    }



    private fun onClickToday(
        dialogAlertBinding: DialogScheduleOrderBinding,
        context: Context,
        cartdata: CartData
    ) {
        dialogAlertBinding.todaylay.isSelected = true
        dialogAlertBinding.todaytext.isSelected = true
        dialogAlertBinding.asaptext.isSelected = false
        dialogAlertBinding.asaplay.isSelected = false
        dialogAlertBinding.laterlay.isSelected = false
        dialogAlertBinding.latertext.isSelected = false
        dialogAlertBinding.topmessage.visibility = View.VISIBLE
        dialogAlertBinding.topmessage.text = context.getString(R.string.select_any_time)

        dialogAlertBinding.todaytext.setTypeface(
            dialogAlertBinding.todaytext.typeface,
            Typeface.BOLD
        )
        dialogAlertBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white_color
            )
        )
        dialogAlertBinding.asaptext.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
        dialogAlertBinding.asaptext.setTypeface(
            dialogAlertBinding.asaptext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        dialogAlertBinding.latertext.setTypeface(
            dialogAlertBinding.latertext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.timeselectedlay.visibility = View.VISIBLE
        dialogAlertBinding.latertimeselectedlay.visibility = View.GONE
        dialogAlertBinding.selectTimeBt.visibility = View.VISIBLE
        cartdata.timeSelection = 1
    }

    private fun onClickAsap(dialogAlertBinding: DialogScheduleOrderBinding,context: Context,cartdata: CartData) {

        dialogAlertBinding.todaylay.isSelected = false
        dialogAlertBinding.todaytext.isSelected = false
        dialogAlertBinding.asaptext.isSelected = true
        dialogAlertBinding.asaplay.isSelected = true
        dialogAlertBinding.laterlay.isSelected = false
        dialogAlertBinding.latertext.isSelected = false
        dialogAlertBinding.topmessage.visibility = View.GONE

        dialogAlertBinding.asaptext.setTypeface(dialogAlertBinding.asaptext.typeface, Typeface.BOLD)

        dialogAlertBinding.asaptext.setTextColor(
            ContextCompat.getColor(context,R.color.white_color)
        )

        dialogAlertBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        dialogAlertBinding.todaytext.setTypeface(
            dialogAlertBinding.todaytext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        dialogAlertBinding.latertext.setTypeface(
            dialogAlertBinding.latertext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.timeselectedlay.visibility = View.GONE
        dialogAlertBinding.latertimeselectedlay.visibility = View.GONE
        dialogAlertBinding.selectTimeBt.visibility = View.VISIBLE
        cartdata.timeSelection = 0
    }
    private fun onClickLater(dialogAlertBinding: DialogScheduleOrderBinding,context: Context,cartdata: CartData) {
        dialogAlertBinding.todaylay.isSelected = false
        dialogAlertBinding.todaytext.isSelected = false
        dialogAlertBinding.asaptext.isSelected = false
        dialogAlertBinding.asaplay.isSelected = false
        dialogAlertBinding.laterlay.isSelected = true
        dialogAlertBinding.latertext.isSelected = true
        dialogAlertBinding.asaptext.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
        dialogAlertBinding.topmessage.visibility = View.VISIBLE
        dialogAlertBinding.topmessage.text = context.getString(R.string.select_a_time_upto_7_days_nin_advance)

        dialogAlertBinding.asaptext.setTypeface(
            dialogAlertBinding.asaptext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.todaytext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.dark_gray
            )
        )
        dialogAlertBinding.todaytext.setTypeface(
            dialogAlertBinding.todaytext.typeface,
            Typeface.NORMAL
        )
        dialogAlertBinding.latertext.setTypeface(
            dialogAlertBinding.todaytext.typeface,
            Typeface.BOLD
        )
        dialogAlertBinding.latertext.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.white_color
            )
        )
        dialogAlertBinding.timeselectedlay.visibility = View.GONE
        dialogAlertBinding.latertimeselectedlay.visibility = View.VISIBLE
        dialogAlertBinding.selectTimeBt.visibility = View.VISIBLE
        cartdata.timeSelection = 2

    }


    @SuppressLint("SimpleDateFormat")
    fun getTimeSet(
        fromNowTime: Boolean,
        islateorder: Boolean,
        timeZone: com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.TimeZone,
        opentime: LocalDateTime?,
        closetime: LocalDateTime?
    ): ArrayList<String> {
        val results = ArrayList<String>()

        if (fromNowTime && islateorder) {
            var locatime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                .plusHours(timeZone.hoursDifference)
                .plusMinutes(timeZone.minutesDifference)

            while (locatime.minute%5 != 0){
                locatime = locatime?.plusMinutes(1)
            }

            while (!(!locatime?.isAfter(opentime)!! || !locatime.isBefore(closetime))) {
                try {
                    results.add(locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(Locale.US))
                }catch (e: Exception){e.printStackTrace()}
                locatime = locatime.plusMinutes(15)
            }
        }

        else if (fromNowTime.not() && islateorder) {


            if (opentime != null && closetime != null) {
                var locattime = LocalDateTime.now(ZoneId.of(getTimeZone()?.id))
                    .plusHours(timeZone.hoursDifference)
                    .plusMinutes(timeZone.minutesDifference)

                if (locattime.isBefore(opentime)) {
                    var locatime = opentime.plusMinutes(5)

                    while ((locatime?.minute ?: 5) % 5 != 0) {
                        locatime = locatime?.plusMinutes(1)
                    }
                    while (!(!locatime?.isAfter(opentime)!! || !locatime.isBefore(
                            closetime
                        ))
                    ) {

                        results.add(
                            locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(
                                Locale.US
                            )
                        )

                        locatime = locatime.plusMinutes(15)
                    }
                } else {
                    if (locattime.dayOfYear > opentime.dayOfYear) {
                        var locatime = opentime.plusMinutes(5)

                        while ((locatime?.minute?:5) % 5 != 0) {
                            locatime = locatime?.plusMinutes(1)
                        }
                        while (!(!locatime?.isAfter(opentime)!! || !locatime.isBefore(closetime))) {

                            results.add(
                                locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(
                                    Locale.US
                                )
                            )
                            locatime = locatime.plusMinutes(15)
                        }

                    }
                }

            } else if (this.opentime != null && this.closetime != null) {
                if (LocalTime.now(ZoneId.of(getTimeZone()?.id))
                        .plusHours(timeZone.hoursDifference)
                        .plusMinutes(timeZone.minutesDifference)
                        .isBefore(this.opentime)
                ){

                    var locatime = this.opentime?.plusMinutes(10)

                    while ((locatime?.minute?:5)%5 != 0){
                        locatime = locatime?.plusMinutes(1)
                    }
                    while (locatime?.isAfter(this.opentime)!! && locatime.isBefore(this.closetime)) {

                        results.add(locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(
                            Locale.US))
                        locatime = locatime.plusMinutes(15)
                    }
                }
            }
        }

        else {
            if (opentime != null && closetime != null) {
                var locatime = opentime.plusMinutes(5)

                while ((locatime?.minute?:5)%5 != 0){
                    locatime = locatime?.plusMinutes(1)
                }
                while (!(!locatime?.isAfter(opentime)!! || !locatime.isBefore(closetime))) {

                    results.add(locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(
                        Locale.US))
                    locatime = locatime.plusMinutes(15)
                }
            } else if (this.opentime != null && this.closetime != null) {
                var locatime = this.opentime?.plusMinutes(5)

                while ((locatime?.minute?:5)%5 != 0){
                    locatime = locatime?.plusMinutes(1)
                }
                while (locatime?.isAfter(this.opentime)!! && locatime.isBefore(this.closetime)) {

                    results.add(locatime.format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase(
                        Locale.US))
                    locatime = locatime.plusMinutes(15)
                }
            }
        }
        return results
    }



    fun getProperDayTime(data: Date): String {
        val dateFormat =
            DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.US)
        return dateFormat.format(data)
    }

    @SuppressLint("SimpleDateFormat")
    fun getProperDay(data: Date): String {

        val formatter = SimpleDateFormat("MM/dd/yyyy")
        return formatter.format(data)

    }

    fun getProperTime(data: Date): String {
        val dateFormat =
            DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US)
        return dateFormat.format(data)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun openPaymentGatway(mContext: Context, openlink: String) {
        val dialog = Dialog(mContext, R.style.Restrurent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogPatmentGatwayBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext.applicationContext),
                R.layout.dialog_patment_gatway,
                null,
                false
            )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)
        dialogAlertBinding.webView.settings.javaScriptEnabled = true
        dialogAlertBinding.webView.settings.domStorageEnabled = true
        dialogAlertBinding.webView.settings.databaseEnabled = true
        //dialogAlertBinding.webView.settings.pluginState = WebSettings.PluginState.ON
        dialogAlertBinding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        dialogAlertBinding.webView.isVerticalScrollBarEnabled = false
        dialogAlertBinding.webView.isHorizontalScrollBarEnabled = false
        dialogAlertBinding.webView.settings.useWideViewPort = true
        dialogAlertBinding.webView.settings.loadWithOverviewMode = true
        dialogAlertBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                Log.e("Request-", request.toString())
                return super.shouldInterceptRequest(view, request)
            }
        }
        dialogAlertBinding.webView.loadUrl(openlink)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()

    }


    private fun isFieldEmpty(value: String): Boolean {
        return value.isEmpty() || value.isBlank()
    }


    private fun getTwoDigitNumber(i: Int): String = (DecimalFormat("00").format(i))
    private fun isPhoneNumberValid(mobileno: String): Boolean {
        var phone = mobileno
        if (phone.startsWith("0"))
            phone = phone.substring(1)
        return phone.trim { it <= ' ' }.length in 10..10
    }

    fun getPermission(): MutableCollection<String> {
        val list = ArrayList<String>()
        list.add("email")
        list.add("public_profile")
        list.add("user_birthday")
        list.add("user_hometown")
        list.add("user_friends")
        return list
    }


    fun getPassword(context: Context, requestSigin: SignInRequest, submitButton: SubmitButton) {
        val dialog = Dialog(context, R.style.Restrurent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogPhoneNoBinding: DialogPasswordNoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context.applicationContext),
            R.layout.dialog_password_no,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogPhoneNoBinding.root)
        val name = requestSigin.data?.customerFBData?.FName?.split(" ")?.get(0)
        dialogPhoneNoBinding.titel.text = "Hello, ${name}!!"

        val dialogcalback = object : DilalogMessageCallBack {
            override fun onError(s: String) {
                dialogPhoneNoBinding.message.text = s
                //isclick = false
                dialogPhoneNoBinding.submit.isEnabled = true
            }

            override fun onSuccessful() {
                dialog.dismiss()
            }
        }

        dialogPhoneNoBinding.submit.setOnClickListener {
            when {
                isFieldEmpty(dialogPhoneNoBinding.mobileno.text.toString()) ->
                    Toast.makeText(context, "Enter valid password", Toast.LENGTH_SHORT).show()
                else -> {
                    //isclick = true
                    dialogPhoneNoBinding.submit.isEnabled = false
                    dialogPhoneNoBinding.message.text = ""
                    submitButton.submit(
                        dialogPhoneNoBinding.mobileno.text.toString(),
                        dialogcalback
                    )
                }
            }

        }

        dialogPhoneNoBinding.cancel.setOnClickListener { dialog.dismiss() }
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
    }


    fun getPhoneNo(
        context: Context,
        from: Int,
        requestSigin: SignInRequest,
        submitButton: SubmitButton
    ) {
        val dialog = Dialog(context, R.style.Restrurent)
        var isclick = false
        Log.e("from",from.toString())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogPhoneNoBinding: DialogPhoneNoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context.applicationContext),
            R.layout.dialog_phone_no,
            null,
            false
        )
        dialog.setCancelable(true)
        //dialog.setCancelable(true)
        dialog.setContentView(dialogPhoneNoBinding.root)
        val name = requestSigin.data?.customerFBData?.FName?.split(" ")?.get(0)
        dialogPhoneNoBinding.titel.text = "Hello, ${name}!!"

        val dialogcalback = object : DilalogMessageCallBack {
            override fun onError(s: String) {
                dialogPhoneNoBinding.message.text = s
                isclick = false
                dialogPhoneNoBinding.submit.isEnabled = true
            }

            override fun onSuccessful() {
                dialog.dismiss()
            }
        }
        //message
        dialogPhoneNoBinding.submit.setOnClickListener {
            if (!isPhoneNumberValid(dialogPhoneNoBinding.mobileno.text.toString())) {
                Toast.makeText(context, "Enter valid mobile no.", Toast.LENGTH_SHORT).show()
            } else {
                if (!isclick) {
                    isclick = true
                    dialogPhoneNoBinding.submit.isEnabled = false
                    dialogPhoneNoBinding.message.text = ""
                    submitButton.submit(
                        dialogPhoneNoBinding.mobileno.text.toString(),
                        dialogcalback
                    )
                }

            }
        }
        dialogPhoneNoBinding.cancel.setOnClickListener { dialog.dismiss() }
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            it.attributes.gravity = Gravity.NO_GRAVITY
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getDataArray(respdata: String): ArrayList<Coupon> {
        val list = ArrayList<Coupon>()
        val arr = JSONArray(respdata)
        try {
            for (i in 0 until arr.length()) {
                val copenobject = arr.getJSONObject(i)
                val coupen = Coupon()
                coupen.issueDate = copenobject.getString("IssueDate")
                coupen.expDate = copenobject.getString("ExpDate")
                coupen.useDate = copenobject.getString("UseDate")
                coupen.status = copenobject.getString("Status")
                coupen.usageCountLeft = copenobject.getLong("UsageCountLeft")
                coupen.custCouponId = copenobject.getLong("CustCouponId")
                coupen.type = copenobject.getString("Type")
                coupen.couponCode = copenobject.getString("CouponCode")
                coupen.description = copenobject.getString("Description")
                coupen.name = copenobject.getString("Name")
                coupen.discount = copenobject.getDouble("Discount")
                coupen.preTax = copenobject.getString("PreTax")
                coupen.active = copenobject.getString("Active")
                coupen.minOrderAmt = copenobject.getDouble("MinOrderAmt")
                coupen.couponImage = copenobject.getString("CouponImage")
                list.add(coupen)
            }

            return list
        } catch (e: java.lang.Exception) {
            return list
        }

    }


    fun requestPhonecallPermission(context: Activity, codeScannerinit: CodeScanerInit) {
        Dexter.withActivity(context)
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    codeScannerinit.allowPermition()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog(context)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }
    private fun showSettingsDialog(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage("This Application needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            Log.e("Which", "$which")
            dialog.cancel()
            openSettings(context)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Log.e("Which", "$which")
            dialog.cancel()
        }
        builder.show()
    }
    private fun openSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun getModifierList(addOnOptionModifier1: AddOnOptionModifier1): ArrayList<AddOnOptionModifier> {
        val modifirelist = ArrayList<AddOnOptionModifier>()
        if (addOnOptionModifier1.factor1 != null && addOnOptionModifier1.label1.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor1
            addmodifier.label = addOnOptionModifier1.label1
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }
        if (addOnOptionModifier1.factor2 != null && addOnOptionModifier1.label2.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor2
            addmodifier.label = addOnOptionModifier1.label2
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }
        if (addOnOptionModifier1.factor3 != null && addOnOptionModifier1.label3.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor3
            addmodifier.label = addOnOptionModifier1.label3
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }
        if (addOnOptionModifier1.factor4 != null && addOnOptionModifier1.label4.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor4
            addmodifier.label = addOnOptionModifier1.label4
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }

        if (addOnOptionModifier1.factor5 != null && addOnOptionModifier1.label5.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor5
            addmodifier.label = addOnOptionModifier1.label5
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }
        if (addOnOptionModifier1.factor6 != null && addOnOptionModifier1.label6.isNullOrEmpty().not()) {
            val addmodifier = AddOnOptionModifier()
            addmodifier.factor = addOnOptionModifier1.factor6
            addmodifier.label = addOnOptionModifier1.label6
            addmodifier.text = addOnOptionModifier1.id.toString()
            modifirelist.add(addmodifier)
        }

        return modifirelist
    }


   fun getCountrys(): ArrayList<Country> {
        val countrymodel: CountryModel = Gson().fromJson(ConstantCommand.COUNRTY_TEXT, CountryModel::class.java)
        return countrymodel.country
    }

    fun openConfirm(context: Context, request: SignInRequest, dialogContinoue: DialogContinoue) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Thank you for logging")
        builder.setMessage(
            "An online order account exists with this eMail Id for AAFES Restaurants.\n\nYou can:\n" +
                    "Please click on Continue, to use the same account (You will have to verify your password to associate your Google account with the existing account for online ordering. This will have to be done only once.)\n\n" +
                    "Please click on cancel,if you do not own this online ordering account. We regret that you will not be able to use your Google login."
        )
        builder.setPositiveButton("Continue") { dialog, which ->
            Log.e("Which", "$which")
            dialog.cancel()
            dialogContinoue.onContinoue(request)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Log.e("Which", "$which")
            dialog.cancel()
        }
        builder.show()
    }

    fun ViewImage(mContext:Context, img: String?, isCompleteUrl: Boolean) {
        val dialog = Dialog(mContext, R.style.Restrurent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogAlertBinding: DialogViewImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext.applicationContext),
            R.layout.dialog_view_image,
            null,
            false
        )
        dialog.setCancelable(true)
        dialog.setContentView(dialogAlertBinding.root)

        if (isCompleteUrl) {
            LoadImageWithOutBaseUrl(
                dialogAlertBinding.imaggeview,
                img
            )
        }else{
            ItemLoadImage(
                dialogAlertBinding.imaggeview,
                img
            )

        }

        dialogAlertBinding.backarrowIV.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }


        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
    }


}