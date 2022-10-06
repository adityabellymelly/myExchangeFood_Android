package com.exchange.user.module.about_us_module

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.FragmentAboutUsBinding
import com.exchange.user.module.about_us_module.model.DeliveryProgramResponse
import com.exchange.user.module.base_module.BaseFragment
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_command.CartDataInt
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.home_module.home_fragment.adapter.RestaruentAdapter
import com.exchange.user.module.restaurent_module.ResturantActivity
import com.exchange.user.module.signin_module.SignInActivity
import javax.inject.Inject

class AboutUsFragment  : BaseFragment<FragmentAboutUsBinding,AboutUsViewModel>(),AboutUsNavigator{
    @Inject
    lateinit var factory : ViewModelProviderFactory
    private lateinit var aboutUsViewModel : AboutUsViewModel
    private lateinit var fragmentaboutusBinding: FragmentAboutUsBinding
    override val bindingVariable: Int get() = BR.aboutUsViewModel
    override val layoutId: Int get() = R.layout.fragment_about_us
    
    override val viewModel: AboutUsViewModel
        get() {
            aboutUsViewModel = ViewModelProvider(this, factory).get(AboutUsViewModel::class.java)
            return aboutUsViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(getContaxt(), R.color.white_color)
        aboutUsViewModel.setNavigator(this)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        initData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentaboutusBinding = viewDataBinding
        initData()
    }
    override fun initData() {
        if (aboutUsViewModel.getUserID()!=null){
           aboutUsViewModel.getAboutUS()
            fragmentaboutusBinding.data.visibility = View.GONE
            fragmentaboutusBinding.view.visibility = View.VISIBLE
        }else{
            fragmentaboutusBinding.data.visibility = View.VISIBLE
            fragmentaboutusBinding.view.visibility = View.GONE
        }
    }
    override fun updateAbout(response: DeliveryProgramResponse) {
//        val htmlAsSpannedTitle = HtmlCompat.fromHtml(response.deliveryprogram?.title!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
//        val htmlAsSpannedDec = HtmlCompat.fromHtml(response.deliveryprogram?.desc!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
//        fragmentaboutusBinding.aboutTitel.text = htmlAsSpannedTitle
        fragmentaboutusBinding.moreabout.loadDataWithBaseURL(null, response.deliveryprogram?.desc!!, "text/html", "utf-8", null)

    }
    override fun openSiginActivity() {
        startActivity(Intent(getContaxt(), SignInActivity::class.java))
    }
    override fun showFeedbackMessage(message: String) {
        showBaseToast(fragmentaboutusBinding.root,message)
    }
}