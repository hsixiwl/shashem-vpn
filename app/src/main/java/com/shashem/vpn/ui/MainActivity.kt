package com.shashem.vpn.ui

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.shashem.vpn.service.ShashemVpnService

class MainActivity : ComponentActivity() {

    private var isConnected by mutableStateOf(false)

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) startVpn()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen(
                        isConnected = isConnected,
                        durationText = "00:00:00",
                        onToggleConnect = { onConnectClick() }
                    )
                }
            }
        }
    }

    private fun onConnectClick() {
        if (isConnected) stopVpn() else {
            val intent = VpnService.prepare(this)
            if (intent != null) vpnPermissionLauncher.launch(intent) else startVpn()
        }
    }

    private fun startVpn() {
        startService(Intent(this, ShashemVpnService::class.java).apply {
            action = ShashemVpnService.ACTION_CONNECT
        })
        isConnected = true
    }

    private fun stopVpn() {
        startService(Intent(this, ShashemVpnService::class.java).apply {
            action = ShashemVpnService.ACTION_DISCONNECT
        })
        isConnected = false
    }
}
