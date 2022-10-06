package com.exchange.user.module.home_module.home_fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.FragmentHomeBinding
import com.exchange.user.module.base_module.BaseFragment
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import com.exchange.user.module.country_module.CountryActivity
import com.exchange.user.module.country_module.model.location_model.LocList
import com.exchange.user.module.home_module.home_fragment.adapter.RestaruentAdapter
import com.exchange.user.module.restaurent_module.ResturantActivity
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(),HomeFragmentNavigator,RestaruentAdapter.ReasturentListner {
    @Inject
    lateinit var factory : ViewModelProviderFactory
    @Inject
    lateinit var restaruentAdapter : RestaruentAdapter
    private lateinit var homefragmentViewModel : HomeFragmentViewModel
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    override val bindingVariable: Int get() = BR.homefragViewmodel
    override val layoutId: Int get() = R.layout.fragment_home

    override val viewModel: HomeFragmentViewModel
        get() {
            homefragmentViewModel = ViewModelProvider(this, factory).get(HomeFragmentViewModel::class.java)
            return homefragmentViewModel
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(getContaxt(), R.color.colorPrimary)
        homefragmentViewModel.setNavigator(this)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = viewDataBinding
        restaruentAdapter.setReasturentListner(this)
        initData()
    }

    override fun onStart() {
        super.onStart()
        fragmentHomeBinding.location.text = homefragmentViewModel.getLocation()
        if (homefragmentViewModel.getLocation().isEmpty()){
            fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.chooseyourlocation)
        }
        //chooseyourlocation
        else{
             val data = homefragmentViewModel.getLocation().split(",")
            if (data.isEmpty()){
                fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.chooseyourlocation)
            }
            else if (data.size==1){
                fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.chooseyourlocation)
            }
            else if (data.size>=2){
                if (data[0].isEmpty()){
                    fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.chooseyourlocation)
                }else{
                    fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.your_location)
                }
            }else{
                fragmentHomeBinding.sublocality.text =  mContext.getString(R.string.your_location)

            }

        }
    }
    override fun openCountryScreen() {
        startActivity(Intent(getContaxt(), CountryActivity:: class.java)
            .putExtra(ConstantCommand.DATA, ConstantCommand.FROM_HOME))
    }
    override fun updateRestrauent(locList: List<LocList>) {
        restaruentAdapter.addItems(locList)
    }
    override fun initData() {
        fragmentHomeBinding.restrurentrecycle.adapter = restaruentAdapter
    }
    override fun onSelectRestaruent(region: LocList) {
        startActivity(Intent(getContaxt(),ResturantActivity:: class.java)
            .putExtra(ConstantCommand.DATA,region))
    }
    override fun showFeedbackMessage(message: String) {
     showBaseToast(fragmentHomeBinding.root,message)
    }
}