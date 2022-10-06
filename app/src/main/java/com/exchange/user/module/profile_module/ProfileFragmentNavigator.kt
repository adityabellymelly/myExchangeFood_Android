package com.exchange.user.module.profile_module

interface ProfileFragmentNavigator {
    fun openOptions()
  fun changeLocation()
    fun openSiginActivity()
    fun openEditActivity()
    fun openChangePassword()
    fun updateUI()
    fun onOrderhistory()
    fun inviteFriend()
    fun showFeedbackMessage(message: String)
}