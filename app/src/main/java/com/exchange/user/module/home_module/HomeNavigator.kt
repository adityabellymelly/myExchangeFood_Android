package com.exchange.user.module.home_module

import androidx.fragment.app.Fragment
import com.exchange.user.module.base_module.base_view.CommonActivityOrFragmentView

interface HomeNavigator : CommonActivityOrFragmentView {
    fun onChangeFragmant(fragment: Fragment)
}