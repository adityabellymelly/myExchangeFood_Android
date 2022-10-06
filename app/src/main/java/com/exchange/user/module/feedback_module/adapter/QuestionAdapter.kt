package com.exchange.user.module.feedback_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.exchange.user.R
import com.exchange.user.databinding.AdapterQuestionItemsBinding
import com.exchange.user.module.feedback_module.model.QuestionModel

class QuestionAdapter(val context: Context, var questions : ArrayList<QuestionModel>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterOrderHistoryitemsBinding : AdapterQuestionItemsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_question_items, parent, false)
        return ViewHolder(adapterOrderHistoryitemsBinding)
    }
    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterOrderHistoryitemsBinding.question.text = questions[holder.adapterPosition].question
        holder.adapterOrderHistoryitemsBinding.reviewRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
            questions[holder.adapterPosition].rating = rating
        }
    }

    fun addItems(questions : ArrayList<QuestionModel>) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class ViewHolder(val adapterOrderHistoryitemsBinding : AdapterQuestionItemsBinding) : RecyclerView.ViewHolder(adapterOrderHistoryitemsBinding.root)

}