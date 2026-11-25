package com.example.arenova.ui.screens

import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
private val RevoraGreenPrimary = Color(0xFF00B368)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

data class CourtDetailItem(
    val id: String = "1",
    val name: String = "Lapangan 1",
    val sportType: String = "Futsal",
    val courtTag: String = "INDOOR COURT A",
    val location: String = "Kebayoran Baru, Jakarta Selatan",
    val price: String = "Rp 80.000",
    val rating: String = "4.9",
    val reviewCount: String = "128 Ulasan",
    val imageRes: Int = R.drawable.img_futsal,
    val description: String = "Lapangan berstandar turnamen nasional dengan material vinyl interlock berkualitas tinggi yang meredam benturan lutut secara optimal. Dilengkapi dengan tata lampu LED anti-glare 800 lux, sirkulasi udara sejuk, ruang ganti privat, serta kantin dengan koneksi WiFi berkecepatan tinggi.",
    val courtSize: String = "25 x 15 Meter",
    val rateType: String = "Tarif Reguler",
    val rules: List<String> = listOf(
        "Wajib memakai sepatu sol karet (non-marking shoes).",
        "Hadir minimal 10 menit sebelum waktu sewa dimulai.",
        "Dilarang merokok dan membawa makanan berat ke lapangan."
    )
) {
    companion object
}

fun CourtDetailItem.Companion.fromListItem(
    id: String,
    name: String,
    location: String,
    price: String,
    rating: String,
    imageRes: Int,
    sportType: String = "Futsal",
    courtTag: String = "INDOOR COURT A"
): CourtDetailItem {
    return CourtDetailItem(
        id = id,
        name = name,
        sportType = sportType,
        courtTag = courtTag,
        location = location,
        price = price,
        rating = rating,
        imageRes = imageRes
    )
}

@Composable
fun CourtDetailScreen(
    court: CourtDetailItem = CourtDetailItem(),
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSelectScheduleClick: (CourtDetailItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ArenovaScreenBg,
        topBar = {
            CourtDetailTopBar(
                onBackClick = onBackClick,
                onShareClick = {
                    onShareClick()
                    Toast.makeText(context, "Membagikan info ${court.name}", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            CourtDetailBottomBar(
                price = court.price,
                onSelectScheduleClick = { onSelectScheduleClick(court) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // ==========================================
            // 1. HERO COURT IMAGE & FLOATING BADGES
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = court.imageRes),
                    contentDescription = court.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Bottom gradient vignette for high legibility
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                            )
                        )
                )

                // Top-Left Badge: "Tersedia Hari Ini"
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp, top = 12.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = Color.White.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(color = Color(0xFF22C55E), shape = CircleShape)
                        )
                        Text(
                            text = "Tersedia Hari Ini",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ArenovaDarkText
                        )
                    }
                }

                // Bottom Badges: Court Type Tag + Star Rating
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFF97316),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = court.courtTag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White.copy(alpha = 0.95f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = "Rating",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${court.rating} (${court.reviewCount})",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 2. KARTU INFORMASI UTAMA
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = court.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location_pin),
                            contentDescription = "Lokasi",
                            tint = RevoraGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = court.location,
                            fontSize = 13.sp,
                            color = ArenovaSubtitleGray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = court.price,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA580C)
                        )
                        Text(
                            text = " /jam",
                            fontSize = 14.sp,
                            color = ArenovaSubtitleGray,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                }
            }

            // ==========================================
            // 3. KARTU DESKRIPSI LAPANGAN & INFO BOXES
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Deskripsi Lapangan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = court.description,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Box 1: Tipe Olahraga
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF0F4FF)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (court.sportType.contains("Badminton", ignoreCase = true) ||
                                                court.sportType.contains("Padel", ignoreCase = true)
                                            ) {
                                                R.drawable.ic_racket
                                            } else {
                                                R.drawable.ic_ball_soccer
                                            }
                                        ),
                                        contentDescription = "Tipe Olahraga",
                                        tint = RevoraGreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = "Tipe Olahraga",
                                        fontSize = 11.sp,
                                        color = ArenovaSubtitleGray
                                    )
                                    Text(
                                        text = court.sportType,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ArenovaDarkText
                                    )
                                }
                            }
                        }

                        // Box 2: Ukuran Area
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF0F4FF)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_area_size),
                                        contentDescription = "Ukuran Lapangan",
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = "Ukuran Area",
                                        fontSize = 11.sp,
                                        color = ArenovaSubtitleGray
                                    )
                                    Text(
                                        text = court.courtSize,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ArenovaDarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 4. KARTU KETENTUAN LAPANGAN
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Ketentuan Lapangan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    court.rules.forEach { rule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(20.dp)
                                    .background(
                                        color = Color(0xFFDCFCE7),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check_circle),
                                    contentDescription = "Ketentuan",
                                    tint = RevoraGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = rule,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = Color(0xFF334155),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CourtDetailTopBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ArenovaScreenBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Detail Lapangan",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Bagikan",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(20.dp)
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
                        contentDescription = "Profil Pengguna",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CourtDetailBottomBar(
    price: String,
    onSelectScheduleClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color(0xFFF1F5F9),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estimasi Total",
                        fontSize = 11.5.sp,
                        color = ArenovaSubtitleGray
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = price,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Button(
                    onClick = onSelectScheduleClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = RevoraGreenPrimary.copy(alpha = 0.35f),
                            spotColor = RevoraGreenPrimary.copy(alpha = 0.45f)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = RevoraGreenGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "PILIH JADWAL",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "Pilih Jadwal",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourtDetailScreenPreview() {
    ArenovaTheme {
        CourtDetailScreen()
    }
}