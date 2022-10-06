//package com.exchange.user.module.cart_module.adapter
//
//import android.content.Context
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.exchange.user.R
//import com.exchange.user.databinding.AdapterSelectedItemsBinding
//import com.exchange.user.module.cart_module.model.action.CartUpdate
//import com.exchange.user.module.utility_module.BuilderManager
//import java.lang.Exception
//
//
//class SelectedList(
//    var context: Context,
//    var itemListselect: ArrayList<CartSelected>,
//    var  cartupdate : CartUpdate
//) : RecyclerView.Adapter<SelectedList.ViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
//        val adapterSelectedItemsBinding : AdapterSelectedItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_selected_items, parent, false)
//        return ViewHolder(adapterSelectedItemsBinding)
//    }
//
//    override fun getItemCount(): Int {
//        return itemListselect.size
//    }
//
//    override fun onBindViewHolder(viewholder : ViewHolder, position : Int) {
//        try {
//            viewholder.adapterSelectedItemsBinding.actualprice.text = BuilderManager.getFormat().format(
//                itemListselect.get(viewholder.adapterPosition).selectedamout!!.unitPrice
//            )
//
//        }catch (e : Exception){}
//        viewholder.adapterSelectedItemsBinding.quantityCount.text = itemListselect.get(viewholder.adapterPosition).selectedamout!!.qty.toString()
//        viewholder.adapterSelectedItemsBinding.itemsName.text = itemListselect.get(viewholder.adapterPosition).itemList!!.name
//        //viewholder.adapterSelectedItemsBinding.addonitems.text = itemListselect.get(viewholder.adapterPosition).extraselect
//
////        if (!itemListselect.get(viewholder.adapterPosition).itemList!!.img!!.isEmpty()) {
////            viewholder.adapterSelectedItemsBinding.itemImage.visibility = View.GONE
////            BuilderManager.ItemLoadImage(viewholder.adapterSelectedItemsBinding.itemImage, itemListselect.get(viewholder.adapterPosition).itemList!!.img)
////        }
////        else{
////            viewholder.adapterSelectedItemsBinding.itemImage.visibility = View.GONE
////        }
//
//        if (!itemListselect.get(viewholder.adapterPosition).selectedamout!!.itemAddOnList.isNullOrEmpty())
//        {
//            var addon = ""
//            for (k in 0 until itemListselect.get(viewholder.adapterPosition).selectedamout!!.itemAddOnList.size) {
//                for (j in 0 until itemListselect.get(viewholder.adapterPosition).selectedamout!!.itemAddOnList.get(k).addOnOptions.size ){
//
//                    addon = addon.plus("/").plus(itemListselect.get(viewholder.adapterPosition).selectedamout!!.itemAddOnList.get(k).addOnOptions.get(j).name)
//                }
//            }
//
//            if(addon.isEmpty()){
//                viewholder.adapterSelectedItemsBinding.addonitems.visibility = View.GONE
//                viewholder.adapterSelectedItemsBinding.addon.visibility = View.GONE
//
//
//            }
//            else{
//                viewholder.adapterSelectedItemsBinding.addonitems.visibility = View.VISIBLE
//                viewholder.adapterSelectedItemsBinding.addon.visibility = View.VISIBLE
//               // viewholder.adapterSelectedItemsBinding.addonitems.text = addon.substring(1)
//            }
//
//        }else{
//            viewholder.adapterSelectedItemsBinding.addon.visibility = View.GONE
//            viewholder.adapterSelectedItemsBinding.addonitems.visibility = View.GONE
//        }
//
//
//        viewholder.adapterSelectedItemsBinding.QuantityIncressed.setOnClickListener( object: View.OnClickListener {
//            override fun onClick(v: View?) {
//                try {
//                    var quality = viewholder.adapterSelectedItemsBinding.quantityCount.text.toString().toInt()
//                if (quality < 10) {
//                    val dec = ++quality
//                    viewholder.adapterSelectedItemsBinding.quantityCount.text = dec.toString()
////                    val id = itemListselect.get(viewholder.adapterPosition).selectedamout!!.id
////                    val qyt = dec
////                    val amout =  (itemListselect.get(viewholder.adapterPosition).selectedamout!!.unitPrice!!.toInt() * dec)
//                    //cartupdate.onIncreageQyt(id,qyt,amout)
//                }
//                }catch (e : Exception){
//                    e.printStackTrace()
//                }
//            }
//        })
//
//        viewholder.adapterSelectedItemsBinding.QualityDecreasee.setOnClickListener( object: View.OnClickListener{
//            override fun onClick(v: View?) {
//
//                try {
//                    var quality  = viewholder.adapterSelectedItemsBinding.quantityCount.text.toString().toInt()
//                if (quality > 0) {
//                    val dec = -- quality
//                    viewholder.adapterSelectedItemsBinding.quantityCount.text = dec.toString()
////                    val id = itemListselect.get(viewholder.adapterPosition).selectedamout!!.id
////                    val qyt = dec
////                    val amout =  (itemListselect.get(viewholder.adapterPosition).selectedamout!!.unitPrice!!.toInt() * dec)
//
//                    if (dec ==0){
//                       // cartupdate.onRemoveItems(id,qyt,amout,viewholder.adapterPosition)
//                    }
//                    else{
//                       // cartupdate.onDecressQyt(id,qyt,amout)
//                    }
//                }
//                }catch (e : Exception){
//                    e.printStackTrace()
//                }
//            }
//
//        })
//    }
//
//    fun remove(adapterPosition: Int) {
//        itemListselect.removeAt(adapterPosition)
//        notifyDataSetChanged()
//       // notifyItemRangeChanged(adapterPosition,itemListselect.size)
//    }
//
//    inner class ViewHolder(val adapterSelectedItemsBinding : AdapterSelectedItemsBinding) : RecyclerView.ViewHolder(adapterSelectedItemsBinding.root)
//
//}