package com.example.scamshieldai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.scamshieldai.ui.theme.ScamShieldTheme

class MainActivity : ComponentActivity() {
    private val pendingNotificationRoute = mutableStateOf<String?>(null)

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingNotificationRoute.value = parseNotificationRoute(intent)
        enableEdgeToEdge()
        requestNotificationsPermissionIfNeeded()
        setContent {
            val notifRoute = pendingNotificationRoute.value
            ScamShieldTheme {
                ScamShieldApp(
                    pendingNotificationRoute = notifRoute,
                    onNotificationRouteConsumed = { pendingNotificationRoute.value = null }
                )
            }
        }
    }

    private fun requestNotificationsPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingNotificationRoute.value = parseNotificationRoute(intent)
    }

    private fun parseNotificationRoute(intent: Intent?): String? {
        val type = intent?.getStringExtra("type") ?: return null
        return when (type) {
            "report_status_updated" -> {
                val reportId = intent.getStringExtra("report_id") ?: return null
                "report_status/$reportId"
            }
            "new_report" -> "admin_reports"
            else -> null
        }
    }
}
