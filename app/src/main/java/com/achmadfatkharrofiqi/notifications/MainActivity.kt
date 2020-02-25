package com.achmadfatkharrofiqi.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        val NOTIFICATION_ID = 0
    }

    private val button_notify: Button by lazy { btn_notify }
    private lateinit var mNotifyManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_notify.setOnClickListener {
            sendNotification()
        }
        createNotificationChannel()
    }

    fun sendNotification(){
        val notifyBuilder = getNotificationBuilder()
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    fun createNotificationChannel(){
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notification from Mascot"
            }
            mNotifyManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
        notifyBuilder.apply {
            setContentTitle("You've been notified!")
            setContentText("This is your notification text.")
            setSmallIcon(R.drawable.ic_android)
            setContentIntent(notificationPendingIntent)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_ALL)
        }
        return notifyBuilder
    }

}
