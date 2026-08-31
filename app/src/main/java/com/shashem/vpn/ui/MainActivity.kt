package com.shashem.vpn.ui

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shashem.vpn.service.ShashemVpnService

class MainActivity : ComponentActivity() {

    private var isConnected by mutableStateOf(false)

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startVpn()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("SHASHEM VPN", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(if (isConnected) "Connected" else "Disconnected")
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { onConnectClick() }) {
                            Text(if (isConnected) "Disconnect" else "Connect")
                        }
                    }
                }
            }
        }
    }

    private fun onConnectClick() {
        if (isConnected) {
            stopVpn()
        } else {
            val intent = VpnService.prepare(this)
            if (intent != null) vpnPermissionLauncher.launch(intent) else startVpn()
        }
    }

    private fun startVpn() {
        val serviceIntent = Intent(this, ShashemVpnService::class.java).apply {
            action = ShashemVpnService.ACTION_CONNECT
        }
        startService(serviceIntent)
        isConnected = true
    }

    private fun stopVpn() {
        val serviceIntent = Intent(this, ShashemVpnService::class.java).apply {
            action = ShashemVpnService.ACTION_DISCONNECT
        }
        startService(serviceIntent)
        isConnected = false
    }
}
