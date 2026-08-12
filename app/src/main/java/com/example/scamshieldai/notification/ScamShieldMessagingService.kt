package com.example.scamshieldai.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.scamshieldai.MainActivity
import com.example.scamshieldai.R
import com.example.scamshieldai.auth.AuthTokenStore
import com.example.scamshieldai.network.ScamShieldRepository
import com.example.scamshieldai.settings.AppPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ScamShieldMessagingService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            if (AuthTokenStore.idToken == null) {
                val saved = AppPreferences.savedTokenFlow(applicationContext).first()
                val refresh = AppPreferences.savedRefreshTokenFlow(applicationContext).first()
                if (saved != null) {
                    AuthTokenStore.setToken(saved, refresh)
                }
            }
            if (AuthTokenStore.idToken != null) {
                ScamShieldRepository().registerFcmToken(token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        serviceScope.launch {
            val allEnabled = AppPreferences.notifAllFlow(applicationContext).first()
            if (!allEnabled) return@launch

            val type = message.data["type"].orEmpty()
            val securityEnabled = AppPreferences.notifSecurityFlow(applicationContext).first()
            val educationEnabled = AppPreferences.notifEducationFlow(applicationContext).first()
            val systemEnabled = AppPreferences.notifSystemFlow(applicationContext).first()

            val allowed = when {
                type == "new_report" || type == "report_status_updated" -> securityEnabled
                type.contains("education") -> educationEnabled
                type.contains("system") -> systemEnabled
                else -> true
            }
            if (!allowed) return@launch

            val title = message.notification?.title ?: message.data["title"] ?: "ScamShield AI"
            val body = message.notification?.body ?: message.data["body"] ?: ""
            showNotification(title, body, message.data)
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String> = emptyMap()) {
        val channelId = "scamshield_alerts"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Peringatan Ancaman", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logoapp_layar)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
