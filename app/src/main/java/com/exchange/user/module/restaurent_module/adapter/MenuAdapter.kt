package com.exchange.user.module.restaurant_module.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.exchange.user.R
import com.exchange.user.databinding.AdapterMenuListBinding
import com.exchange.user.module.cart_module.model.action.AddItemsToCart
import com.exchange.user.module.cart_module.model.card_model.CartData
import com.exchange.user.module.cart_module.model.suggestiondata.Suggestion
import com.exchange.user.module.restaurent_module.adapter.MenuItems
import com.exchange.user.module.restaurent_module.model.data_model.reastaurent_mode.RestrurentModel
import com.exchange.user.module.restaurent_module.model.menu_model.CatList
import com.exchange.user.module.restaurent_module.model.menu_model.ImageRepository
import com.exchange.user.module.utility_module.BuilderManager
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter

class MenuAdapter(
    var context: Context,
    var table: List<CatList>,
    val additmstocart: AddItemsToCart,
    searchItems: EditText,
    val cartdata: CartData,
    val suggestion: Suggestion,
    val restrurent: RestrurentModel,
    val imageRepository: List<ImageRepository>?
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){
    var searchItemsin : EditText = searchItems
    val animation = AnimationUtils.loadAnimation(context,R.anim.item_animation_blink)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val adaptermenulistbinding : AdapterMenuListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_menu_list, parent, false)
        return ViewHolder(adaptermenulistbinding)
    }
    override fun getItemCount(): Int {
        return table.size
    }
    override fun onBindViewHolder(viewholder : ViewHolder, position : Int) {
        val autoTransition = AutoTransition()
        autoTransition.duration=100
        viewholder.adaptermenulistbinding.menuname.text = table[viewholder.adapterPosition].catName
        val itemslist = BuilderManager.romoveItems(table.get(position).itemList,restrurent.timeZone!!)



        if ((table[viewholder.adapterPosition].openTime?:"").equals((table[viewholder.adapterPosition].closeTime?:""),true)){
            viewholder.adaptermenulistbinding.menutiming.visibility = View.GONE
        }else{
            val available = StringBuilder()
            available.append("available on ")
            available.append(BuilderManager.getFormateTime(table[viewholder.adapterPosition].openTime))
            available.append(" -to- ")
            available.append(BuilderManager.getFormateTime(table[viewholder.adapterPosition].closeTime))
            viewholder.adaptermenulistbinding.menutiming.text = available.toString()
            viewholder.adaptermenulistbinding.menutiming.visibility = View.VISIBLE
            viewholder.adaptermenulistbinding.menutiming.animation = animation
        }

        viewholder.adaptermenulistbinding.expandOption.setListener(object : ExpandableLayoutListenerAdapter(){
            override fun onOpened() {
                super.onOpened()
                TransitionManager.beginDelayedTransition(viewholder.adaptermenulistbinding.container,autoTransition)
                viewholder.adaptermenulistbinding.imageopen.visibility = View.VISIBLE
                viewholder.adaptermenulistbinding.imageclosed.visibility = View.GONE
            }

            override fun onClosed() {
                super.onClosed()
                TransitionManager.beginDelayedTransition(viewholder.adaptermenulistbinding.container,autoTransition)
                viewholder.adaptermenulistbinding.imageopen.visibility = View.GONE
                viewholder.adaptermenulistbinding.imageclosed.visibility = View.VISIBLE
            }
        })

        if (itemslist.isEmpty()) {
            viewholder.adaptermenulistbinding.menunamelay.visibility = View.GONE
        } else {
            val menuItemsAdapter = MenuItems(context,itemslist,
                itemslist, additmstocart, table.get(position), cartdata, suggestion,table[viewholder.adapterPosition].isShowItemImages?:"False",
                imageRepository
            )
            viewholder.adaptermenulistbinding.menuItemsRecycle.layoutManager = LinearLayoutManager(context)
            viewholder.adaptermenulistbinding.menuItemsRecycle.adapter = menuItemsAdapter
            searchItemsin.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    menuItemsAdapter.filter.filter(s.toString())
                    if (menuItemsAdapter.itemCount == 0) {
                        notifyItemChanged(viewholder.adapterPosition)
                        viewholder.adaptermenulistbinding.menunamelay.visibility = View.GONE
                    } else {
                        viewholder.adaptermenulistbinding.menunamelay.visibility = View.VISIBLE
                        notifyItemChanged(viewholder.adapterPosition)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })

        }

        viewholder.adaptermenulistbinding.menunamelay.setOnClickListener {
             if (viewholder.adaptermenulistbinding.expandOption.isExpanded){
                 viewholder.adaptermenulistbinding.expandOption.collapse()
             }else{
                 viewholder.adaptermenulistbinding.expandOption.expand()
             }
        }

        Handler(Looper.getMainLooper()).postDelayed({

        if (viewholder.adaptermenulistbinding.expandOption.isExpanded && viewholder.adapterPosition!=0){
            viewholder.adaptermenulistbinding.expandOption.collapse()
        }

        },500)

    }


    inner class ViewHolder(val adaptermenulistbinding : AdapterMenuListBinding) : RecyclerView.ViewHolder(adaptermenulistbinding.root)
}