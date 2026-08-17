package com.example.scamshieldai.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scamshieldai.R
import com.example.scamshieldai.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    threatCount: Int,
    onScanModeSelected: (String) -> Unit,
    onEducationSelected: () -> Unit,
    onHistoryClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bagrounapp),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        
        Scaffold(
            topBar = { HomeTopBar(threatCount, onNotificationClick) },
            containerColor = Color.Transparent,
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    GreetingSection(userName)
                }

                item {
                    EducationBannerCard(onEducationSelected)
                }

                item {
                    OngoingProjectsSection(onHistoryClick)
                }
                
                item {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ScanModeCard(
                                title = "Scan Chat",
                                desc = "Analisis teks pesan",
                                icon = Icons.Outlined.ChatBubbleOutline,
                                isSelected = selectedMode == "chat",
                                modifier = Modifier.weight(1f),
                                onClick = { 
                                    selectedMode = "chat"
                                    onScanModeSelected("chat") 
                                }
                            )
                            ScanModeCard(
                                title = "Screenshot",
                                desc = "Ekstrak teks gambar",
                                icon = Icons.Outlined.Image,
                                isSelected = selectedMode == "screenshot",
                                modifier = Modifier.weight(1f),
                                onClick = { 
                                    selectedMode = "screenshot"
                                    onScanModeSelected("screenshot") 
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ScanModeCard(
                                title = "Cek Tautan",
                                desc = "Verifikasi URL aman",
                                icon = Icons.Outlined.Link,
                                isSelected = selectedMode == "link",
                                modifier = Modifier.weight(1f),
                                onClick = { 
                                    selectedMode = "link"
                                    onScanModeSelected("link") 
                                }
                            )
                            ScanModeCard(
                                title = "Scan QR",
                                desc = "Pindai kode QRIS",
                                icon = Icons.Outlined.QrCodeScanner,
                                isSelected = selectedMode == "qr",
                                modifier = Modifier.weight(1f),
                                onClick = { 
                                    selectedMode = "qr"
                                    onScanModeSelected("qr") 
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanModeCard(
    title: String,
    desc: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) DeepNavy else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black
    val iconColor = if (isSelected) Color.White else DeepNavy
    val descColor = if (isSelected) Color.White.copy(alpha = 0.7f) else Slate500
    val borderColor = if (isSelected) DeepNavy else Slate100

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = desc,
                fontSize = 13.sp,
                color = descColor,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun HomeTopBar(threatCount: Int, onNotificationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.GridView,
            contentDescription = "Menu",
            tint = PrussianBlue,
            modifier = Modifier.size(28.dp)
        )
        
        Text(
            text = "Home",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrussianBlue
        )

        BadgedBox(
            badge = {
                if (threatCount > 0) {
                    Badge(containerColor = DangerRed) {
                        Text(text = threatCount.toString(), color = Color.White)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifikasi",
                tint = PrussianBlue,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onNotificationClick() }
            )
        }
    }
}

@Composable
private fun GreetingSection(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp_removebg),
            contentDescription = "Logo Aplikasi",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = "Hi ${userName.ifEmpty { "Pengguna" }}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrussianBlue
            )
            Text(
                text = "Mau cek apa?",
                fontSize = 16.sp,
                color = Slate500
            )
        }
    }
}

@Composable
private fun EducationBannerCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(24.dp))
            .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pusat Edukasi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrussianBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pelajari modus penipuan terbaru dan cara menghindarinya.",
                        fontSize = 14.sp,
                        color = Slate500,
                        lineHeight = 20.sp
                    )
                }
                
                Image(
                    painter = painterResource(id = R.drawable.edukasicard_beranda),
                    contentDescription = "Edukasi",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun OngoingProjectsSection(onViewAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Analisis Terbaru",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrussianBlue
        )
        
        Text(
            text = "lihat semua",
            fontSize = 14.sp,
            color = Slate400,
            modifier = Modifier.clickable { onViewAll() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomeScreen(
        userName = "Jenifer",
        threatCount = 2,
        onScanModeSelected = {},
        onEducationSelected = {},
        onHistoryClick = {},
        onNotificationClick = {}
    )
}
