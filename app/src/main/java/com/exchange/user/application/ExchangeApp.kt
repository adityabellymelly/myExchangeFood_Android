package com.exchange.user.application

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import com.exchange.user.R
import com.exchange.user.di_module.component.DaggerApplicationComponent
import com.exchange.user.module.base_module.ConstantCommand
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject
class ExchangeApp: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchandroidinjection: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchandroidinjection
    }

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .inject(this)
        AndroidThreeTen.init(this)
        RxJavaPlugins.setErrorHandler { _ -> }
        initNotificationChannel()

    }

    private fun initNotificationChannel() {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(packageName,ConstantCommand.APP_NAME, importance)
            mChannel.importance = importance
            mChannel.setBypassDnd(true)
            mChannel.setShowBadge(true)
            val att = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
            mChannel.setSound(defaultSoundUri, att)
            mChannel.enableLights(true)
            mChannel.enableVibration(false)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            //create channel
            notificationManager.createNotificationChannel(mChannel)
            val customSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.notification)
            val mRestaurantChannel = NotificationChannel(packageName.plus(".restaurant"), ConstantCommand.APP_NAME, importance)
            mRestaurantChannel.importance = importance
            mRestaurantChannel.setBypassDnd(true)
            mRestaurantChannel.setShowBadge(true)

            val resAtt = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()

            mRestaurantChannel.setSound(customSoundUri, resAtt)
            mRestaurantChannel.enableLights(true)
            mRestaurantChannel.enableVibration(false)
            mRestaurantChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            //create channel
            notificationManager.createNotificationChannel(mRestaurantChannel)
        }
    }
}