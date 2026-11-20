package com.example.arenova.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

@Composable
fun DashboardScreen(
    userName: String = "Pengguna",
    onCategoryClick: (categoryName: String) -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 1. HEADER (Greeting & Profile Avatar)
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

                // Avatar Pengguna dengan Status Dot Hijau
                UserAvatarWithStatus(
                    onClick = onProfileClick
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // 2. SEARCH BAR & FILTER
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
            // 3. CARD MAIN BARENG KOMUNITAS
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
            // 4. SECTION KATEGORI (4 Olahraga Populer)
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
                    BadmintonCourtVisual()
                }

                SportCategoryCard(
                    title = "Futsal",
                    tagText = "Vinyl",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Futsal") }
                ) {
                    FutsalCourtVisual()
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
                    MiniSoccerCourtVisual()
                }

                SportCategoryCard(
                    title = "Padel",
                    tagText = "Glass Court",
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClick("Padel") }
                ) {
                    PadelCourtVisual()
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // 5. BANNER INFO JADWAL AKHIR PEKAN
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
}

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
            // Siluet wajah tersenyum sporty
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
                // Ilustrasi Visual Lapangan
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

// =========================================================================
// VISUAL ILUSTRASI 4 LAPANGAN OLAHRAGA
// =========================================================================

/**
 * 1. Lapangan Badminton (Indoor hijau dengan net & garis putih)
 */
@Composable
private fun BadmintonCourtVisual() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Latar Belakang Dinding Indoor Hall
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF2C3E50),
                    Color(0xFF34495E),
                    Color(0xFF1B4D3E)
                )
            )
        )

        // Karpet Lantai Hijau Badminton (Perspektif)
        val courtPath = Path().apply {
            moveTo(0f, h * 0.35f)
            lineTo(w, h * 0.35f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = courtPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF007E4A), Color(0xFF00B368))
            )
        )

        // Garis Lapangan Badminton Putih
        val stroke = Stroke(width = 2.dp.toPx())
        val lineColor = Color.White.copy(alpha = 0.9f)

        // Garis Tepi Dalam & Luar
        drawLine(lineColor, Offset(w * 0.12f, h * 0.35f), Offset(w * 0.04f, h), 2.dp.toPx())
        drawLine(lineColor, Offset(w * 0.88f, h * 0.35f), Offset(w * 0.96f, h), 2.dp.toPx())
        drawLine(lineColor, Offset(w * 0.08f, h * 0.65f), Offset(w * 0.92f, h * 0.65f), 2.dp.toPx())
        drawLine(lineColor, Offset(w * 0.04f, h * 0.92f), Offset(w * 0.96f, h * 0.92f), 2.dp.toPx())
        drawLine(lineColor, Offset(w * 0.50f, h * 0.35f), Offset(w * 0.50f, h), 2.dp.toPx())

        // Net Badminton di Tengah
        drawRect(
            color = Color.White.copy(alpha = 0.45f),
            topLeft = Offset(0f, h * 0.30f),
            size = Size(w, h * 0.08f)
        )
        // Garis Putih Atas Net
        drawLine(Color.White, Offset(0f, h * 0.30f), Offset(w, h * 0.30f), 3.dp.toPx())
    }
}

/**
 * 2. Lapangan Futsal (Vinyl biru & hijau terang arena luas)
 */
@Composable
private fun FutsalCourtVisual() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Struktur Atap & Dinding Hall Futsal
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF475569),
                    Color(0xFF94A3B8),
                    Color(0xFF1E293B)
                )
            )
        )

        // Lantai Futsal Hijau
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF059669), Color(0xFF10B981)),
                startY = h * 0.38f,
                endY = h
            ),
            topLeft = Offset(0f, h * 0.38f),
            size = Size(w, h * 0.62f)
        )

        // Area D Penalti Biru Vinyl Khas Futsal
        val blueDPath = Path().apply {
            moveTo(w * 0.25f, h)
            cubicTo(
                w * 0.25f, h * 0.58f,
                w * 0.75f, h * 0.58f,
                w * 0.75f, h
            )
            close()
        }
        drawPath(
            path = blueDPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
            )
        )

        // Garis Lingkaran Penalti Putih
        val stroke = Stroke(width = 2.dp.toPx())
        drawPath(
            path = blueDPath,
            color = Color.White,
            style = stroke
        )
    }
}

/**
 * 3. Lapangan Mini Soccer (Rumput sintetis outdoor senja)
 */
@Composable
private fun MiniSoccerCourtVisual() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Langit Senja / Dusk Twilight Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1E3A8A),
                    Color(0xFFD97706),
                    Color(0xFFF59E0B)
                ),
                startY = 0f,
                endY = h * 0.40f
            ),
            topLeft = Offset.Zero,
            size = Size(w, h * 0.40f)
        )

        // Tiang Lampu Stadion Mini
        drawLine(Color(0xFFCBD5E1), Offset(w * 0.22f, h * 0.15f), Offset(w * 0.22f, h * 0.40f), 2.dp.toPx())
        drawLine(Color(0xFFCBD5E1), Offset(w * 0.78f, h * 0.15f), Offset(w * 0.78f, h * 0.40f), 2.dp.toPx())
        // Cahaya Lampu
        drawCircle(Color(0xFFFEF08A), 4.dp.toPx(), Offset(w * 0.22f, h * 0.15f))
        drawCircle(Color(0xFFFEF08A), 4.dp.toPx(), Offset(w * 0.78f, h * 0.15f))

        // Rumput Sintetis Hijau Segar
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF15803D), Color(0xFF16A34A), Color(0xFF22C55E)),
                startY = h * 0.40f,
                endY = h
            ),
            topLeft = Offset(0f, h * 0.40f),
            size = Size(w, h * 0.60f)
        )

        // Garis Kotak Penalti Mini Soccer
        val penaltyBox = Path().apply {
            moveTo(w * 0.18f, h)
            lineTo(w * 0.25f, h * 0.65f)
            lineTo(w * 0.75f, h * 0.65f)
            lineTo(w * 0.82f, h)
        }
        drawPath(
            path = penaltyBox,
            color = Color.White.copy(alpha = 0.85f),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * 4. Lapangan Padel (Glass Court biru modern dengan dinding kaca & tiang hitam)
 */
@Composable
private fun PadelCourtVisual() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Latar Belakang Luar Lapangan
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFE2E8F0), Color(0xFF94A3B8))
            )
        )

        // Lantai Padel Biru Elektrik Modern
        val floorPath = Path().apply {
            moveTo(0f, h * 0.40f)
            lineTo(w, h * 0.40f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = floorPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF1D4ED8), Color(0xFF2563EB), Color(0xFF3B82F6))
            )
        )

        // Garis Lapangan Padel Putih
        val stroke = Stroke(width = 2.dp.toPx())
        drawLine(Color.White, Offset(w * 0.50f, h * 0.40f), Offset(w * 0.50f, h), 2.dp.toPx())
        drawLine(Color.White, Offset(w * 0.15f, h * 0.68f), Offset(w * 0.85f, h * 0.68f), 2.dp.toPx())

        // Dinding Kaca & Rangka Besi Hitam Padel
        val cageColor = Color(0xFF0F172A)
        drawLine(cageColor, Offset(w * 0.22f, h * 0.10f), Offset(w * 0.22f, h * 0.70f), 3.dp.toPx())
        drawLine(cageColor, Offset(w * 0.50f, h * 0.10f), Offset(w * 0.50f, h * 0.70f), 3.dp.toPx())
        drawLine(cageColor, Offset(w * 0.78f, h * 0.10f), Offset(w * 0.78f, h * 0.70f), 3.dp.toPx())
        drawLine(cageColor, Offset(0f, h * 0.25f), Offset(w, h * 0.25f), 2.dp.toPx())

        // Efek Kilau Kaca
        drawRect(
            color = Color.White.copy(alpha = 0.18f),
            topLeft = Offset(w * 0.23f, h * 0.12f),
            size = Size(w * 0.26f, h * 0.45f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    ArenovaTheme {
        DashboardScreen()
    }
}
