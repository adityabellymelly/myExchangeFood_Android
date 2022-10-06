package com.exchange.user.module.cart_module.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bellymelly.user.module.restaurant_module.adapter.AddOnItemsOption
import com.exchange.user.R
import com.exchange.user.databinding.AdapterLikeItemsBinding
import com.exchange.user.databinding.ViewProductBinding
import com.exchange.user.module.cart_module.model.action.AddItemsFromSheet
import com.exchange.user.module.cart_module.model.action.AddItemsToCart
import com.exchange.user.module.cart_module.model.action.OnAddOnOption
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.card_model.ItemsAddonList
import com.exchange.user.module.cart_module.model.suggestiondata.SuggestItem
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnList
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.ItemList
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.KeyboardUtils
import com.google.gson.GsonBuilder
import com.rey.material.app.BottomSheetDialog
import java.util.*

class LikedItems(
    var context: Context,
    var sugesstionlist: ArrayList<SuggestItem>,
    val additmstocart: AddItemsToCart,
    val cartdata: CartData
) : RecyclerView.Adapter<LikedItems.ViewHolder>() {

    private var msg = ""

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adapterLikeItemsBinding : AdapterLikeItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_like_items, parent, false)
        return ViewHolder(adapterLikeItemsBinding)
    }

    override fun getItemCount(): Int {
        return if(sugesstionlist.size>6){
            6
        }else{
            sugesstionlist.size
        }
    }

    override fun onBindViewHolder(viewholder : ViewHolder, position : Int) {
        var positionid = 0L
        viewholder.adapterLikeItemsBinding.nameofitems.text = sugesstionlist[viewholder.adapterPosition].item!!.name!!

        val addItemsFromSheet = object : AddItemsFromSheet {
            @SuppressLint("SetTextI18n")
            override fun onAddFromSheet() {}

        }

        if (sugesstionlist.get(viewholder.adapterPosition).item!!.img!!.isNotEmpty()) {
            BuilderManager.ItemLoadImage(viewholder.adapterLikeItemsBinding.itemImage, sugesstionlist[viewholder.adapterPosition].item!!.img)
        }

        when {
            sugesstionlist.get(viewholder.adapterPosition).item!!.p1 >= 0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p1)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p1?.id?:0
            }
            sugesstionlist.get(viewholder.adapterPosition).item!!.p2 >0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p2)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p2?.id?:0
            }
            sugesstionlist.get(viewholder.adapterPosition).item!!.p3 >0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p3)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p3?.id?:0
            }
            sugesstionlist.get(viewholder.adapterPosition).item!!.p4 >0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p4)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p4?.id?:0
            }
            sugesstionlist.get(viewholder.adapterPosition).item!!.p5 >0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p5)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p5?.id?:0
            }
            sugesstionlist.get(viewholder.adapterPosition).item!!.p6 >0 -> {
                viewholder.adapterLikeItemsBinding.actualprice.text =
                    BuilderManager.getFormat().format(sugesstionlist.get(viewholder.adapterPosition).item!!.p6)
                positionid = sugesstionlist[viewholder.adapterPosition].catagoury?.p6?.id?:0
            }
            else -> viewholder.adapterLikeItemsBinding.actualprice.text = BuilderManager.getFormat().format(0)
        }

        viewholder.adapterLikeItemsBinding.addButton.setOnClickListener {
            BottemSheetDialog(context,
                sugesstionlist[viewholder.adapterPosition].item!!, additmstocart,
                viewholder.adapterLikeItemsBinding.add, sugesstionlist[viewholder.adapterPosition].catagoury!!, addItemsFromSheet,cartdata)
        }
    }



    @SuppressLint("SetTextI18n")
    fun BottemSheetDialog(context: Context, table: ItemList, additmstocart: AddItemsToCart, quantityCount: TextView, catlist: CatList, additemsfronsheet: AddItemsFromSheet, cartdata: CartData){
        // var addcheck = false
        val items = com.exchange.user.module.cart_module.model.card_model.ItemList()
        items.id = table.id
        items.name = table.name
        items.maxQ = table.maxQ?:1
        items.minQ = table.minQ?:1
        items.isTaxFree = table.isTaxFree?:true

        var positionotion = "p1"

        var addOnItemsOption : AddOnItemsOption? = null

        val dialog = BottomSheetDialog(context, R.style.SheetDialog)

        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)

        val viewproductbinding : ViewProductBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_product, null, false)
        dialog.setContentView(viewproductbinding.root)
        viewproductbinding.quantityCount.text = (table.minQ?:1).toString()
        viewproductbinding.itemsName.text = table.name
        items.qty =  viewproductbinding.quantityCount.text.toString().toLong()


        KeyboardUtils.addKeyboardToggleListener((context as Activity),object : KeyboardUtils.SoftKeyboardToggleListener{
            override fun onToggleSoftKeyboard(isVisible: Boolean) {
                if (isVisible){
                    dialog.heightParam(viewproductbinding.root.height+(viewproductbinding.root.height/2))
                }else{
                    dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                }

            }

        })

        if (table.specialOffer!=null){
            viewproductbinding.countItems.visibility =  View.GONE
            viewproductbinding.sizelay.visibility =  View.GONE
            if(calculateSpecialOffer(items,table.addOnList,table)){
                val amount = calculateSpecialOfferamount(table)
                viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
            }else{
                viewproductbinding.temstotel.text = ""
                viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
            }
        }else{
            viewproductbinding.countItems.visibility =  View.VISIBLE
            viewproductbinding.sizelay.visibility =  View.VISIBLE
        }

        if (table.icon1!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewproductbinding.vegIcon1,
                table.icon1
            )
        } else{
            viewproductbinding.vegIcon1.visibility = View.VISIBLE
        }

        if (table.icon2!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewproductbinding.vegIcon2,
                table.icon2
            )
        } else{
            viewproductbinding.vegIcon2.visibility = View.VISIBLE
        }

        if (table.icon3!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewproductbinding.vegIcon3,
                table.icon3
            )
        } else{
            viewproductbinding.vegIcon2.visibility = View.VISIBLE
        }

        if (table.icon4!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(viewproductbinding.vegIcon4,table.icon4)
        } else{
            viewproductbinding.vegIcon2.visibility = View.VISIBLE
        }

        val onaddoptioncomm =   object : OnAddOnOption {

            override fun onAddRemoveOption(addonlist: ItemsAddonList) {
                val x = items.itemAddOnList.indexOfFirst {it.itemAddOnId == addonlist.itemAddOnId}
                if (x!=-1)
                {
                    items.itemAddOnList.removeAt(x)

                }
                if (table.specialOffer!=null){

                    if(calculateSpecialOffer(items,table.addOnList,table)){
                        val amount = calculateSpecialOfferamount(table)
                        viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{

                    if (calcuateTotle(items,table.addOnList)){

                        if(items.portionId!=null){
                            val amount = calculateamount(items)
                            viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                            viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                        }else{
                            viewproductbinding.temstotel.text = ""
                            viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                        }

                    }
                    else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }

            }

            override fun onAddOnOption(addonlist: ItemsAddonList) {
                val x = items.itemAddOnList.indexOfFirst {it.itemAddOnId == addonlist.itemAddOnId}
                if (x==-1)
                {
                    items.itemAddOnList.add(addonlist)
                }
                else {
                    items.itemAddOnList[x] = addonlist
                }


                if (table.specialOffer!=null){

                    if(calculateSpecialOffer(items,table.addOnList,table)){
                        val amount = calculateSpecialOfferamount(table)
                        viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else {

                    if (calcuateTotle(items, table.addOnList)) {

                        if (items.portionId != null) {
                            val amount = calculateamount(items)
                            viewproductbinding.temstotel.text = "Item Total ".plus(
                                BuilderManager.getFormat().format(
                                    amount
                                )
                            )
                            viewproductbinding.addtobag.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_green
                                )
                            )
                        } else {
                            viewproductbinding.temstotel.text = ""
                            viewproductbinding.addtobag.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.dark_gray
                                )
                            )
                        }

                    } else {
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.dark_gray
                            )
                        )
                    }
                }
            }

        }

        if (table.addOnList.isNotEmpty()){
            viewproductbinding.itemsaddrecycle.layoutManager = LinearLayoutManager(context)
            addOnItemsOption = AddOnItemsOption(context,
                table.addOnList,onaddoptioncomm,catlist,viewproductbinding.quantityCount,"p1")
            viewproductbinding.itemsaddrecycle.adapter = addOnItemsOption
        }


        viewproductbinding.radiogrup.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {

                when {
                    checkedRadioButton.id == viewproductbinding.p1.id -> {
                        positionotion = "p1"
                        items.portionId = catlist.p1!!.id
                        items.unitPrice = table.p1

                    }
                    checkedRadioButton.id == viewproductbinding.p2.id -> {
                        positionotion = "p2"
                        items.portionId = catlist.p2!!.id
                        items.unitPrice = table.p2
                    }
                    checkedRadioButton.id == viewproductbinding.p3.id -> {
                        positionotion = "p3"
                        items.portionId =catlist.p3!!.id
                        items.unitPrice = table.p3
                    }
                    checkedRadioButton.id == viewproductbinding.p4.id -> {
                        positionotion = "p4"
                        items.portionId =catlist.p4!!.id
                        items.unitPrice = table.p4
                    }
                    checkedRadioButton.id == viewproductbinding.p5.id -> {
                        positionotion = "p5"
                        items.portionId =catlist.p5!!.id
                        items.unitPrice = table.p5

                    }
                }
                items.itemAddOnList = ArrayList()

                addOnItemsOption?.updateAdapter(positionotion)

                if (calcuateTotle(items,table.addOnList)){

                    if(items.portionId!=null){
                        val amount = calculateamount(items)
                        viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{
                    viewproductbinding.temstotel.text = ""
                    viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
            }
        }


        viewproductbinding.p1.visibility =  View.GONE
        viewproductbinding.p2.visibility =  View.GONE
        viewproductbinding.p3.visibility =  View.GONE
        viewproductbinding.p4.visibility =  View.GONE
        viewproductbinding.p5.visibility =  View.GONE

        if (table.p5 >=0) {
            catlist.p5?.let {
                viewproductbinding.p5.visibility =  View.VISIBLE
                viewproductbinding.p5.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p5
                ))
                viewproductbinding.p5.isChecked = true
            }

        }
        if (table.p4 >=0) {
            catlist.p4?.let {
                viewproductbinding.p4.visibility =  View.VISIBLE
                viewproductbinding.p4.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p4
                ))
                viewproductbinding.p4.isChecked = true
            }

        }
        if (table.p3 >=0) {
            catlist.p3?.let {
                viewproductbinding.p3.visibility =  View.VISIBLE
                viewproductbinding.p3.text = it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p3
                ))
                viewproductbinding.p3.isChecked = true
            }

        }
        if (table.p2 >=0) {
            catlist.p2?.let{
                viewproductbinding.p2.visibility =  View.VISIBLE
                viewproductbinding.p2.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p2
                ))
                viewproductbinding.p2.isChecked = true
            }

        }

        if (table.p1 >=0) {
            catlist.p1?.let {
                viewproductbinding.p1.visibility =  View.VISIBLE
                viewproductbinding.p1.text = it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p1
                ))
                viewproductbinding.p1.isChecked = true
            }
        }



        viewproductbinding.addtobag.setOnClickListener {
            val quality =  viewproductbinding.quantityCount.text.toString().toInt()
            if (table.specialOffer!=null){
                var spamont : Double ? = 0.0
                when(positionotion){
                    "p1"->{ spamont = table.specialOffer!!.amt1 }
                    "p2"->{ spamont = table.specialOffer!!.amt2 }
                    "p3"->{ spamont = table.specialOffer!!.amt3 }
                }
                if (cartdata.preTaxAmt>= spamont!!){
                    var amount = 0.0
                    if (items.itemAddOnList.isNotEmpty()){
                        for(a in 0 until items.itemAddOnList.size){
                            val addonoption = items.itemAddOnList[a].addOnOptions
                            for (b in 0 until addonoption.size){
                                amount += addonoption[b].amt
                            }
                        }
                    }
                    items.amt = ((items.unitPrice + amount))
                    items.qty = quality.toLong()
                    quantityCount.text = quality.toString()
                    items.specialInstructions = viewproductbinding.sepialInstruction.text.toString()
                    items.havespicialoffer = true
                    items.havespicialofferAmount = table.specialOffer!!.amt1?:0.0
                    items.categoryId = catlist.catId
                    val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
                    val jsonrestrurant: String = gson.toJson(items)
                    Log.e("Cart data -  ",jsonrestrurant)

                    val index =  cartdata.itemList.indexOfFirst { it.havespicialoffer }

                    if (index==-1){
                        additmstocart.onAddItemToCartPopUp(items)
                        quantityCount.text = quality.toString()
                        additemsfronsheet.onAddFromSheet()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(context,"You already have an offer items", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(context,"Minimum order should be ${BuilderManager.getFormat().format(spamont)}",
                        Toast.LENGTH_LONG).show()
                }

            }
            else{

                if (items.portionId==null){
                    Toast.makeText(context,"Please choose size", Toast.LENGTH_LONG).show()
                } else  if (quality<=0){
                    Toast.makeText(context,"Please add quntity.", Toast.LENGTH_LONG).show()
                }
                else if (!calcuateTotle(items,table.addOnList)){
                    Toast.makeText(context, msg,Toast.LENGTH_LONG).show()
                }else{

                    var amount = 0.0
                    if (items.itemAddOnList.isNotEmpty()){
                        for(a in 0 until items.itemAddOnList.size){
                            val addonoption = items.itemAddOnList[a].addOnOptions
                            for (b in 0 until addonoption.size){
                                amount += addonoption[b].amt
                            }
                        }
                    }

                    items.amt = (items.unitPrice + amount)
                    items.qty = quality.toLong()
                    quantityCount.text = quality.toString()
                    items.specialInstructions = viewproductbinding.sepialInstruction.text.toString()
                    items.categoryId = catlist.catId
                    val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
                    val jsonrestrurant: String = gson.toJson(items)
                    Log.e("Cart data -  ",jsonrestrurant)

                    additmstocart.onAddItemToCartPopUp(items)
                    quantityCount.text = quality.toString()
                    additemsfronsheet.onAddFromSheet()
                    dialog.dismiss()
                }
            }
        }

        viewproductbinding.QuantityIncressed.setOnClickListener {
            var quality = viewproductbinding.quantityCount.text.toString().toInt()
            if (quality < table.maxQ!!) {
                val dec = ++quality
                viewproductbinding.quantityCount.text = dec.toString()
                items.qty = dec.toLong()
                if (calcuateTotle(items,table.addOnList)){
                    if(items.portionId!=null){
                        val amount = calculateamount(items)
                        viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }
                }else{
                    viewproductbinding.temstotel.text = ""
                    viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
            }
        }
        viewproductbinding.QualityDecreasee.setOnClickListener {
            var quality  = (if (viewproductbinding.quantityCount.text.toString().isEmpty().not())
                viewproductbinding.quantityCount.text.toString()
            else "0" ).toInt()

            if (quality > table.minQ!!) {
                val dec = -- quality
                viewproductbinding.quantityCount.text = dec.toString()
                items.qty = dec.toLong()
                if (calcuateTotle(items,table.addOnList)){
                    if(items.portionId!=null){
                        val amount = calculateamount(items)
                        viewproductbinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewproductbinding.temstotel.text = ""
                        viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{
                    viewproductbinding.temstotel.text = ""
                    viewproductbinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }

            }
        }

        viewproductbinding.closse.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }



    private fun calculateSpecialOfferamount(table: ItemList): Double {
        return table.specialOffer?.amt1 ?:table.specialOffer?.amt2?:table.specialOffer?.amt3?:0.0
    }

    private fun calculateamount(items: com.exchange.user.module.cart_module.model.card_model.ItemList): Double {
        var amt = items.unitPrice
        for(i in 0 until items.itemAddOnList.size ){
            for(j in 0 until items.itemAddOnList[i].addOnOptions.size ){
                amt += items.itemAddOnList[i].addOnOptions[j].amt
            }
        }
        return amt.times(items.qty)
    }

    fun calculateSpecialOffer(
        items: com.exchange.user.module.cart_module.model.card_model.ItemList,
        table: List<AddOnList>?,
        table1: ItemList
    ): Boolean {
        val anount = table1.specialOffer?.amt1 ?:table1.specialOffer?.amt2?:table1.specialOffer?.amt3?:0.0
        if (!table.isNullOrEmpty()){
            for(i in table.indices){
                if (table[i].reqd.equals("T"))
                {
                    val x = items.itemAddOnList.indexOfFirst {
                        it.itemAddOnId == table[i].itemAddOnId
                    }
                    if (x == -1){
                        msg =  "Please Enter Require Items"
                        return false
                    }
                }
            }
        }
        if ( cartdata.preTaxAmt<= anount){
            return false

        }
        val index =  cartdata.itemList.indexOfFirst { it.havespicialoffer }
        if (index!=-1){
            return false
        }
        return true
    }

    fun calcuateTotle(items: com.exchange.user.module.cart_module.model.card_model.ItemList, table: List<AddOnList>?): Boolean{

        if (!table.isNullOrEmpty()){
            for(i in table.indices){
                if (table[i].reqd.equals("T"))
                {
                    val x = items.itemAddOnList.indexOfFirst {
                        it.itemAddOnId == table[i].itemAddOnId
                    }
                    if (x == -1){
                        msg =  "Please Enter Require Items"
                        return false
                    }
                }
            }
        }
        return checkMinMax(items,table)
    }

    fun checkMinMax(items: com.exchange.user.module.cart_module.model.card_model.ItemList, table: List<AddOnList>?): Boolean{
        if (!table.isNullOrEmpty()){
            for(i in table.indices){
                if (table[i].reqd.equals("T"))
                {
                    val itemaddonlist = items.itemAddOnList.find {
                        it.itemAddOnId == table[i].itemAddOnId
                    }
                    itemaddonlist?.let {

                        if (table[i].max?:0L==0L){
                            table[i].max = (table[i].addOnOptions?.size?:0).toLong()
                        }

                        if ((it.addOnOptions.size < ((table[i].min?:0L).toString()).toInt())){
                            msg = "Please select an option from ${table[i].name}. Select min ${table[i].min} options"
                            return false
                        }

                        if ((it.addOnOptions.size > ((table[i].max?:0L).toString()).toInt())){
                            msg = "Please select an option from ${table[i].name}. Select max ${table[i].max} options"
                            return false
                        }

                    }
                }else{

                    val itemaddonlist = items.itemAddOnList.find {
                        it.itemAddOnId == table[i].itemAddOnId
                    }
                    itemaddonlist?.let {

                        if (table[i].max?:0L==0L){
                            table[i].max = (table[i].addOnOptions?.size?:0).toLong()
                        }

                        if ((it.addOnOptions.size < ((table[i].min?:0L).toString()).toInt())){
                            msg = "Please select an option from ${table[i].name}. Select min ${table[i].min} options"
                            return false
                        }

                        if ((it.addOnOptions.size > ((table[i].max?:0L).toString()).toInt())){
                            msg = "Please select an option from ${table[i].name}. Select max ${table[i].max} options"
                            return false
                        }

                    }

                }
            }
        }
        return true
    }


    inner class ViewHolder(val adapterLikeItemsBinding : AdapterLikeItemsBinding) : RecyclerView.ViewHolder(adapterLikeItemsBinding.root)

}