package com.exchange.user.module.restaurent_module.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bellymelly.user.module.restaurant_module.adapter.AddOnItemsOption
import com.exchange.user.R
import com.exchange.user.databinding.AdapterMenuListItemsBinding
import com.exchange.user.databinding.ViewProductBinding
import com.exchange.user.module.cart_module.model.action.AddItemsFromSheet
import com.exchange.user.module.cart_module.model.action.AddItemsToCart
import com.exchange.user.module.cart_module.model.action.OnAddOnOption
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.card_model.ItemsAddonList
import com.exchange.user.module.cart_module.model.suggestiondata.SuggestItem
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.restaurent_module.model.menu_model.AddOnList
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.ImageRepository
import com.exchange.user.module.restaurent_module.model.menu_model.ItemList
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.KeyboardUtils
import com.google.gson.GsonBuilder
import com.rey.material.app.BottomSheetDialog


class MenuItems(
    var context: Context,
    var table: List<ItemList>,
    var originaltable: List<ItemList>,
    private val additmstocart: AddItemsToCart,
    val catlist: CatList,
    val cartdata: CartData,
    private val suggestion: Suggestion,
    private val isShowItemImages: String,
    private val imageRepository: List<ImageRepository>?
) : RecyclerView.Adapter<MenuItems.ViewHolder>(),Filterable {
    private val mFilter = ItemFilter()
    private var msg = ""


    override fun getFilter(): Filter {
        return mFilter
    }

    override fun onCreateViewHolder(parent : ViewGroup, p1: Int): ViewHolder {
        val adapterMenuListItemsbinding : AdapterMenuListItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_menu_list_items, parent, false)
        return ViewHolder(adapterMenuListItemsbinding)
    }

    override fun getItemCount(): Int {
        return table.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder : ViewHolder, p1: Int) {
        viewHolder.adapterMenuListItemsbinding.itemsName.text = table[viewHolder.adapterPosition].name
        viewHolder.adapterMenuListItemsbinding.addItems.visibility = View.VISIBLE
        viewHolder.adapterMenuListItemsbinding.countItems.visibility = View.GONE

        table[viewHolder.adapterPosition].desc?.let {
            viewHolder.adapterMenuListItemsbinding.discres.text = it
        }
        if ((isShowItemImages == "False").not()){
            if (table[viewHolder.adapterPosition].img.isNullOrEmpty().not()) {
                BuilderManager.ItemLoadImage(viewHolder.adapterMenuListItemsbinding.itemImage, table[viewHolder.adapterPosition].img)
            }else{
                val items  =  imageRepository?.find { it.POSItemId ==table[viewHolder.adapterPosition].POSItemId }
                if (items!=null){
                    if (items.POSItemImage.isNullOrEmpty().not()){
                        table[viewHolder.adapterPosition].img = items.POSItemImage
                        BuilderManager.ItemLoadImage(viewHolder.adapterMenuListItemsbinding.itemImage, items.POSItemImage)
                    }else{
                        viewHolder.adapterMenuListItemsbinding.imagecontainer.visibility = View.GONE
                    }
                    items.ItemDesc?.let {
                        viewHolder.adapterMenuListItemsbinding.discres.text = it
                        table[viewHolder.adapterPosition].desc=it
                    }
                }else{
                    viewHolder.adapterMenuListItemsbinding.imagecontainer.visibility = View.GONE
                }
            }
        }else{
            viewHolder.adapterMenuListItemsbinding.imagecontainer.visibility = View.GONE
        }
        viewHolder.adapterMenuListItemsbinding.imagecontainer.setOnClickListener {
            additmstocart.openImage(table[viewHolder.adapterPosition].img,table[viewHolder.adapterPosition].name)
        }

        if (table[viewHolder.adapterPosition].isShowforSuggestion.equals("True"))
        {
            val suggestitems = SuggestItem()
            suggestitems.catagoury = catlist
            suggestitems.item = table[viewHolder.adapterPosition]
            suggestion.sugesstionlist.add(suggestitems)
        }
        val itemsposition = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }
        if (itemsposition!=-1){
            when {
                catlist.p2!=null -> {
                    var quantity = 0
                    for ( x in 0 until cartdata.itemList.size){
                        if (cartdata.itemList[x].id == table[viewHolder.adapterPosition].id){
                            quantity += cartdata.itemList[x].qty.toInt()
                        }
                    }
                    viewHolder.adapterMenuListItemsbinding.add.text = "$quantity Added"
                }
                catlist.p3!=null -> {
                    var quantity = 0
                    for ( x in 0 until cartdata.itemList.size){
                        if (cartdata.itemList[x].id == table[viewHolder.adapterPosition].id){
                            quantity += cartdata.itemList[x].qty.toInt()
                        }
                    }
                    viewHolder.adapterMenuListItemsbinding.add.text = "$quantity Added"
                }
                else -> {
                    var quantity = 0
                    for ( x in 0 until cartdata.itemList.size){
                        if (cartdata.itemList[x].id == table[viewHolder.adapterPosition].id){

                            quantity+= cartdata.itemList[x].qty.toInt()
                        }
                    }
                    viewHolder.adapterMenuListItemsbinding.add.text = "$quantity Added"
                }
            }

        }

        val additemsfronsheet = object : AddItemsFromSheet {
            @SuppressLint("SetTextI18n")
            override fun onAddFromSheet() {
                var quantity = 0
                for ( x in 0 until cartdata.itemList.size){
                    if (cartdata.itemList[x].id == table[viewHolder.adapterPosition].id){

                        quantity += cartdata.itemList[x].qty.toInt()
                    }
                }

                viewHolder.adapterMenuListItemsbinding.add.text = "$quantity Added"
            }

        }


        viewHolder.itemView.setOnClickListener {
            viewHolder.adapterMenuListItemsbinding.addItems.visibility = View.VISIBLE
            viewHolder.adapterMenuListItemsbinding.countItems.visibility = View.GONE
            BottemSheetDialog(context, table[viewHolder.adapterPosition], additmstocart,
                viewHolder.adapterMenuListItemsbinding.quantityCount, catlist,additemsfronsheet,
                cartdata)
        }


        viewHolder.adapterMenuListItemsbinding.addItems.setOnClickListener {

            viewHolder.adapterMenuListItemsbinding.addItems.visibility = View.VISIBLE
            viewHolder.adapterMenuListItemsbinding.countItems.visibility = View.GONE
            BottemSheetDialog(
                context,
                table[viewHolder.adapterPosition],
                additmstocart,
                viewHolder.adapterMenuListItemsbinding.quantityCount,
                catlist,
                additemsfronsheet,
                cartdata
            )
        }

        when {
            table[viewHolder.adapterPosition].p1 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p1)
            table[viewHolder.adapterPosition].p2 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p2)
            table[viewHolder.adapterPosition].p3 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p3)
            table[viewHolder.adapterPosition].p4 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p4)
            table[viewHolder.adapterPosition].p5 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p5)
            table[viewHolder.adapterPosition].p6 >0 -> viewHolder.adapterMenuListItemsbinding.actualprice.text =
                BuilderManager.getFormat().format(table[viewHolder.adapterPosition].p6)
            else -> viewHolder.adapterMenuListItemsbinding.actualprice.text = BuilderManager.getFormat().format(0)
        }


        if (!table[viewHolder.adapterPosition].icon1.isNullOrEmpty()) {
            viewHolder.adapterMenuListItemsbinding.vegIcon1.visibility = View.VISIBLE
            BuilderManager.ItemVegLoadImage(viewHolder.adapterMenuListItemsbinding.vegIcon1, table[viewHolder.adapterPosition].icon1)
        }
        else{
            viewHolder.adapterMenuListItemsbinding.vegIcon1.visibility = View.GONE
        }

        if (!table[viewHolder.adapterPosition].icon2.isNullOrEmpty()) {
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.VISIBLE
            BuilderManager.ItemVegLoadImage(viewHolder.adapterMenuListItemsbinding.vegIcon2, table[viewHolder.adapterPosition].icon2)
        }
        else{
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.GONE
        }

        if (!table[viewHolder.adapterPosition].icon3.isNullOrEmpty()) {
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.VISIBLE
            BuilderManager.ItemVegLoadImage(viewHolder.adapterMenuListItemsbinding.vegIcon3, table[viewHolder.adapterPosition].icon3)
        }
        else{
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.GONE
        }

        if (!table[viewHolder.adapterPosition].icon4.isNullOrEmpty()) {
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.VISIBLE
            BuilderManager.ItemVegLoadImage(viewHolder.adapterMenuListItemsbinding.vegIcon4, table[viewHolder.adapterPosition].icon4)
        }
        else{
            viewHolder.adapterMenuListItemsbinding.vegIcon2.visibility = View.GONE
        }


        viewHolder.adapterMenuListItemsbinding.QuantityIncressed.setOnClickListener {
            var quality = viewHolder.adapterMenuListItemsbinding.quantityCount.text.toString().toInt()

            if (quality < table[viewHolder.adapterPosition].maxQ!!) {
                val dec = ++quality
                viewHolder.adapterMenuListItemsbinding.quantityCount.text = dec.toString()
                if (dec<=1) {
                    val items = com.exchange.user.module.cart_module.model.card_model.ItemList()
                    items.id = table[viewHolder.adapterPosition].id
                    items.name = table[viewHolder.adapterPosition].name
                    items.portionId = catlist.p1!!.id
                    items.unitPrice = table[viewHolder.adapterPosition].p1
                    items.qty = dec.toLong()
                    //items.amt = table[viewHolder.adapterPosition].p1!! * dec
                    items.specialInstructions = ""
                    items.categoryId = catlist.catId

                    val itemsition = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }

                    if (itemsition!=-1){
                        items.amt = cartdata.itemList[itemsition].unitPrice * dec
                        items.itemAddOnList  = cartdata.itemList[itemsition].itemAddOnList
                    }else{
                        items.amt = table[viewHolder.adapterPosition].p1 * dec
                        items.itemAddOnList = ArrayList()
                    }
                    additmstocart.onAddItemToCart(items)
                } else{

                    val items =com.exchange.user.module.cart_module.model.card_model.ItemList()
                    items.id = table[viewHolder.adapterPosition].id
                    items.name = table[viewHolder.adapterPosition].name
                    items.portionId =catlist.p1!!.id
                    items.unitPrice = table[viewHolder.adapterPosition].p1
                    items.qty = dec.toLong()
                    //items.amt = table[viewHolder.adapterPosition].p1!! * dec
                    items.specialInstructions = ""
                    items.categoryId = catlist.catId
                    val posit = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }

                    if (posit!=-1){
                        items.amt = cartdata.itemList[posit].unitPrice * dec
                        items.itemAddOnList  = cartdata.itemList[posit].itemAddOnList
                    }else{
                        items.amt = table[viewHolder.adapterPosition].p1 * dec
                        items.itemAddOnList = ArrayList()
                    }
                    additmstocart.onsetItemToCart(items)
                }
            }
        }

        viewHolder.adapterMenuListItemsbinding.QualityDecreasee.setOnClickListener {
            var quality  = (if (viewHolder.adapterMenuListItemsbinding.quantityCount.text.toString().isEmpty().not())
                viewHolder.adapterMenuListItemsbinding.quantityCount.text.toString()
            else "0" ).toInt()


            if (quality > 1) {
                val dec = -- quality
                viewHolder.adapterMenuListItemsbinding.quantityCount.text = dec.toString()
                if (dec==0) {
                    val items = com.exchange.user.module.cart_module.model.card_model.ItemList()
                    items.id = table[viewHolder.adapterPosition].id
                    items.name = table[viewHolder.adapterPosition].name
                    items.portionId = catlist.p1!!.id
                    items.unitPrice = table[viewHolder.adapterPosition].p1
                    items.qty = dec.toLong()
                    items.amt = table[viewHolder.adapterPosition].p1 * dec
                    items.specialInstructions = ""
                    items.categoryId = catlist.catId
                    val itmsposition = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }

                    if (itmsposition!=-1){
                        items.itemAddOnList  = cartdata.itemList[itmsposition].itemAddOnList
                    }else{
                        items.itemAddOnList = ArrayList()
                    }
                    additmstocart.onRemoveItemToCart(items)
                } else{

                    val items = com.exchange.user.module.cart_module.model.card_model.ItemList()
                    items.id = table[viewHolder.adapterPosition].id
                    items.name = table[viewHolder.adapterPosition].name
                    items.portionId = catlist.p1!!.id
                    items.unitPrice = table[viewHolder.adapterPosition].p1
                    items.qty = dec.toLong()
                    items.amt = table[viewHolder.adapterPosition].p1 * dec
                    items.specialInstructions = ""
                    items.categoryId = catlist.catId
                    val itmposition = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }

                    if (itmposition!=-1){
                        items.itemAddOnList  = cartdata.itemList[itmposition].itemAddOnList
                    }else{
                        items.itemAddOnList = ArrayList()
                    }
                    additmstocart.onsetItemToCart(items)
                }
            } else{
                viewHolder.adapterMenuListItemsbinding.quantityCount.text = "0"

                val autoTransition = AutoTransition()
                autoTransition.duration = 100
                TransitionManager.beginDelayedTransition(viewHolder.adapterMenuListItemsbinding.container,autoTransition)
                viewHolder.adapterMenuListItemsbinding.addItems.visibility = View.VISIBLE
                viewHolder.adapterMenuListItemsbinding.countItems.visibility = View.GONE
                val items =com.exchange.user.module.cart_module.model.card_model.ItemList()
                items.id = table[viewHolder.adapterPosition].id
                items.name = table[viewHolder.adapterPosition].name
                items.portionId = catlist.p1!!.id
                items.unitPrice = table[viewHolder.adapterPosition].p1
                items.qty = 0
                items.amt = table[viewHolder.adapterPosition].p1 * 0
                items.specialInstructions = ""
                items.categoryId = catlist.catId
                val itempositn = cartdata.itemList.indexOfFirst { it.id == table[viewHolder.adapterPosition].id }

                if (itempositn!=-1){
                    items.itemAddOnList  = cartdata.itemList[itempositn].itemAddOnList
                }else{
                    items.itemAddOnList = ArrayList()
                }
                additmstocart.onRemoveItemToCart(items)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun BottemSheetDialog(context: Context, table: ItemList, additmstocart: AddItemsToCart, quantityCount: TextView, catlist: CatList, additemsfronsheet: AddItemsFromSheet, cartdata: CartData){
        val items = com.exchange.user.module.cart_module.model.card_model.ItemList()
        items.id = table.id
        items.name = table.name
        items.maxQ = table.maxQ?:1
        items.minQ = table.minQ?:1
        items.isTaxFree = table.isTaxFree?:true

        var positionotion = "p1"

        var addOnItemsOption : AddOnItemsOption? = null

        val dialog = BottomSheetDialog(context, R.style.SheetDialog)

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent_color)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT).inDuration(400)

        val viewProductBinding : ViewProductBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_product, null, false)
        dialog.setContentView(viewProductBinding.root)

        viewProductBinding.discription.text = table.desc
        viewProductBinding.quantityCount.text = (table.minQ?:1).toString()
        viewProductBinding.itemsName.text = table.name
        items.qty =  viewProductBinding.quantityCount.text.toString().toLong()



        KeyboardUtils.addKeyboardToggleListener((context as Activity),object : KeyboardUtils.SoftKeyboardToggleListener{
            override fun onToggleSoftKeyboard(isVisible: Boolean) {
                if (isVisible){
                    dialog.heightParam(viewProductBinding.root.height+(viewProductBinding.root.height/2))
                }else{
                    dialog.heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                }

            }

        })

        if (table.specialOffer!=null){
            viewProductBinding.countItems.visibility =  View.GONE
            viewProductBinding.sizelay.visibility =  View.GONE
            if(calculateSpecialOffer(items,table.addOnList,table)){
                val amount = calculateSpecialOfferamount(table)
                viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
            }else{
                viewProductBinding.temstotel.text = ""
                viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
            }
        }else{
            viewProductBinding.countItems.visibility =  View.VISIBLE
            viewProductBinding.sizelay.visibility =  View.VISIBLE
        }

        if (table.icon1!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewProductBinding.vegIcon1,
                table.icon1
            )
        } else{
            viewProductBinding.vegIcon1.visibility = View.VISIBLE
        }

        if (table.icon2!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewProductBinding.vegIcon2,
                table.icon2
            )
        } else{
            viewProductBinding.vegIcon2.visibility = View.VISIBLE
        }

        if (table.icon3!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(
                viewProductBinding.vegIcon3,
                table.icon3
            )
        } else{
            viewProductBinding.vegIcon2.visibility = View.VISIBLE
        }

        if (table.icon4!!.isNotEmpty()) {
            BuilderManager.ItemVegLoadImage(viewProductBinding.vegIcon4,table.icon4)
        } else{
            viewProductBinding.vegIcon2.visibility = View.VISIBLE
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
                        viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{

                    if (calcuateTotle(items,table.addOnList)){

                        if(items.portionId!=null){
                            val amount = calculateamount(items)
                            viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                            viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                        }else{
                            viewProductBinding.temstotel.text = ""
                            viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                        }

                    }
                    else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
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
                        viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else {

                    if (calcuateTotle(items, table.addOnList)) {

                        if (items.portionId != null) {
                            val amount = calculateamount(items)
                            viewProductBinding.temstotel.text = "Item Total ".plus(
                                BuilderManager.getFormat().format(
                                    amount
                                )
                            )
                            viewProductBinding.addtobag.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_green
                                )
                            )
                        } else {
                            viewProductBinding.temstotel.text = ""
                            viewProductBinding.addtobag.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.dark_gray
                                )
                            )
                        }

                    } else {
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(
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
            viewProductBinding.itemsaddrecycle.layoutManager = LinearLayoutManager(context)
            addOnItemsOption = AddOnItemsOption(context,
                table.addOnList,onaddoptioncomm,catlist,viewProductBinding.quantityCount,"p1")
            viewProductBinding.itemsaddrecycle.adapter = addOnItemsOption
        }


        viewProductBinding.radiogrup.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<View>(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {

                when {
                    checkedRadioButton.id == viewProductBinding.p1.id -> {
                        positionotion = "p1"
                        items.portionId = catlist.p1!!.id
                        items.unitPrice = table.p1

                    }
                    checkedRadioButton.id == viewProductBinding.p2.id -> {
                        positionotion = "p2"
                        items.portionId = catlist.p2!!.id
                        items.unitPrice = table.p2
                    }
                    checkedRadioButton.id == viewProductBinding.p3.id -> {
                        positionotion = "p3"
                        items.portionId =catlist.p3!!.id
                        items.unitPrice = table.p3
                    }
                    checkedRadioButton.id == viewProductBinding.p4.id -> {
                        positionotion = "p4"
                        items.portionId =catlist.p4!!.id
                        items.unitPrice = table.p4
                    }
                    checkedRadioButton.id == viewProductBinding.p5.id -> {
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
                        viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{
                    viewProductBinding.temstotel.text = ""
                    viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
            }
        }


        viewProductBinding.p1.visibility =  View.GONE
        viewProductBinding.p2.visibility =  View.GONE
        viewProductBinding.p3.visibility =  View.GONE
        viewProductBinding.p4.visibility =  View.GONE
        viewProductBinding.p5.visibility =  View.GONE

        if (table.p5 >=0) {
            catlist.p5?.let {
                viewProductBinding.p5.visibility =  View.VISIBLE
                viewProductBinding.p5.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p5
                ))
                viewProductBinding.p5.isChecked = true
            }

        }
        if (table.p4 >=0) {
            catlist.p4?.let {
                viewProductBinding.p4.visibility =  View.VISIBLE
                viewProductBinding.p4.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p4
                ))
                viewProductBinding.p4.isChecked = true
            }

        }
        if (table.p3 >=0) {
            catlist.p3?.let {
                viewProductBinding.p3.visibility =  View.VISIBLE
                viewProductBinding.p3.text = it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p3
                ))
                viewProductBinding.p3.isChecked = true
            }

        }
        if (table.p2 >=0) {
            catlist.p2?.let{
                viewProductBinding.p2.visibility =  View.VISIBLE
                viewProductBinding.p2.text =it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p2
                ))
                viewProductBinding.p2.isChecked = true
            }

        }
        if (table.p1 >=0) {
            catlist.p1?.let {
                viewProductBinding.p1.visibility =  View.VISIBLE
                viewProductBinding.p1.text = it.name.plus(" "+ BuilderManager.getFormat().format(
                    table.p1
                ))
                viewProductBinding.p1.isChecked = true
            }
        }

        viewProductBinding.addtobag.setOnClickListener {
            val quality =  viewProductBinding.quantityCount.text.toString().toInt()
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
                    items.specialInstructions = viewProductBinding.sepialInstruction.text.toString()
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
                    items.specialInstructions = viewProductBinding.sepialInstruction.text.toString()
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
        viewProductBinding.QuantityIncressed.setOnClickListener {
            var quality = viewProductBinding.quantityCount.text.toString().toInt()
            if (quality < table.maxQ!!) {
                val dec = ++quality
                viewProductBinding.quantityCount.text = dec.toString()
                items.qty = dec.toLong()
                if (calcuateTotle(items,table.addOnList)){
                    if(items.portionId!=null){
                        val amount = calculateamount(items)
                        viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }
                }else{
                    viewProductBinding.temstotel.text = ""
                    viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }
            }
        }
        viewProductBinding.QualityDecreasee.setOnClickListener {
            var quality  = (if (viewProductBinding.quantityCount.text.toString().isEmpty().not())
                viewProductBinding.quantityCount.text.toString()
            else "0" ).toInt()

            if (quality > table.minQ!!) {
                val dec = -- quality
                viewProductBinding.quantityCount.text = dec.toString()
                items.qty = dec.toLong()
                if (calcuateTotle(items,table.addOnList)){
                    if(items.portionId!=null){
                        val amount = calculateamount(items)
                        viewProductBinding.temstotel.text = "Item Total ".plus(BuilderManager.getFormat().format(amount))
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.color_green))
                    }else{
                        viewProductBinding.temstotel.text = ""
                        viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                    }

                }else{
                    viewProductBinding.temstotel.text = ""
                    viewProductBinding.addtobag.setBackgroundColor(ContextCompat.getColor(context,R.color.dark_gray))
                }

            }
        }
        viewProductBinding.closse.setOnClickListener { dialog.dismiss() }

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




    inner class ViewHolder(val adapterMenuListItemsbinding : AdapterMenuListItemsBinding) : RecyclerView.ViewHolder(adapterMenuListItemsbinding.root)

    inner class ItemFilter : Filter() {
        @SuppressLint("DefaultLocale")
        override fun performFiltering(charSequence : CharSequence?): FilterResults {
            val results = FilterResults()
            if (charSequence.toString().isEmpty()) {
                results.values = originaltable
                results.count = originaltable.size
            } else {
                val list = originaltable
                val count = list.size
                val nlist = ArrayList<ItemList>(count)
                for (i in 0 until count) {
                    if (list[i].name.toString().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        nlist.add(list[i])
                    }
                }
                results.values = nlist
                results.count = nlist.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            table = results!!.values as ArrayList<ItemList>
            Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }

        }
    }
}