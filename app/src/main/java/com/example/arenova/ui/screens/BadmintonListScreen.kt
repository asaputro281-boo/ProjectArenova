package com.example.arenova.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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

// Palet warna hijau disamakan persis dengan seluruh tampilan sebelum-sebelumnya
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

data class BadmintonCourtItem(
    val id: String,
    val name: String,
    val imageRating: String,
    val starRating: String,
    val location: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun BadmintonListScreen(
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onBookCourtClick: (BadmintonCourtItem) -> Unit = {},
    onTabSelected: (DashboardTab) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilterChip by remember { mutableStateOf("Hari Ini") }
    var selectedTab by remember { mutableStateOf(DashboardTab.BERANDA) }

    val courts = remember {
        listOf(
            BadmintonCourtItem(
                id = "1",
                name = "Lapangan 1",
                imageRating = "4.9",
                starRating = "4.9",
                location = "Kebayoran Baru, Jakarta Selatan",
                price = "Rp 65.000",
                imageRes = R.drawable.img_badminton
            ),
            BadmintonCourtItem(
                id = "2",
                name = "Lapangan 2",
                imageRating = "4.8",
                starRating = "4.8",
                location = "Kebayoran Baru, Jakarta Selatan",
                price = "Rp 50.000",
                imageRes = R.drawable.img_futsal
            ),
            BadmintonCourtItem(
                id = "3",
                name = "Lapangan 3",
                imageRating = "4.7",
                starRating = "4.7",
                location = "Kebayoran Baru, Jakarta Selatan",
                price = "Rp 55.000",
                imageRes = R.drawable.img_badminton
            ),
            BadmintonCourtItem(
                id = "4",
                name = "Lapangan 4",
                imageRating = "4.9",
                starRating = "4.6",
                location = "Kebayoran Baru, Jakarta Selatan",
                price = "Rp 70.000",
                imageRes = R.drawable.img_padel
            )
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding(),
        containerColor = ArenovaScreenBg,
        bottomBar = {
            BadmintonBottomNavigation(
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
                .padding(bottom = innerPadding.calculateBottomPadding())
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
                // Kiri: Logo Kotak Hijau + Teks "Beranda"
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

                // Kanan: Ikon Lonceng Notifikasi & Ikon Pengguna
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
            // 2. SUB-HEADER: TOMBOL KEMBALI, JUDUL & FILTER/SORT
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Back
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
                        text = "Badminton",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "4 venue tersedia di sekitarmu",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray
                    )
                }

                // Tombol Filter & Sort
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tombol Filter (Tune)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onFilterClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tune),
                            contentDescription = "Filter",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Tombol Sort (Up & Down Arrows)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSortClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            contentDescription = "Urutkan",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 3. FILTER CHIPS (Hari Ini, Karpet Li-Ning, Indoor AC)
            // ==========================================
            val filterChips = listOf(
                FilterChipData("Hari Ini", R.drawable.ic_calendar),
                FilterChipData("Karpet Li-Ning", R.drawable.ic_verified_badge),
                FilterChipData("Indoor AC", R.drawable.ic_ac)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                filterChips.forEach { chip ->
                    val isSelected = selectedFilterChip == chip.label
                    Surface(
                        onClick = { selectedFilterChip = chip.label },
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent,
                        modifier = Modifier
                            .shadow(
                                elevation = if (isSelected) 3.dp else 0.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = RevoraGreenPrimary.copy(alpha = 0.25f),
                                spotColor = RevoraGreenPrimary.copy(alpha = 0.35f)
                            )
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .then(
                                    if (isSelected) {
                                        Modifier.background(brush = RevoraGreenGradient)
                                    } else {
                                        Modifier
                                            .background(Color.White)
                                            .border(
                                                width = 1.dp,
                                                color = Color(0xFFE2E8F0),
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                    }
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = chip.iconRes),
                                    contentDescription = chip.label,
                                    tint = if (isSelected) Color.White else ArenovaSubtitleGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = chip.label,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else ArenovaDarkText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 4. DAFTAR KARTU LAPANGAN BADMINTON
            // ==========================================
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(courts) { court ->
                    BadmintonCourtCard(
                        court = court,
                        onBookClick = { onBookCourtClick(court) }
                    )
                }
            }
        }
    }
}

private data class FilterChipData(
    val label: String,
    val iconRes: Int
)

/**
 * Kartu Lapangan Badminton individual
 */
@Composable
private fun BadmintonCourtCard(
    court: BadmintonCourtItem,
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
            // Gambar Lapangan + Badge Rating di Atas Kiri Gambar
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

                // Badge Rating di Pojok Kiri Atas Gambar
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

            // Kolom Informasi Lapangan & Tombol Pesan
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Baris Judul & Rating Star Kanan
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

                // Lokasi Lapangan
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

                // Harga & Tombol "Pesan"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Harga per jam (Warna oranye khas tiket/booking)
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

                    // Tombol "Pesan" (Diselaraskan dengan gaya tombol tampilan sebelumnya)
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

/**
 * Bottom Navigation Bar untuk Badminton Screen
 */
@Composable
private fun BadmintonBottomNavigation(
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
                BadmintonBottomNavItem(
                    label = "Beranda",
                    iconRes = R.drawable.ic_home,
                    isSelected = selectedTab == DashboardTab.BERANDA,
                    onClick = { onTabSelected(DashboardTab.BERANDA) },
                    modifier = Modifier.weight(1f)
                )

                BadmintonBottomNavItem(
                    label = "Riwayat",
                    iconRes = R.drawable.ic_receipt,
                    isSelected = selectedTab == DashboardTab.RIWAYAT,
                    onClick = { onTabSelected(DashboardTab.RIWAYAT) },
                    modifier = Modifier.weight(1f)
                )

                BadmintonBottomNavItem(
                    label = "Profil",
                    iconRes = R.drawable.ic_profile_tab,
                    isSelected = selectedTab == DashboardTab.PROFIL,
                    onClick = { onTabSelected(DashboardTab.PROFIL) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BadmintonBottomNavItem(
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
fun BadmintonListScreenPreview() {
    ArenovaTheme {
        BadmintonListScreen()
    }
}
