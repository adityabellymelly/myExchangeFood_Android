package com.exchange.user.module.order_history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterOrderHistoryItemsBinding
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.order_info_module.OrderInfoActivity
import com.exchange.user.module.order_info_module.model.DeliveryStatus
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.exchange.user.module.profile_module.model.OrderHistory
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.dialog_commands.CallApi
import com.exchange.user.module.utility_module.dialog_commands.TrackOrderCallBack
import com.google.gson.GsonBuilder

class OrderHistoryListAdapter(var context: Context, private var orderHistory: List<OrderHistory>):
    RecyclerView.Adapter<OrderHistoryListAdapter.ViewHolder>() {

    lateinit var  callapi : CallApi

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterOrderHistoryItemsBinding : AdapterOrderHistoryItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_order_history_items, parent, false)
        return ViewHolder(adapterOrderHistoryItemsBinding)
    }
    override fun getItemCount(): Int {
        return orderHistory.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.adapterOrderHistoryItemsBinding.nameofretru.text = orderHistory[holder.adapterPosition].orderNumber
        holder.adapterOrderHistoryItemsBinding.orderamot.text = BuilderManager.getFormat().format(orderHistory[holder.adapterPosition].totalAmt)
        holder.adapterOrderHistoryItemsBinding.creteon.text = "Ordered On - ${orderHistory[holder.adapterPosition].createdOn}"
        holder.adapterOrderHistoryItemsBinding.ordertype.text = "Order Type : ${orderHistory[holder.adapterPosition].restService}"
        holder.adapterOrderHistoryItemsBinding.orderstatus.text = orderHistory[holder.adapterPosition].status

        when(orderHistory[holder.adapterPosition].paymentType){
            "$$"->{
                holder.adapterOrderHistoryItemsBinding.payment.text = "Payment - Cash Payment"
            }
            "CC"->{
                holder.adapterOrderHistoryItemsBinding.payment.text = "Payment - ${context.getString(R.string.debit_credit_card)}"
            }
            "STAR"->{
                holder.adapterOrderHistoryItemsBinding.payment.text = "Payment - ${context.getString(R.string.miltry_star_card)}"
            }
            "POP"->{
                holder.adapterOrderHistoryItemsBinding.payment.text = "Payment - Pay on Pickup"
            }
            else->{
            holder.adapterOrderHistoryItemsBinding.payment.text = "Payment - ${orderHistory[holder.adapterPosition].paymentType}"
            }
        }

        when(orderHistory[holder.adapterPosition].status){
            "Open"->{
                holder.adapterOrderHistoryItemsBinding.orderstatus.text ="In Progress"
                holder.adapterOrderHistoryItemsBinding.orderstatus.setTextColor(ContextCompat.getColor(context, R.color.colorYellow))
                holder.adapterOrderHistoryItemsBinding.trackorder.visibility = View.VISIBLE
            }
            "Confirmed"->{
                holder.adapterOrderHistoryItemsBinding.orderstatus.text ="Confirmed"
                holder.adapterOrderHistoryItemsBinding.orderstatus.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                holder.adapterOrderHistoryItemsBinding.trackorder.visibility = View.GONE
            }
            "Canceled"->{
                holder.adapterOrderHistoryItemsBinding.orderstatus.text ="Canceled"
                holder.adapterOrderHistoryItemsBinding.orderstatus.setTextColor(ContextCompat.getColor(context, R.color.color_red))
                holder.adapterOrderHistoryItemsBinding.trackorder.visibility = View.GONE
            }
            "Delivered"->{
                when (orderHistory[holder.adapterPosition].restService){
                    "On-base Delivery" ->{
                        holder.adapterOrderHistoryItemsBinding.orderstatus.text = "Delivered"
                    }
                    "Off-base Delivery"->{
                        holder.adapterOrderHistoryItemsBinding.orderstatus.text = "Delivered"
                    }
                   else ->{
                        holder.adapterOrderHistoryItemsBinding.orderstatus.text = "Complete"
                    }
                }
                holder.adapterOrderHistoryItemsBinding.orderstatus.setTextColor(ContextCompat.getColor(context, R.color.color_green))
                holder.adapterOrderHistoryItemsBinding.trackorder.visibility = View.GONE
            }
        }
        if (orderHistory[holder.adapterPosition].locationName.isNullOrEmpty().not()){
            holder.adapterOrderHistoryItemsBinding.locationname.visibility = View.VISIBLE
            holder.adapterOrderHistoryItemsBinding.locationname.text = "Order From : ${orderHistory[holder.adapterPosition].locationName}"
        }else{
            holder.adapterOrderHistoryItemsBinding.locationname.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderInfoActivity::class.java)
            intent.putExtra(ConstantCommand.DATA,GsonBuilder().setPrettyPrinting()
                .create().toJson(orderHistory[holder.adapterPosition]))
            context.startActivity(intent)
        }

        holder.adapterOrderHistoryItemsBinding.trackorder.setOnClickListener {

            holder.adapterOrderHistoryItemsBinding.trackorder.text = "Loading.."
            holder.adapterOrderHistoryItemsBinding.trackorder.isEnabled = false
               callapi.callFunction(orderHistory[holder.adapterPosition],object :TrackOrderCallBack{
                   override fun onResult(
                       orderInfoResponse: OrderInfoModel,
                       deliveryStatus: DeliveryStatus?
                   ) {
                       holder.adapterOrderHistoryItemsBinding.trackorder.text = "Track Order"
                       holder.adapterOrderHistoryItemsBinding.trackorder.isEnabled = true
                       callapi.trackHistory(orderHistory[holder.adapterPosition],orderInfoResponse,deliveryStatus)
                   }

                   override fun onResult(
                       orderInfoResponse: OrderInfoModel,
                       deliveryStatus: DeliveryStatus?,
                       orderHistory: OrderHistory
                   ) {

                   }
               })
        }
    }

    fun addItems(orderHistory: ArrayList<OrderHistory>) {
       this.orderHistory =orderHistory
        notifyDataSetChanged()
    }

    fun setApiCallBack(callapi : CallApi){
        this.callapi = callapi
    }


    inner class ViewHolder(val adapterOrderHistoryItemsBinding : AdapterOrderHistoryItemsBinding) : RecyclerView.ViewHolder(adapterOrderHistoryItemsBinding.root)

}