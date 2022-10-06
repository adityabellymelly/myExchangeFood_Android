package com.exchange.user.module.base_module.base_service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.exchange.user.R
import com.exchange.user.module.base_module.ConstantCommand
import com.exchange.user.module.order_history.OrderHistoryActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyfirebaseMessagingService : FirebaseMessagingService() {
    private var count = 7693
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        createNotification("Exchange Notification", data["message"] ?: "", data)
         setNotificationBroadcast()
    }

    private fun setNotificationBroadcast() {
        val intent = Intent(ConstantCommand.INCOMING_NOTIFICATION)
        sendBroadcast(intent)
    }


    private fun createNotification(title: String, body: String, data: MutableMap<String, String>) {
        val intent = Intent(applicationContext, OrderHistoryActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(this, packageName)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setSmallIcon(getNotificationIcon())
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setSound(defaultSoundUri)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body)).setContentText(body)
            .setContentIntent(contentIntent)
        notificationManager.notify(count, notificationBuilder.build())
        count++
    }


    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.drawable.logo else R.drawable.logo
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun isAppForground(context: Context) : Boolean{
//        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val tasks = am.appTasks
//        if(tasks.isNotEmpty()){
//
//            for( i in 0 until tasks.size){
//                if (tasks[i].taskInfo.topActivity?.packageName.equals(context.packageName)){
//                    return false
//                }
//            }
//        }
//        return true
//    }

}