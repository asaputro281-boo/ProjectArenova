package com.example.arenova.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

data class FutsalCourtItem(
    val id: String,
    val name: String,
    val imageRating: String,
    val starRating: String,
    val location: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun FutsalListScreen(
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookCourtClick: (FutsalCourtItem) -> Unit = {},
    onTabSelected: (DashboardTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.BERANDA) }

    val courts = remember {
        listOf(
            FutsalCourtItem(
                id = "1",
                name = "Lapangan 1",
                imageRating = "4.9",
                starRating = "4.9",
                location = "Kebayoran Baru, Jakarta Selatan",
                price = "Rp 80.000",
                imageRes = R.drawable.img_futsal
            ),
            FutsalCourtItem(
                id = "2",
                name = "Lapangan 2",
                imageRating = "4.8",
                starRating = "4.8",
                location = "Kebayoran Lama, Jakarta Selatan",
                price = "Rp 75.000",
                imageRes = R.drawable.img_futsal1
            ),
            FutsalCourtItem(
                id = "3",
                name = "Lapangan 3",
                imageRating = "4.7",
                starRating = "4.7",
                location = "Setiabudi, Jakarta Selatan",
                price = "Rp 70.000",
                imageRes = R.drawable.img_futsal2
            ),
            FutsalCourtItem(
                id = "4",
                name = "Lapangan 4",
                imageRating = "4.6",
                starRating = "4.6",
                location = "Mampang, Jakarta Selatan",
                price = "Rp 65.000",
                imageRes = R.drawable.img_futsal3
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ArenovaScreenBg,
        bottomBar = {
            FutsalBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    onTabSelected(tab)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR: LOGO BERANDA & AKSI KANAN
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
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
                            ) { onProfileClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "Profil",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 2. SUB-HEADER: TOMBOL KEMBALI & JUDUL
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali ke Dashboard",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Futsal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "${courts.size} Lapangan tersedia di sekitarmu",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 3. DAFTAR KARTU LAPANGAN FUTSAL
            // ==========================================
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(courts, key = { it.id }) { court ->
                    FutsalCourtCard(
                        court = court,
                        onBookClick = { onBookCourtClick(court) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FutsalCourtCard(
    court: FutsalCourtItem,
    onBookClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .clip(RoundedCornerShape(14.dp))
            ) {
                Image(
                    painter = painterResource(id = court.imageRes),
                    contentDescription = court.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .padding(start = 6.dp, top = 6.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.92f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "Rating",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = court.imageRating,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = court.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = "Star",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = court.starRating,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_location_pin),
                        contentDescription = "Lokasi",
                        tint = ArenovaSubtitleGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.location,
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = court.price,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA580C)
                        )
                        Text(
                            text = " /jam",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray,
                            modifier = Modifier.padding(bottom = 1.dp)
                        )
                    }

                    Button(
                        onClick = onBookClick,
                        modifier = Modifier
                            .height(34.dp)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(10.dp),
                                ambientColor = RevoraGreenPrimary.copy(alpha = 0.3f),
                                spotColor = RevoraGreenPrimary.copy(alpha = 0.4f)
                            ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(brush = RevoraGreenGradient)
                                .padding(horizontal = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pesan",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FutsalBottomNavigation(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
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
                FutsalBottomNavItem(
                    label = "Beranda",
                    iconRes = R.drawable.ic_home,
                    isSelected = selectedTab == DashboardTab.BERANDA,
                    onClick = { onTabSelected(DashboardTab.BERANDA) },
                    modifier = Modifier.weight(1f)
                )

                FutsalBottomNavItem(
                    label = "Riwayat",
                    iconRes = R.drawable.ic_receipt,
                    isSelected = false,
                    onClick = { onTabSelected(DashboardTab.RIWAYAT) },
                    modifier = Modifier.weight(1f)
                )

                FutsalBottomNavItem(
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
private fun FutsalBottomNavItem(
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
fun FutsalListScreenPreview() {
    ArenovaTheme {
        FutsalListScreen()
    }
}