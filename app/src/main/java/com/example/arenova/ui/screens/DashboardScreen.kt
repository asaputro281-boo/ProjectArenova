package com.example.arenova.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaBorderGray
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaPrimaryGreenLight
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

/**
 * Tab navigasi utama pada Dashboard Arenova
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

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding(),
        containerColor = ArenovaScreenBg,
        bottomBar = {
            ArenovaBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (selectedTab) {
                DashboardTab.BERANDA -> {
                    BerandaContent(
                        userName = userName,
                        onCategoryClick = onCategoryClick,
                        onCommunityClick = onCommunityClick,
                        onProfileTabClick = { selectedTab = DashboardTab.PROFIL },
                        onNotificationClick = onNotificationClick
                    )
                }

                DashboardTab.RIWAYAT -> {
                    RiwayatContent(
                        onNotificationClick = onNotificationClick,
                        onBookAgainClick = onCategoryClick
                    )
                }

                DashboardTab.PROFIL -> {
                    ProfilContent(
                        userName = userName,
                        onNotificationClick = onNotificationClick,
                        onLogoutClick = onLogoutClick
                    )
                }
            }
        }
    }
}

// =========================================================================
// 1. KONTEN BERANDA (HOME SCREEN)
// =========================================================================

@Composable
private fun BerandaContent(
    userName: String,
    onCategoryClick: (categoryName: String) -> Unit,
    onCommunityClick: () -> Unit,
    onProfileTabClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // ==========================================
        // TOP APP BAR: LOGO BERANDA & AKSI KANAN
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kiri: Logo Kotak Hijau + Ikon Rumah Putih + Tulisan "Beranda"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = ArenovaPrimaryGreen,
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

            // Kanan: Ikon Notifikasi (Lonceng) & Ikon Pengguna (Lingkaran Hijau)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Ikon Lonceng Notifikasi
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

                // Tombol Profil Bulat Hijau
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = ArenovaPrimaryGreen,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onProfileTabClick() },
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
        // GREETING & PROFILE AVATAR
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

        Spacer(modifier = Modifier.height(22.dp))

        // ==========================================
        // SEARCH BAR & FILTER
        // ==========================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Cari",
                    tint = ArenovaPlaceholderGray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Cari lapangan...",
                            color = ArenovaPlaceholderGray,
                            fontSize = 14.sp
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = ArenovaDarkText
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Tombol Filter di kanan search bar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFEFF3F8),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { /* Filter Action */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tune),
                        contentDescription = "Filter",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ==========================================
        // CARD MAIN BARENG KOMUNITAS
        // ==========================================
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCommunityClick() },
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon Komunitas (Squircle Hijau dengan Figure Biru)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = ArenovaPrimaryGreen,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_community),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Main Bareng Komunitas",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "14 sesi terbuka di sekitarmu",
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Tombol "Gabung"
                Button(
                    onClick = onCommunityClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArenovaPrimaryGreen,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "Gabung",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // SECTION KATEGORI (4 Olahraga Populer)
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
                color = ArenovaPrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- GRID 2X2 KATEGORI OLAHRAGA ---
        // Baris 1: Badminton & Futsal
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
                tagText = "Vinyl",
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

        // Baris 2: Mini Soccer & Padel
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
                tagText = "Glass Court",
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
        // BANNER INFO JADWAL AKHIR PEKAN
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
                // Ikon Kalender Hijau dalam Lingkaran Mint
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

// =========================================================================
// 2. KONTEN RIWAYAT (HISTORY SCREEN)
// =========================================================================

private data class BookingHistoryItem(
    val id: String,
    val sportName: String,
    val venueName: String,
    val dateTime: String,
    val price: String,
    val status: String, // "Berlangsung", "Selesai"
    val imageRes: Int
)

@Composable
private fun RiwayatContent(
    onNotificationClick: () -> Unit,
    onBookAgainClick: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filterOptions = listOf("Semua", "Berlangsung", "Selesai")

    val sampleBookings = remember {
        listOf(
            BookingHistoryItem(
                id = "#ARE-98213",
                sportName = "Badminton",
                venueName = "Arena Badminton Hall A (Court 2)",
                dateTime = "Sabtu, 22 Nov 2026 • 19:00 - 21:00 WIB",
                price = "Rp 120.000",
                status = "Berlangsung",
                imageRes = R.drawable.img_badminton
            ),
            BookingHistoryItem(
                id = "#ARE-84721",
                sportName = "Futsal",
                venueName = "Galaxy Futsal Stadium (Vinyl 1)",
                dateTime = "Kamis, 20 Nov 2026 • 20:00 - 22:00 WIB",
                price = "Rp 250.000",
                status = "Selesai",
                imageRes = R.drawable.img_futsal
            ),
            BookingHistoryItem(
                id = "#ARE-77340",
                sportName = "Mini Soccer",
                venueName = "Golden Turf Champions (Outdoor A)",
                dateTime = "Minggu, 15 Nov 2026 • 16:00 - 18:00 WIB",
                price = "Rp 400.000",
                status = "Selesai",
                imageRes = R.drawable.img_mini_soccer
            ),
            BookingHistoryItem(
                id = "#ARE-65129",
                sportName = "Padel",
                venueName = "Metro Padel Club (Glass Court 1)",
                dateTime = "Selasa, 10 Nov 2026 • 17:00 - 18:30 WIB",
                price = "Rp 180.000",
                status = "Selesai",
                imageRes = R.drawable.img_padel
            )
        )
    }

    val filteredList = remember(selectedFilter) {
        if (selectedFilter == "Semua") sampleBookings
        else sampleBookings.filter { it.status == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top App Bar Riwayat
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
                            color = ArenovaPrimaryGreen,
                            shape = RoundedCornerShape(11.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_receipt),
                        contentDescription = "Logo Riwayat",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Riwayat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )
            }

            // Ikon Lonceng Notifikasi
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
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subtitle deskripsi
        Text(
            text = "Daftar pemesanan & sewa lapangan olahraga Anda",
            fontSize = 13.sp,
            color = ArenovaSubtitleGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter chips baris horizontal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) ArenovaPrimaryGreen else Color.White
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ArenovaPrimaryGreen else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else ArenovaDarkText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List kartu booking
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredList) { booking ->
                BookingCard(
                    item = booking,
                    onActionClick = { onBookAgainClick(booking.sportName) }
                )
            }
        }
    }
}

@Composable
private fun BookingCard(
    item: BookingHistoryItem,
    onActionClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Thumbnail + Detail Ringkas + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.sportName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.sportName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )

                        // Status Badge
                        val isOngoing = item.status == "Berlangsung"
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isOngoing) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = item.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isOngoing) Color(0xFF15803D) else Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.venueName,
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.dateTime,
                        fontSize = 11.5.sp,
                        color = ArenovaDarkText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Kode Booking + Harga + Tombol Aksi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.id,
                        fontSize = 11.sp,
                        color = ArenovaPlaceholderGray
                    )
                    Text(
                        text = item.price,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaPrimaryGreen
                    )
                }

                if (item.status == "Berlangsung") {
                    Button(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ArenovaPrimaryGreen,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Lihat Tiket",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ArenovaPrimaryGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ArenovaPrimaryGreen
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Pesan Lagi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. KONTEN PROFIL (PROFILE SCREEN)
// =========================================================================

@Composable
private fun ProfilContent(
    userName: String,
    onNotificationClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Top App Bar Profil
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
                            color = ArenovaPrimaryGreen,
                            shape = RoundedCornerShape(11.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile_tab),
                        contentDescription = "Logo Profil",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Profil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )
            }

            // Ikon Lonceng Notifikasi
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
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // KARTU DATA PENGGUNA (AVATAR & INFO)
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Besar
                Box(
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFED7AA),
                                        Color(0xFFFDBA74),
                                        Color(0xFF854D0E)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    // Dot Hijau Online Aktif
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.BottomEnd)
                            .background(Color(0xFF10B981), CircleShape)
                            .border(2.5.dp, Color.White, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                Spacer(modifier = Modifier.height(3.dp))

                val userHandle = userName.lowercase().replace(" ", "")
                Text(
                    text = "$userHandle@arenova.id",
                    fontSize = 13.sp,
                    color = ArenovaSubtitleGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Badge Member Arenova
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Member Aktif Arenova",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArenovaPrimaryGreen
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Statistik 3 Kolom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatItem(count = "12", label = "Sesi Main")
                    ProfileStatItem(count = "4", label = "Favorit")
                    ProfileStatItem(count = "250", label = "Poin Arena")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // MENU PENGATURAN & DUKUNGAN
        // ==========================================
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                ProfileMenuItem(
                    title = "Edit Profil & Akun",
                    subtitle = "Nama, kontak, dan foto profil"
                )
                HorizontalDivider(color = Color(0xFFF8FAFC), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(
                    title = "Lapangan Favorit",
                    subtitle = "Daftar lapangan yang sering dipesan"
                )
                HorizontalDivider(color = Color(0xFFF8FAFC), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(
                    title = "Pusat Bantuan & CS",
                    subtitle = "Layanan pelanggan dan panduan Arenova"
                )
                HorizontalDivider(color = Color(0xFFF8FAFC), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileMenuItem(
                    title = "Syarat & Ketentuan",
                    subtitle = "Kebijakan privasi dan sewa lapangan"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==========================================
        // TOMBOL LOGOUT (KELUAR DARI AKUN)
        // ==========================================
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFEBEB),
                contentColor = ArenovaErrorRed
            )
        ) {
            Text(
                text = "Keluar dari Akun",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaErrorRed
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun ProfileStatItem(
    count: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ArenovaDarkText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.5.sp,
            color = ArenovaSubtitleGray
        )
    }
}

@Composable
private fun ProfileMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArenovaDarkText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.5.sp,
                color = ArenovaSubtitleGray
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = ArenovaSubtitleGray,
            modifier = Modifier.size(16.dp)
        )
    }
}

// =========================================================================
// 4. BOTTOM NAVIGATION BAR (BERANDA, RIWAYAT, PROFIL)
// =========================================================================

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
                    // Garis pembatas tipis di bagian atas Bottom Bar
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
                // Tab 1: Beranda
                BottomNavItem(
                    label = "Beranda",
                    iconRes = R.drawable.ic_home,
                    isSelected = selectedTab == DashboardTab.BERANDA,
                    onClick = { onTabSelected(DashboardTab.BERANDA) },
                    modifier = Modifier.weight(1f)
                )

                // Tab 2: Riwayat
                BottomNavItem(
                    label = "Riwayat",
                    iconRes = R.drawable.ic_receipt,
                    isSelected = selectedTab == DashboardTab.RIWAYAT,
                    onClick = { onTabSelected(DashboardTab.RIWAYAT) },
                    modifier = Modifier.weight(1f)
                )

                // Tab 3: Profil
                BottomNavItem(
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
private fun BottomNavItem(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = ArenovaPrimaryGreen
    val inactiveColor = Color(0xFF475569) // Warna abu-abu slate seperti pada gambar desain

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

// =========================================================================
// 5. KOMPONEN PENDUKUNG (AVATAR & SPORT CATEGORY CARD)
// =========================================================================

/**
 * Avatar pengguna dengan indikator status online bulat hijau
 */
@Composable
private fun UserAvatarWithStatus(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
    ) {
        // Foto Profil Ilustratif Pengguna
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFED7AA),
                            Color(0xFFFDBA74),
                            Color(0xFF854D0E)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "P",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )
        }

        // Dot hijau aktif di pojok kanan bawah
        Box(
            modifier = Modifier
                .size(13.dp)
                .align(Alignment.BottomEnd)
                .background(Color(0xFF10B981), CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

/**
 * Kartu Kategori Olahraga
 */
@Composable
private fun SportCategoryCard(
    title: String,
    tagText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visualContent: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Area Visual Lapangan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.15f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                // Konten Gambar / Visual Lapangan
                visualContent()

                // Tag Badge di Kiri Atas
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

            // Nama Kategori & Ikon Chevron
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    ArenovaTheme {
        DashboardScreen(userName = "Angga Dwi Saputro")
    }
}
