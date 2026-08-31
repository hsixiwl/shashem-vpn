package com.shashem.vpn.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.shashem.vpn.R
import com.shashem.vpn.ui.MainActivity

class ShashemVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var isRunning = false

    companion object {
        const val ACTION_CONNECT = "com.shashem.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.shashem.vpn.DISCONNECT"
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "shashem_vpn_channel"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_CONNECT -> startVpnTunnel()
            ACTION_DISCONNECT -> stopVpnTunnel()
        }
        return START_NOT_STICKY
    }

    private fun startVpnTunnel() {
        if (isRunning) return
        createNotificationChannel()

        val notification = buildNotification("SHASHEM VPN", "Connection active")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        try {
            val builder = Builder()
                .setSession("SHASHEM VPN")
                .addAddress("10.0.0.2", 24)
                .addDnsServer("1.1.1.1")
                .addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0)
                .setMtu(1500)

            vpnInterface = builder.establish()
            isRunning = true
            // NOTE: real tunneling to a remote server (V2Ray/WireGuard core) is the next step.
        } catch (e: Exception) {
            e.printStackTrace()
            stopVpnTunnel()
        }
    }

    private fun stopVpnTunnel() {
        isRunning = false
        try {
            vpnInterface?.close()
            vpnInterface = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "SHASHEM VPN Status", NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Shows VPN connection status" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(title: String, text: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        stopVpnTunnel()
        super.onDestroy()
    }
}
