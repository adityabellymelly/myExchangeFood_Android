package com.exchange.user.module.feedback_module

import android.content.Context
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_model.PostData
import com.exchange.user.module.base_module.base_model.ResponceModel
import com.exchange.user.module.base_module.base_model.ResponceModelSecond
import com.exchange.user.module.base_module.base_view.BaseViewModel
import com.exchange.user.module.feedback_module.model.FeedBackRequest
import com.exchange.user.module.feedback_module.model.QuestionModel
import com.exchange.user.module.feedback_module.model.ServiceRequest
import com.exchange.user.module.utility_module.ApplicationSharePrefrence
import com.exchange.user.module.utility_module.ApplicationUtility
import com.exchange.user.module.utility_module.BuilderManager
import com.exchange.user.module.utility_module.Validations
import com.exchange.user.module.web_module.ApplicationRequest
import com.exchange.user.rx_module.SchedulerProvider
import com.google.gson.GsonBuilder
import java.lang.System.*
import java.util.ArrayList

class FeedbackViewModel(applicationprefrence: ApplicationSharePrefrence,
                        schedulerProvider : SchedulerProvider,
                        applicationutility : ApplicationUtility,
                        applicationcontext : Context,
                        validations  : Validations,
                        applicationrequest : ApplicationRequest
) : BaseViewModel(applicationprefrence,schedulerProvider, applicationutility, applicationcontext , validations , applicationrequest){


    fun onBack(){
        (getNavigator() as FeedBackNavigation).onBackPress()
    }

    fun submitFeedback(){
        (getNavigator() as FeedBackNavigation).submitFeedBack()
    }

    fun getUserName(): String?  = getApplicationSharePref().getUserName()
    fun getPhoneNumber(): String?  = getApplicationSharePref().getPhoneNumber()
    fun getEmail(): String?  = getApplicationSharePref().getEmail()

    fun getQuestiondata(): ArrayList<QuestionModel>{
        val question =  ArrayList<QuestionModel>()
        question.add(QuestionModel(ConstantCommand.QUESTION_ONE,false,0.0F))
        question.add(QuestionModel(ConstantCommand.QUESTION_TWO,false,0.0F))
        question.add(QuestionModel(ConstantCommand.QUESTION_THREE,false,0.0F))
        question.add(QuestionModel(ConstantCommand.QUESTION_FOUR,false,0.0F))
        question.add(QuestionModel(ConstantCommand.QUESTION_FIVE,false,0.0F))
        return  question
    }

    fun sendServiceFeedback(serviceFeedbackRequest: ServiceRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            serviceFeedbackRequest.tid = "463b7b6b-7c26-443f-9bda-67cb2a85980a"
            currentTimeMillis().also { serviceFeedbackRequest.dt = it }
            val request = BuilderManager.encodeString(GsonBuilder().setPrettyPrinting().create().toJson(serviceFeedbackRequest))

            getCompositeDisposable().add(
                getApplicationRequest().sendServiceFeedback(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModelSecond ->
                        if (responce.serviceStatus.equals("S", true)) {
                            (getNavigator() as FeedBackNavigation).onSubmitRate()
                            (getNavigator() as FeedBackNavigation).showFeedbackMessage("Thanks For Your Rate")
                        } else {
                            responce.message?.let {
                                (getNavigator() as FeedBackNavigation).showFeedbackMessage(it)
                            }
                        }
                        hideProgress()
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as FeedBackNavigation).showFeedbackMessage(
                                    it
                                )
                            }
                        }

                    ))

        }
        else {
            (getNavigator() as FeedBackNavigation).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

    fun submitFeedBack(feedbackrequest: FeedBackRequest) {
        if (getApplicationUtility().isInternetConnected()) {
            showProgress()
            feedbackrequest.tid = CartDataInt.orderdata.tId
            val request = BuilderManager.encodeString(GsonBuilder().setPrettyPrinting().create().toJson(feedbackrequest))
            getCompositeDisposable().add(
                getApplicationRequest().savefeedBack(PostData(request)).doOnSuccess { }
                    .subscribeOn(getScheduleProvider().io())
                    .observeOn(getScheduleProvider().ui())
                    .subscribe({ responce: ResponceModel ->
                        if (responce.serviceStatus.equals("S", true)) {
                            (getNavigator() as FeedBackNavigation).onSubmitRate()
                            (getNavigator() as FeedBackNavigation).showFeedbackMessage("Thanks For Your Rate")
                        } else {
                            responce.message?.let {
                                (getNavigator() as FeedBackNavigation).showFeedbackMessage(it)
                            }
                        }
                        hideProgress()
                    },
                        { throwable: Throwable? ->
                            hideProgress()
                            throwable?.message?.let {
                                (getNavigator() as FeedBackNavigation).showFeedbackMessage(
                                    it
                                )
                            }
                        }

                    ))
        }
        else {
            (getNavigator() as FeedBackNavigation).showFeedbackMessage(getContext().getString(R.string.warning_no_internet))
        }
    }

}