package com.exchange.user.module.base_module

object ConstantCommand {
    const val PATMENT_GATWAY = "https://aafesprem.imenu360.com/proxy/AAFESPaymentGateway.aspx?_dt=1607577331817"
    //const val PATMENT_GATWAY2 = "http://staging.m4.imenu360.com/aafesproxy/AAFESPaymentGateway.aspx?_dt=1607590211286"

    const val PATMENT_GATWAY2 = "https://aafesprem.imenu360.com/proxy/AAFESPaymentGateway.aspx?_dt=1607577331817"
    const val BASE_URL_COUNTRY="https://aafesprem.imenu360.com/"
    const val BASE_DRIVER_URL ="https://restadmin.im360aafes.com/DeliveryAppAPI/DeliveryService.svc/"

    const val BASE_URL="https://aafesapi.imenu360.com/iMenu360Service.svc/"
//  const val BASE_URL="http://staging.m4.imenu360.com/aafesAPI/iMenu360Service.svc/"

    const val APP_TOKEN = "A16F9955-7648-44E2-AA81-3DDEEA7AA283"
    const val URl = "http://www.aafes.imenu360.mobi/?id"
    const val IMAGE_BASE_URL = "https://s3.amazonaws.com/v1.0/"
    const val COUNTRY_IMAGE_BASE_URL = "https://aafesprem.imenu360.com/images/"

    const val APP_NAME: String = "Exchange"
    const val  REQUEST_CAMERA = 234
    const val  SELECT_FILE = 564
    const val LOC_REQ_CODE = 1
    const val EMAIL_ERROR = 1
    const val PASSWORD_ERROR =2
    const val  MY_REQUEST_CODE = 64
    const val M0BILE_ERROR =3
    const val LOCATION_ERROR = 6
    const val USERNAME_ERROR =4
    const val FACEBOOK_LOGIN = 1
    const val GOOGLE_LOGIN = 2
    const val NORMAL_LOGIN = 3
    const val NO_ERROR =6
    // demo location
    const val urlName = "dallas"
    const val locName = "Exchange Test Restaurant"
    const val chainId = 266L
//    const val locId = "1234"

    // demo location 2
    const val urlName2 = "dallastest1"
//    const val locName2 = "AAFEStest1(DallasTest1)"
//    const val locId2 = "10057"
//
// demo location 2
const val urlName3 = "aafes"
//    const val locName2 = "zzExchange TEST SITE"
//    const val locId2 = "542"

    const val OKRESTULT ="okayresult"
    const val VALID_COUPON = "validcoupon"
    const val MENUDATA  = "menudata"
    const val FCM_TOKEN="fcm_token"
    const val TERMS_AND_CONDITIONS= "https://bellymelly.com/terms-condition.html"
    const val RESTRAURENT= "resturent"
    const val CART= "cart"
    const val PROFILE= "PROFILE"
    const val PRIVACY_POLICY = "https://bellymelly.com/privacy.html"
    const val REWARDPOINT ="rewardpoint"

    const val COUNTRY =  "country"
    const val COUNTRY_SELECTED =  "country_selected"

    const val LOCALDATA = "localdata"

    const val ADDRESS = "address"

    const val PREF_NAME="bellymellyprefrence"
    const val PHONE_NO = "PhoneNumber"
    const val USER_IDShare = "userid"
    const val USER_NAME = "username"
    const val EMAIL = "email"
    const val FIRST = "first"

    const val DATA = "data"
    const val DATA_2 = "data_2"
    const val FROM_CHOOSE = "from_choose"
    const val FROM_HOME = "from_home"

    const val FROM_FORGOT =7909
    const val FROM_CREATE_ACCOUNT =4539
    const val FROM_CREDIT_CART = 67654
    const val FROM_MILTRY_STAR = 4565678


    const val KEY_AMOUNT = "Amount="
    const val KEY_MENUID = "&MenuId="
    const val KEY_LOCATION = "&LocationId="
    const val KEY_MIL_STAR = "&isMilStar="
    const val KEY_TID = "&tid="
    const val KEY_TRACEID= "&traceid="
    const val KEY_DATA= "&data="
    const val INCOMING_NOTIFICATION = "incoming_notification"

   // ====api =====

    const val GET_NEW_TOKEN = "GetNewToken"
    const val FET_LOCATION_DETAIL = "GetLocationdetails"
    const val MENU_DETAIL = "GetMenuDetail"
    const val CHECK_GOOGLE_ACCOUNT_EXIST = "CheckGoogleAccountExists"
    const val CHECK__ACCOUNT_EXIST = "CheckAccountExists"
    const val DO_LOGIN = "DoLogin"
    const val SEND_FORGOTPASSWORD_OTP = "SendForgetPasswordOTP"
    const val RESET_PASSWORD_OTP = "ResetPassword_OTP"
    const val SEND_SIGNUP_OTP = "SendSignupOTP"
    const val UPDATE_USER_PROFILE = "SetUserProfile"
    const val CHANGE_PASSWORD = "UpdatePassword"
    const val NEWUSER = "NewUser"
    const val DOGOOGLE_LOGIN = "DoGoogleLogin"
    const val DOFB_LOGIN = "DoFBLogin"
    const val USER_COUPON ="GetUserCoupons"
    const val UPDATECOUPON = "UpdateCoupons"
    const val LAST_ORDER ="GetLastOrder"
    const val START_ORDER ="SetOrder"
    const val DO_LOGIN_BY_CUSTID ="DoLoginByCustId"
    const val SET_USER_ADDRESS = "SetUserAddress"
    const val GET_UNIQUE_IDENTIFIER = "GetUniqueIdentifier"
    const val DOMARGE_GOOGLE_DATA = "DoMergeGoogleData"
    const val GET_CUST_ORDERHISTORY = "GetCustOrderHistory"
    const val GET_ORDER_INF ="GetOrderInfo"
    const val SAVE_FEEDBACK = "SaveOrderFeedback"
    const val GET_DELIVERY_STATUS ="GetOrderDeliveryStatusForCustomer"
    const val SEND_SERVICE_FEEDBACK ="SendServiceFeedback"
    const val DELETE_USER_IOS = "DeleteUseriOS"

    const val COUNRTY_TEXT ="{\"country\":[{\"name\":\"Arizona\",\"link\":\"Arizona.json\",\"image\":\"map-arizona-new.png\"},{\"name\":\"Germany\",\"link\":\"Germany.json\",\"image\":\"map-germany-new.png\"},{\"name\":\"Guam\",\"link\":\"Guam.json\",\"image\":\"map-guam-new.png\"},{\"name\":\"Japan\",\"link\":\"Japan.json\",\"image\":\"map-mainland-japan-new.png\"},{\"name\":\"Okinawa\",\"link\":\"Okinawa.json\",\"image\":\"map-okinawa-japan-new.png\"},{\"name\":\"South Korea\",\"link\":\"SouthKorea.json\",\"image\":\"map-south-korea-new.png\"},{\"name\":\"United Kingdom\",\"link\":\"UnitedKingdom.json\",\"image\":\"map-united-kingdom-new.png\"},{\"name\":\"Texas\",\"link\":\"USA.json\",\"image\":\"map-texas-new.png\"}]}"

 var QUESTION_ONE = "How would you rate the quality of your food?"
 var QUESTION_TWO = "How would you rate the quality of our service"
 var QUESTION_THREE = "Was your order ready for pickup/or delivered in a timely fashion?"
 var QUESTION_FOUR= "Please rate the timeliness of the Pickup or Delivery service."
 var QUESTION_FIVE= "What is the likelihood that you'd use online versus telephone for your next order?"

  var TEST_LOCATION = "IHsiQ291bnRyeU5hbWUiOiAiQWRtaW4iLCJSZWdpb25MaXN0IjpbeyJSZWdpb25OYW1lIjogIlRlc3QgTG9jYXRpb25zIiwiTG9jTGlzdCI6W3siTG9jSWQiOiAiMTIzNCIsIkxvY05hbWUiOiAiRXhjaGFuZ2UgVGVzdCBSZXN0YXVyYW50IiwidXJsTmFtZSI6ICJkYWxsYXMifSx7IkxvY0lkIjogIjEwMDU3IiwiTG9jTmFtZSI6ICJBQUZFU3Rlc3QxKERhbGxhc1Rlc3QxKSIsInVybE5hbWUiOiAiZGFsbGFzdGVzdDEifSx7IkxvY0lkIjoiNTQyIiwiTG9jTmFtZSI6Inp6RXhjaGFuZ2UgVEVTVCBTSVRFIiwidXJsTmFtZSI6ImFhZmVzIn1dfV19"
}