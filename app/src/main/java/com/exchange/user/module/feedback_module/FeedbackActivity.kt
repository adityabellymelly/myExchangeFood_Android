package com.exchange.user.module.feedback_module

import android.os.Bundle
import android.view.ActionMode
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityFeedbackBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.feedback_module.adapter.DeliveryQuestionAdapter
import com.exchange.user.module.feedback_module.adapter.QuestionAdapter
import com.exchange.user.module.feedback_module.model.FeedBackData
import com.exchange.user.module.feedback_module.model.FeedBackModel
import com.exchange.user.module.feedback_module.model.FeedBackRequest
import com.exchange.user.module.feedback_module.model.ServiceRequest
import com.exchange.user.module.order_info_module.model.OrderInfoModel
import com.google.gson.Gson
import javax.inject.Inject

class FeedbackActivity : BaseActivity<ActivityFeedbackBinding, FeedbackViewModel>(),
    FeedBackNavigation {
    @Inject
    lateinit var factory: ViewModelProviderFactory
    @Inject
    lateinit var questionAdapter : QuestionAdapter
    @Inject
    lateinit var deliveryQuestionAdapter : DeliveryQuestionAdapter
    private var feedBackModel= FeedBackModel()
    private var serviceFeedbackRequest = ServiceRequest()
    private lateinit var feedbackViewModel:FeedbackViewModel
    lateinit var activityFeedbackBinding : ActivityFeedbackBinding
    private lateinit var orderinforesonce : OrderInfoModel
    override fun getLayoutId(): Int {
        return R.layout.activity_feedback
    }
    override fun getViewModel(): FeedbackViewModel {
        feedbackViewModel =  ViewModelProvider(this, factory).get(FeedbackViewModel::class.java)
        return feedbackViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.feedbackViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFeedbackBinding = getViewDataBinding()
        feedbackViewModel.setNavigator(this)
        feedbackViewModel.initProgress(this)
        initData()
    }
    override fun onActionModeStarted(mode: ActionMode?) {
        val menu = mode?.menu
        menu?.clear()
        menu?.removeItem(android.R.id.copy)
        menu?.removeItem(android.R.id.paste)
        menu?.removeItem(android.R.id.selectAll)
        super.onActionModeStarted(mode)
    }
    override fun initData() {
        orderinforesonce = Gson().fromJson(intent.getStringExtra(ConstantCommand.DATA), OrderInfoModel::class.java)
        feedBackModel.restLocationId =orderinforesonce.locationId
        feedBackModel.restaurantName = orderinforesonce.locationName
        feedBackModel.restaurantTelephone = orderinforesonce.locationTel

        feedBackModel.orderTotal=orderinforesonce.totalAmt
        feedBackModel.orderNumber=orderinforesonce.orderNumber
        feedBackModel.paymentMode= orderinforesonce.paymentType

        feedBackModel.customerEmail = feedbackViewModel.getEmail()
        feedBackModel.customerName = feedbackViewModel.getUserName()
        feedBackModel.customerTelephone = feedbackViewModel.getPhoneNumber()

        feedBackModel.restaurantAddress = orderinforesonce.address

        activityFeedbackBinding.orderdate.text = orderinforesonce.createdOn
        activityFeedbackBinding.orderno.text = "Order#: $${orderinforesonce.orderNumber.toString()}"
        activityFeedbackBinding.restroname.text = orderinforesonce.locationName
        activityFeedbackBinding.feedbackrcycle.adapter = questionAdapter
        activityFeedbackBinding.delievryfeedbackrcycle.adapter =deliveryQuestionAdapter
        questionAdapter.addItems(feedbackViewModel.getQuestiondata())
        when(orderinforesonce.restService){
            "On-base Delivery"->{
                activityFeedbackBinding.delievryLay.visibility = View.VISIBLE
                deliveryQuestionAdapter.addItems(orderinforesonce.questionList)
            }
            "Off-base Delivery"->{
                activityFeedbackBinding.delievryLay.visibility = View.VISIBLE
                deliveryQuestionAdapter.addItems(orderinforesonce.questionList)
            }
            else->{
                activityFeedbackBinding.delievryLay.visibility = View.GONE
            }
        }

        if (orderinforesonce.isDriverFeedbackSubmitted=="True"){
            activityFeedbackBinding.delievryLay.visibility = View.GONE
        }
    }

    override fun submitFeedBack() {
        feedBackModel.comment = activityFeedbackBinding.comment.text.toString()

        when(orderinforesonce.restService){
            "On-base Delivery"->{
                setFeedbackServiceAnser()
                serviceFeedbackRequest.OrderId = orderinforesonce.orderId.toString()
                feedbackViewModel.sendServiceFeedback(serviceFeedbackRequest)
                submitOrderFeedBack()
            }
            "Off-base Delivery"->{
                setFeedbackServiceAnser()
                serviceFeedbackRequest.OrderId = orderinforesonce.orderId.toString()
                feedbackViewModel.sendServiceFeedback(serviceFeedbackRequest)
                submitOrderFeedBack()
            }
            else->{
              submitOrderFeedBack()
            }
        }


    }

    private fun submitOrderFeedBack() {
        setAnwser()
        val feedbackrequest = FeedBackRequest()
        feedbackrequest.tid = CartDataInt.orderdata.tId
        val datavalue =  FeedBackData()
        datavalue.feedbackData = feedBackModel
        feedbackrequest.data = datavalue
        feedbackViewModel.submitFeedBack(feedbackrequest)
    }


    override fun onBackPress() {
        super.onBackPressed()
        finish()
    }

    override fun onSubmitRate() {
        onBackPress()
    }

    override fun showFeedbackMessage(message: String) {
        showBaseToast(activityFeedbackBinding.rootlay,message)
    }
    private fun setFeedbackServiceAnser() {
        val question = deliveryQuestionAdapter.questions
        for (i in 0 until question.size){
            when(i+1){
                1->{
                    serviceFeedbackRequest.feedback1Cnt=question[i].feedbackQuestion?.answers?.find { it.isSelected }?.OptionValue
                }
                2->{
                    serviceFeedbackRequest.feedback2Cnt=question[i].feedbackQuestion?.answers?.find { it.isSelected }?.OptionValue

                }
                3->{
                    serviceFeedbackRequest.feedback3Cnt=question[i].feedbackQuestion?.answers?.find { it.isSelected }?.OptionValue

                }
                4->{
                    serviceFeedbackRequest.feedback4Cnt=question[i].feedbackQuestion?.answers?.find { it.isSelected }?.OptionValue
                }
            }
        }
    }
    private fun setAnwser() {
        val quesion = questionAdapter.questions

        for (i in 0 until quesion.size){
            when(i+1){
                1->{
                    feedBackModel.answer1 =quesion[i].rating.toInt().toString()
                }
                2->{
                    feedBackModel.answer2 = quesion[i].rating.toInt().toString()

                }
                3->{
                    feedBackModel.answer3 = quesion[i].rating.toInt().toString()

                }
                4->{
                    feedBackModel.answer4 = quesion[i].rating.toInt().toString()
                }
                5->{
                    feedBackModel.answer5 = quesion[i].rating.toInt().toString()
                }
            }
        }

    }
}