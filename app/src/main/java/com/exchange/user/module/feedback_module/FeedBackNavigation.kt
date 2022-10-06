package com.exchange.user.module.feedback_module

import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface FeedBackNavigation : CommonActivityOrFragmentView {

    fun onBackPress()
    fun submitFeedBack()
    fun onSubmitRate()
}