package com.exchange.user.module.feedback_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterDeliveryQuestionItemsBinding
import com.exchange.user.module.order_info_module.model.QuestionList

class DeliveryQuestionAdapter (val context: Context, var questions : ArrayList<QuestionList>) : RecyclerView.Adapter<DeliveryQuestionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val adapterOrderHistoryitemsBinding : AdapterDeliveryQuestionItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_delivery_question_items, parent, false)
        return ViewHolder(adapterOrderHistoryitemsBinding)
    }
    override fun getItemCount(): Int = questions.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.adapterOrderHistoryitemsBinding.question.text = questions[holder.adapterPosition]
           .feedbackQuestion?.QuestionName

        holder.adapterOrderHistoryitemsBinding.answerRecycle.adapter = DelievryAnswerAdapter(context,
            questions[holder.adapterPosition]
            .feedbackQuestion?.answers ?: ArrayList())

//        holder.adapterOrderHistoryitemsBinding.reviewRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
//            questions[holder.adapterPosition].rating = rating
//        }
    }

    fun addItems(questions : ArrayList<QuestionList>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterOrderHistoryitemsBinding : AdapterDeliveryQuestionItemsBinding) : RecyclerView.ViewHolder(adapterOrderHistoryitemsBinding.root)

}