package com.exchange.user.di_module.modules.activity_module.feedback_module

import com.exchange.user.module.feedback_module.FeedbackActivity
import com.exchange.user.module.feedback_module.adapter.DeliveryQuestionAdapter
import com.exchange.user.module.feedback_module.adapter.QuestionAdapter
import dagger.Module
import dagger.Provides

@Module
class FeedbackModule {

    @Provides
    fun provideCoupenListAdapter(fragment : FeedbackActivity): QuestionAdapter {
        return QuestionAdapter(fragment,ArrayList())
    }
    @Provides
    fun provideDeliveryQuestionAdapter(fragment : FeedbackActivity): DeliveryQuestionAdapter {
        return DeliveryQuestionAdapter(fragment,ArrayList())
    }
}