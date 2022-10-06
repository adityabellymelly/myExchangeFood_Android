package com.exchange.user.module.paymentModule
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.exchange.user.BR
import com.exchange.user.R
import com.exchange.user.databinding.ActivityWebPaymentBinding
import com.exchange.user.module.base_module.BaseActivity
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.base_module.base_view.ViewModelProviderFactory
import java.io.InputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class WebPayment : BaseActivity<ActivityWebPaymentBinding, WebPaymentViewModel>(),WebPaymentNavigator{
    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var webpaymentviewModel: WebPaymentViewModel
    private lateinit var activitywebBinding:ActivityWebPaymentBinding
    private var mActionMode  : ActionMode? =  null
    lateinit var from: String

    override fun getLayoutId(): Int {
        return R.layout.activity_web_payment
    }
    override fun getViewModel(): WebPaymentViewModel {
        webpaymentviewModel = ViewModelProvider(this, factory).get(WebPaymentViewModel::class.java)
        return webpaymentviewModel
    }
    override fun getBindingVariable(): Int {
        return BR.webPaymentViewModel
    }

    private  fun injectCSS(){
        try {
            val inputStream : InputStream = assets.open("style.css")
            val buffer =  byteArrayOf(inputStream.available().toByte())
            inputStream.read(buffer)
            inputStream.close()
            val encoded : String = Base64.encodeToString(buffer, Base64.NO_WRAP)
            activitywebBinding.webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(canvas{\n" +
                    "  -webkit-tap-highlight-color: transparent;\n" +
                    "})" +
                    "})()")

        }catch (e:Exception){}

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("MenuItem",item.toString())
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        return super.onCreateOptionsMenu(menu)
    }


    override fun onActionModeStarted(mode: ActionMode?) {
        if (mActionMode==null){
            mActionMode= mode
            val menu = mode?.menu
            menu?.clear()
            menu?.removeItem(android.R.id.copy)
            menu?.removeItem(android.R.id.paste)
            menu?.removeItem(android.R.id.selectAll)
        }
        super.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
        mActionMode = null
//        if (mActionMode!=null){
//            mActionMode?.finish()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitywebBinding = getViewDataBinding()
        initData()
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun initData() {
        activitywebBinding.root.setOnTouchListener { p0, p1 ->
            Log.e("event", p1?.action.toString())
            false
        }
        activitywebBinding.webView.isSelected =false // = Color.TRANSPARENT
        activitywebBinding.webView.isClickable = false
        activitywebBinding.webView.setOnClickListener {
            Toast.makeText(
                this@WebPayment,
                "Double Click",
                Toast.LENGTH_LONG
            ).show()
        }

//        activitywebBinding.webView.setOnClickListener(object : DoubleClickListener() {
//            override fun onSingleClick(v: View?) {
//
//            }
//
//            override fun onDoubleClick(v: View?) {
//                Toast.makeText(this@WebPayment, "Double Click",Toast.LENGTH_LONG).show()
//                //activitywebBinding.webView.clearFocus()
//            }
//
//        })
        intent.getStringExtra(ConstantCommand.DATA)?.let { it ->
            val link :String = it
            activitywebBinding.webView.settings.javaScriptEnabled = true
            activitywebBinding.webView.settings.domStorageEnabled = true
            activitywebBinding.webView.settings.databaseEnabled = true
           // activitywebBinding.webView.settings.pluginState = WebSettings.PluginState.ON
            activitywebBinding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            activitywebBinding.webView.isVerticalScrollBarEnabled = false
            activitywebBinding.webView.isHorizontalScrollBarEnabled = false
            activitywebBinding.webView.settings.useWideViewPort = true
            activitywebBinding.webView.settings.loadWithOverviewMode = true
            activitywebBinding.webView.isHapticFeedbackEnabled = false
            activitywebBinding.webView.setOnLongClickListener { false }
            activitywebBinding.webView.isLongClickable = false
            activitywebBinding.webView.addJavascriptInterface(MyJavaScriptInterface(this),"HtmlViewer")
            activitywebBinding.webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    injectCSS();
                    super.onPageFinished(view, url)
                }
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    request?.let {
                         if(it.url.path?.contains("CreditAuthorizer") == true){
                             Log.e("path Contain -","CreditAuthorizer") }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
                override fun onLoadResource(view: WebView?, url: String?) {
                    if(url?.contains("AAFESPaymentReturn.aspx")==true &&
                        url.contains("payResult=CANCEL")){
                        finish()
                    }
                    else if(url?.contains("AAFESPaymentReturn.aspx") == true) {
                        val returnIntent = Intent()
                        returnIntent.putExtra(ConstantCommand.DATA,ConstantCommand.OKRESTULT)
                        setResult(Activity.RESULT_OK,returnIntent)
                        finish()
                    }
                    super.onLoadResource(view, url)
                }
                @RequiresApi(Build.VERSION_CODES.N)
                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    return  super.shouldInterceptRequest(view, request)
                }
                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    Log.e("handler -",handler.toString())
                    Log.e("error-", error?.primaryError.toString())
                    super.onReceivedSslError(view, handler, error)
                }
            }
            activitywebBinding.webView.loadUrl(link)
        }
    }

}