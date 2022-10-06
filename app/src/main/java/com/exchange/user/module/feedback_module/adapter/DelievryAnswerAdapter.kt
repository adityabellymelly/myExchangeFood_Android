package com.exchange.user.module.feedback_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterAnswerItemsBinding
import com.exchange.user.module.order_info_module.model.Answer

class DelievryAnswerAdapter (val context: Context, var questions : ArrayList<Answer>) : RecyclerView.Adapter<DelievryAnswerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterOrderHistoryitemsBinding : AdapterAnswerItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_answer_items, parent, false)
        return ViewHolder(adapterOrderHistoryitemsBinding)
    }
    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterOrderHistoryitemsBinding.ansername.text = questions[holder.adapterPosition].OptionName
        holder.adapterOrderHistoryitemsBinding.ansername.setOnCheckedChangeListener { _, b ->
           // questions[holder.adapterPosition].isSelected = b
            changeItems(holder.adapterPosition,questions[holder.adapterPosition].isSelected)

        }
        holder.adapterOrderHistoryitemsBinding.ansername.setOnClickListener {
            questions[holder.adapterPosition].isSelected = !questions[holder.adapterPosition].isSelected
            holder.adapterOrderHistoryitemsBinding.ansername.isChecked =  questions[holder.adapterPosition].isSelected
        }

        holder.adapterOrderHistoryitemsBinding.ansername.isChecked = questions[holder.adapterPosition].isSelected
    }

    fun addItems(questions : ArrayList<Answer>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    fun changeItems(adapterPosition: Int, b: Boolean) {
        try {
            for ( i in questions.indices){
                questions[i].isSelected = i ==adapterPosition
                notifyItemChanged(i)
            }
        }catch (e:Exception){e.toString()}
    }

    inner class ViewHolder(val adapterOrderHistoryitemsBinding : AdapterAnswerItemsBinding) : RecyclerView.ViewHolder(adapterOrderHistoryitemsBinding.root)

}