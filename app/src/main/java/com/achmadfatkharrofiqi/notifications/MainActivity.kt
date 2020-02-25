package com.achmadfatkharrofiqi.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
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
    private val button_update: Button by lazy { btn_update }
    private val button_cancel: Button by lazy { btn_cancel }
    private lateinit var mNotifyManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_notify.setOnClickListener {
            sendNotification()
        }

        button_update.setOnClickListener {
            updateNotification()
        }

        button_cancel.setOnClickListener {
            cancelNotification()
        }
        createNotificationChannel()
        setNotificationButtonState(
            isNotifyEnabled = true,
            isUpdateEnabled = false,
            isCancelEnabled = false
        )
    }

    fun setNotificationButtonState(isNotifyEnabled: Boolean, isUpdateEnabled: Boolean, isCancelEnabled: Boolean){
        button_notify.isEnabled = isNotifyEnabled
        button_update.isEnabled = isUpdateEnabled
        button_cancel.isEnabled = isCancelEnabled
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources,R.drawable.mascot_1)
        val notifyBuilder = getNotificationBuilder()
        notifyBuilder.apply {
            setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notification updated!")
            )
        }
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(
            isNotifyEnabled = false,
            isUpdateEnabled = false,
            isCancelEnabled = true
        )
    }

    private fun cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(
            isNotifyEnabled = true,
            isUpdateEnabled = false,
            isCancelEnabled = false
        )
    }

    fun sendNotification(){
        val notifyBuilder = getNotificationBuilder()
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
        setNotificationButtonState(
            isNotifyEnabled = false,
            isUpdateEnabled = true,
            isCancelEnabled = true
        )
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
