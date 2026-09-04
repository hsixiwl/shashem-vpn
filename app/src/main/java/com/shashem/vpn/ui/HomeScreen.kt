package com.shashem.vpn.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDark = Color(0xFF030408)
private val CardDark = Color(0xFF0F172A)
private val BlueGlow = Color(0xFF3B82F6)
private val PurpleGlow = Color(0xFF9333EA)

@Composable
fun HomeScreen(
    isConnected: Boolean,
    durationText: String,
    serverName: String = "Frankfurt #04",
    ping: Int = 24,
    onToggleConnect: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowAlpha"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(BgDark)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF111827))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(BlueGlow))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("VLESS", color = Color(0xFFCBD5E1), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isConnected) BlueGlow.copy(alpha = 0.15f) else Color(0xFF1E293B))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isConnected) Icons.Filled.VerifiedUser else Icons.Filled.GppMaybe,
                        contentDescription = null,
                        tint = if (isConnected) BlueGlow else Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("SHASHEM VPN", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text("SECURE TUNNEL v2.8", fontSize = 10.sp, color = Color(0xFF94A3B8), letterSpacing = 2.sp)
        }

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(210.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        BlueGlow.copy(alpha = if (isConnected) glowAlpha else 0.08f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier.size(180.dp).clip(CircleShape).background(CardDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(CircleShape)
                                .background(if (isConnected) BlueGlow.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.2f))
                                .clickable(onClick = onToggleConnect),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.PowerSettingsNew,
                                    contentDescription = "Connect",
                                    tint = if (isConnected) BlueGlow else Color(0xFF94A3B8),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isConnected) "DISCONNECT" else "CONNECT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isConnected) BlueGlow else Color(0xFF64748B),
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = if (isConnected) "CONNECTED" else "DISCONNECTED",
                    color = if (isConnected) BlueGlow else Color(0xFF94A3B8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = if (isConnected) durationText else "00:00:00",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .background(CardDark.copy(alpha = 0.9f))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(serverName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Ping: ${ping}ms", color = Color(0xFF4ADE80), fontSize = 10.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlueGlow.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("CHANGE", color = BlueGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                SpeedCard(
                    modifier = Modifier.weight(1f),
                    label = "DOWNLOAD",
                    value = if (isConnected) "1.2 MB/s" else "0.0 MB/s",
                    color = BlueGlow,
                    isDownload = true
                )
                SpeedCard(
                    modifier = Modifier.weight(1f),
                    label = "UPLOAD",
                    value = if (isConnected) "0.3 MB/s" else "0.0 MB/s",
                    color = PurpleGlow,
                    isDownload = false
                )
            }
        }
    }
}

@Composable
private fun SpeedCard(modifier: Modifier = Modifier, label: String, value: String, color: Color, isDownload: Boolean) {
    Row(
        modifier = modifier.clip(RoundedCornerShape(16.dp)).background(Color.Black.copy(alpha = 0.2f)).padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, color = Color(0xFF64748B), fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Icon(
            imageVector = if (isDownload) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
    }
}
