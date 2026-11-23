package com.example.arenova.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

/**
 * Tab navigasi pada Bottom Bar (hanya BERANDA yang memiliki tampilan/fungsi aktif)
 */
enum class DashboardTab {
    BERANDA,
    RIWAYAT,
    PROFIL
}

@Composable
fun DashboardScreen(
    userName: String = "Pengguna",
    onCategoryClick: (categoryName: String) -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onLogoutClick: () -> Unit = onProfileClick,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.BERANDA) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding(),
        containerColor = ArenovaScreenBg,
        bottomBar = {
            ArenovaBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    // Fungsi Riwayat dan Profil dikosongkan; tombol tetap ada di UI
                    if (tab == DashboardTab.BERANDA) {
                        selectedTab = DashboardTab.BERANDA
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR: LOGO BERANDA & AKSI KANAN
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                brush = RevoraGreenGradient,
                                shape = RoundedCornerShape(11.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Logo Beranda",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Beranda",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNotificationClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "Notifikasi",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                brush = RevoraGreenGradient,
                                shape = CircleShape
                            )
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onLogoutClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "Keluar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 2. GREETING HEADER
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selamat Datang",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = ArenovaSubtitleGray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Halo, $userName",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ==========================================
            // 3. SECTION KATEGORI (4 Olahraga Populer)
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kategori",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                Text(
                    text = "4 Olahraga Populer",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RevoraGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- GRID 2X2 KATEGORI OLAHRAGA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SportCategoryCard(
                    title = "Badminton",
                    tagText = "Indoor",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Badminton") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_badminton),
                        contentDescription = "Lapangan Badminton Indoor",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                SportCategoryCard(
                    title = "Futsal",
                    tagText = "Indoor",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Futsal") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_futsal),
                        contentDescription = "Lapangan Futsal Indoor",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SportCategoryCard(
                    title = "Mini Soccer",
                    tagText = "Outdoor",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Mini Soccer") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mini_soccer),
                        contentDescription = "Lapangan Mini Soccer Outdoor",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                SportCategoryCard(
                    title = "Padel",
                    tagText = "Indoor",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Padel") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_padel),
                        contentDescription = "Lapangan Padel Glass Court",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // 4. BANNER INFO JADWAL AKHIR PEKAN
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFEFF5FE)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = Color(0xFFD1FBE5),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Kalender",
                            tint = ArenovaPrimaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Jadwal akhir pekan segera penuh!",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pesan lebih awal untuk slot malam favoritmu.",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SportCategoryCard(
    title: String,
    tagText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visualContent: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.15f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                visualContent()

                Box(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tagText,
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = ArenovaSubtitleGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ArenovaBottomNavigation(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    label = "Beranda",
                    iconRes = R.drawable.ic_home,
                    isSelected = selectedTab == DashboardTab.BERANDA,
                    onClick = { onTabSelected(DashboardTab.BERANDA) },
                    modifier = Modifier.weight(1f)
                )

                BottomNavItem(
                    label = "Riwayat",
                    iconRes = R.drawable.ic_receipt,
                    isSelected = false,
                    onClick = { onTabSelected(DashboardTab.RIWAYAT) },
                    modifier = Modifier.weight(1f)
                )

                BottomNavItem(
                    label = "Profil",
                    iconRes = R.drawable.ic_profile_tab,
                    isSelected = false,
                    onClick = { onTabSelected(DashboardTab.PROFIL) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = RevoraGreenPrimary
    val inactiveColor = Color(0xFF475569)
    val itemColor = if (isSelected) activeColor else inactiveColor

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = itemColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = itemColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    ArenovaTheme {
        DashboardScreen(userName = "Angga Dwi Saputro")
    }
}